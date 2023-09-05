package arclibrary.util.command;

public class ParamPatternParseException extends RuntimeException {
    public final int startIndex;
    public final int endIndex;
    public final String rawText;

    public ParamPatternParseException(String message, int startIndex, int endIndex, String rawText) {
        super(calculateMessage(message, rawText, startIndex, endIndex));
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.rawText = rawText;

    }


    private static String calculateMessage(String message, String rawText, int startIndex, int endIndex) {
        StringBuilder builder = new StringBuilder(message);
        appendRanges(builder, startIndex, endIndex);
        builder.append("\n").append(rawText).append("\n");
        for (int i = 0; i < startIndex; i++) {
            builder.append(" ");
        }
        for (int i = startIndex; i < endIndex; i++) {
            builder.append("^");
        }
        builder.append("\n");
        for (int i = 0; i < startIndex; i++) {
            builder.append(" ");
        }
        builder.append(message);
        return builder.toString();
    }

    private static void appendRanges(StringBuilder builder, int startIndex, int endIndex) {
        builder.append(" at ");

        builder.append('[')
                .append(startIndex)
                .append(":")
                .append(endIndex)
                .append(']');
    }
}
