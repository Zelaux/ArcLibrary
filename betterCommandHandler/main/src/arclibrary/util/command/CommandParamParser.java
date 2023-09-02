package arclibrary.util.command;

import arc.struct.Seq;
import arclibrary.utils.pools.ThreadSafePool;
import arclibrary.utils.pools.ThreadSafePoolImpl;
import org.intellij.lang.annotations.Language;

public class CommandParamParser {
    static final ThreadSafePool<TextRegion> textRegionPool = new ThreadSafePoolImpl<>(() -> new TextRegion() {
    });
    private static final byte ERROR_STATE = -1;
    private static final byte searchParam = 0;
    private static final byte parsingRequired = 0b1;
    private static final byte parsingOptional = 0b10;
    private static final byte parsingHandler = 0b100;
    private static final ThreadLocal<CommandParamParserState> state = ThreadLocal.withInitial(CommandParamParserState::new);

    public static int getParamAmount(@Language("ExtendedArcCommandParams") String text) {
        Seq<TextRegion> tmpRegions = state.get().tmpRegions;
        collectRegions(text, tmpRegions, true);
        int size = tmpRegions.size;
        clear(tmpRegions);
        return size;
    }

    public static CommandParams parse(@Language("ExtendedArcCommandParams") String text) throws CommandParamParseException {
        final Seq<TextRegion> tmpRegions = state.get().tmpRegions;
        collectRegions(text, tmpRegions, true);
        BetterCommandHandler.BCommandParam[] params = new BetterCommandHandler.BCommandParam[tmpRegions.size];
        boolean wasVariadic = false;
        for (int i = 0; i < tmpRegions.size; i++) {
            TextRegion region = tmpRegions.get(i);
            boolean isVariadic = false;
            int nameOffset = 0;
            if (region.length() > 5) {
                int lastChar = region.end - 1;
                if (region.isEndsByHandler()) {
                    lastChar = region.handlerStart - 1;
                    if (region.handlerStart - region.start < 5/*open char+one symbol+'...'*/) {
                        lastChar = -1;
                    }
                }
                if (lastChar > 0) {
                    for (int j = 0; ; j++) {
                        if (text.charAt(lastChar - 1 - i) != '.') break;
                        if (j == 2) {
                            if (wasVariadic) {
                                throwException("Cannot be more than one variadic parameter!", region, text);
                            }
                            isVariadic = wasVariadic = true;
                            nameOffset = 3;
                            break;
                        }
                    }
                }
            }
            boolean isOptional = text.charAt(region.start) == '[';
            if (region.isEndsByHandler() && isVariadic) {
                params[i] = new BetterCommandHandler.BCommandParam(
                        text.substring(region.start + 1, region.handlerStart - 1 - nameOffset)+"(" + region.substringHandler(text)+")",
                        isOptional,
                        true, region.hasHandler() ? region.substringHandler(text) : null);
            } else {
                params[i] = new BetterCommandHandler.BCommandParam(
                        text.substring(region.start + 1, region.end - 1 - nameOffset),
                        isOptional,
                        isVariadic, region.hasHandler() ? region.substringHandler(text) : null);
            }
        }
        clear(tmpRegions);
        return new CommandParams(params);
    }

    private static int collectRegions(String text, final Seq<TextRegion> tmpRegions, boolean canThrow) {
        clear(tmpRegions);
        byte state = searchParam;
        int begin = -1;
        int handlerBegin = -1, handleEnd = -1;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if ((state & parsingHandler) != 0) {
                if (c == ')') {
                    state ^= parsingHandler;
                    handleEnd = i;
                }
                continue;
            }
            switch (state & 0b11) {
                case searchParam:
                    if (c != ' ' && c != '<' && c != '[')
                        if (canThrow) {
                            throwException("Unexpected char '" + c + "'", i, i + 1, text);
                        } else {
                            clear(tmpRegions);
                            return -1;
                        }
                    if (c == '<' || c == '[') {
                        state = c == '<' ? parsingRequired : parsingOptional;
                        begin = i;
                    }
                    break;
                case parsingRequired:
                case parsingOptional:
                    if (c == '>' && (state & parsingRequired) != 0 || c == ']' && (state & parsingOptional) != 0) {
                        state = completeParam(text, tmpRegions, begin, i + 1, handlerBegin, handleEnd, canThrow);
                        if (state == ERROR_STATE) {
                            clear(tmpRegions);
                            return -1;
                        }
                        handlerBegin = handleEnd = -1;
                    }
                    if (c == '(') {
                        handlerBegin = i;
                        state |= parsingHandler;
                    }
                    break;

            }
        }
        return tmpRegions.size;
    }

    private static void clear(final Seq<TextRegion> tmpRegions) {
        textRegionPool.freeAll(tmpRegions);
        tmpRegions.clear();
    }

    private static byte completeParam(String text, final Seq<TextRegion> tmpRegions, int begin, int end, int handlerBegin, int handleEnd, boolean canThrow) {
        if (end - begin <= 2) {
            if (canThrow) {
                throwException("Malformed param '" + text.substring(begin, end) + "'",
                        begin, end, text
                );
            } else {
                return ERROR_STATE;
            }
        }

        tmpRegions.add(textRegion(begin, end, handlerBegin == -1 ? -1 : handlerBegin + 1, handleEnd));
        return searchParam;
    }

    private static TextRegion textRegion(int begin, int end, int handlerBegin, int handleEnd) {
        return textRegionPool.obtain().set(begin, end, handlerBegin, handleEnd);
    }

    static void throwException(String message, int startIndex, int endIndex, String rawText) {
        throw new CommandParamParseException(message, startIndex, endIndex, rawText);
    }

    static void throwException(String message, TextRegion region, String rawText) {
        throwException(message, region.start, region.end, rawText);
    }

    public static final class CommandParamParserState {
        final Seq<TextRegion> tmpRegions = new Seq<>();
    }

    private static abstract class TextRegion {
        public int start;
        public int end;
        public int handlerStart = 0;
        public int handlerEnd = 0;

        public TextRegion set(int start, int end, int handlerStart, int handlerEnd) {
            this.start = start;
            this.end = end;
            this.handlerStart = handlerStart;
            this.handlerEnd = handlerEnd;
            return this;
        }

        public boolean hasHandler() {
            return handlerStart != -1 && handlerEnd != -1;
        }

        public int length() {
            return end - start;
        }

        public String substring(String text) {
            return text.substring(start, end);
        }

        public String substringHandler(String text) {
            return text.substring(handlerStart, handlerEnd);
        }

        public boolean isEndsByHandler() {
            return end - 2 == handlerEnd;
        }
    }
}
