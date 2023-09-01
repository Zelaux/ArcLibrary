package arclibrary.util.command;

public class DefaultCommandHandlers {
    public static final ParamHandler<Integer> intHandler = (command, arguments, index) -> {
        String string = arguments.getRawString(index);
        try {
            return ParamHandler.Result.success(Integer.parseInt(string));
        } catch (NumberFormatException e) {
            return ParamHandler.Result.error('\'' + string + "' is not an integer");
        }
    };
    public static final ParamHandler<Float> floatHandler = (command, arguments, index) -> {
        String string = arguments.getRawString(index);
        try {
            return ParamHandler.Result.success(Float.parseFloat(string));
        } catch (NumberFormatException e) {
            return ParamHandler.Result.error('\'' + string + "' is not an floating number");
        }
    };
    public static final ParamHandler<Boolean> booleanHandler = (command, arguments, index) -> {
        return ParamHandler.Result.success(Boolean.valueOf(arguments.getRawString(index)));
    };

    public static void add(BetterCommandHandler handler) {
        handler.registerHandler("digit", intHandler);
        handler.registerHandler("floating", floatHandler);
        handler.registerHandler("boolean", booleanHandler);
    }
}
