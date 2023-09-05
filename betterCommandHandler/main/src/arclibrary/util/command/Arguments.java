package arclibrary.util.command;


import arc.func.Prov;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Arguments {
    private final String[] rawStrings;
    private final Object[] handledObjects;
    private final short[] indexMapper;
    private BetterCommandHandler.BCommand myCommand;
    private String commandWithPrefix;

    public Arguments(String[] rawStrings, short[] indexMapper) {
        this.rawStrings = rawStrings;
        this.indexMapper = indexMapper;
        handledObjects = new Object[rawStrings.length];
    }

    public Arguments setCommand(BetterCommandHandler.BCommand myCommand, String commandWithPrefix) {
        this.myCommand = myCommand;
        this.commandWithPrefix = commandWithPrefix;
        return this;
    }

    public BetterCommandHandler.BCommand command() {
        return myCommand;
    }

    public String commandWithPrefix() {
        return commandWithPrefix;
    }

    public String[] getCopyRawStrings() {
        String[] strings = new String[rawStrings.length];
        System.arraycopy(rawStrings, 0, strings, 0, strings.length);
        return strings;
    }

    /**
     * @param paramIndex index of param in string descriptor of CommandParams
     *                   Example:
     *                   [it1] [it2]
     *                   ^^^^  ^^^^
     *                   idx0  idx1
     */
    public String getRawString(int paramIndex) {
        return rawStrings[indexMapper[paramIndex]];
    }

    /**
     * @param paramIndex index of param in string descriptor of CommandParams
     *                   Example:
     *                   [it1] [it2]
     *                   ^^^^  ^^^^
     *                   idx0  idx1
     */
    @Contract(value = "_,!null->!null;_,null->_")
    public String getRawStringOrDefault(int paramIndex, String def) {
        if (!has(paramIndex)) return def;
        return getRawString(paramIndex);
    }

    public void setHandledObject(int index, Object object) {
        handledObjects[indexMapper[index]] = object;
    }

    /**
     * @deprecated use {@link Arguments#has(int)}
     */
    @Deprecated
    public boolean hasParam(int i) {
        return has(i);
    }

    /**
     * @param paramIndex index of param in string descriptor of CommandParams
     *                   Example:
     *                   [it1] [it2]
     *                   ^^^^  ^^^^
     *                   idx0  idx1
     */
    public boolean has(int paramIndex) {
        if (indexMapper.length == 0) return false;
        return indexMapper[paramIndex] != -1;
    }


    /**
     * @deprecated use {@link Arguments#get(int)}
     */
    @Deprecated
    public <T> T getParam(int index) {
        return get(index);

    }

    /**
     * @param paramIndex index of param in string descriptor of CommandParams
     *                   Example:
     *                   [it1] [it2]
     *                   ^^^^  ^^^^
     *                   idx0  idx1
     */
    public <T> T get(int paramIndex) {
        //noinspection unchecked
        return (T) handledObjects[indexMapper[paramIndex]];
    }

    @Nullable
    public <T> T getOrNull(int paramIndex) {
        return getOrDefault(paramIndex, null);
    }

    @Contract(value = "_,!null->!null;_,null->_")
    public <T> T getOrDefault(int paramIndex, @Nullable T def) {
        if (!has(paramIndex)) return def;
        return get(paramIndex);
    }

    public <T> T getOrCraeteDefault(int paramIndex, @NotNull Prov<T> def) {
        if (!has(paramIndex)) return def.get();
        return get(paramIndex);
    }

    public void reset() {
        myCommand = null;
        commandWithPrefix = null;
    }
}
