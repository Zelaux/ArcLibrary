package arclibrary.util.command;


public class CommandArguments {
    private final String[] rawStrings;
    private final Object[] handledObjects;
    private final short[] indexMapper;

    public CommandArguments(String[] rawStrings, short[] indexMapper) {
        this.rawStrings = rawStrings;
        this.indexMapper = indexMapper;
        handledObjects = new Object[rawStrings.length];
    }

    public String[] getCopyRawStrings() {
        String[] strings = new String[rawStrings.length];
        System.arraycopy(rawStrings, 0, strings, 0, strings.length);
        return strings;
    }

    public String getRawString(int index) {
        return rawStrings[indexMapper[index]];
    }

    public void setHandledObject(int index, Object object) {
        handledObjects[indexMapper[index]] = object;
    }

    public boolean hasParam(int i) {
        if (indexMapper.length == 0) return false;
        return indexMapper[i] != -1;
    }

    public <T> T getParam(int index) {
        return (T) handledObjects[indexMapper[index]];
    }
}
