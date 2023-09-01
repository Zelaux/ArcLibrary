package arclibrary.util.command;

import arc.struct.*;

import java.util.*;

public class CommandParamSplitter{
    private static final ThreadLocal<SplitResponse> response = ThreadLocal.withInitial(SplitResponse::new);
    private static final CommandArguments emptyArguments = new CommandArguments(new String[0], new short[0]);
    private static final ThreadLocal<IntSeq> tmpSeq = ThreadLocal.withInitial(IntSeq::new);

    public static SplitResponse split(String text, CommandParams pattern){
        return split(text, 0, text.length(), pattern);
    }

    public static SplitResponse split(String text, int startIndex, int endIndex, CommandParams pattern){
        if(endIndex - startIndex == 0){
            return pattern.requiredAmount == 0 ? response().args(emptyArguments) : response().few();
        }
        int spaces = 0;
        IntSeq tmpSeq = CommandParamSplitter.tmpSeq.get();
        tmpSeq.clear();
        tmpSeq.add(startIndex - 1);
        for(int i = startIndex; i < endIndex; i++){
            if(text.charAt(i) == ' '){
                tmpSeq.add(i);
                spaces++;
                if(spaces % 5 == 0){
                    if(spaces + 1 > pattern.params.length && pattern.variadicIndex == -1) return response().many();
                }
            }
        }

        if(spaces + 1 < pattern.requiredAmount) return response().few();

        int expandVariadic = spaces + 1 - pattern.params.length;
        if(expandVariadic > 0 && pattern.variadicIndex == -1) return response().many();
        int givenParams = Math.min(pattern.params.length, spaces + 1);
        int optionalLeft = givenParams - pattern.requiredAmount;
        short[] indexMapper = mapperForPattern(pattern);
        String[] rawStrings = new String[givenParams];

        tmpSeq.add(endIndex);
        for(int paramIndex = 0, spaceIndex = 0, argIndex = 0; paramIndex < pattern.params.length; paramIndex++){

            if(pattern.params[paramIndex].optional){
                if(optionalLeft <= 0){
                    continue;
                }else optionalLeft--;
            }
            int begin = tmpSeq.get(spaceIndex) + 1;
            if(pattern.variadicIndex == paramIndex && expandVariadic > 0){
                spaceIndex += expandVariadic;
            }
            int end = tmpSeq.get(spaceIndex + 1);
            rawStrings[argIndex] = text.substring(begin, end);
            indexMapper[paramIndex] = (short)argIndex;
            argIndex++;
            spaceIndex++;
        }
        return response().args(new CommandArguments(rawStrings, indexMapper));
    }

    private static SplitResponse response() {
        return response.get();
    }

    private static short[] mapperForPattern(CommandParams pattern){
        short[] mapper = new short[pattern.params.length];
        Arrays.fill(mapper, (short)-1);
        return mapper;
    }

    public static class SplitResponse{
        public boolean many;
        public boolean few;
        public CommandArguments args;

        public SplitResponse many(){
            reset();
            this.many = true;
            return this;
        }

        private void reset(){
            few = many = false;
            args = null;
        }

        public SplitResponse few(){
            reset();
            this.few = true;
            return this;
        }

        public SplitResponse args(CommandArguments args){
            reset();
            this.args = args;
            return this;
        }
    }
}
