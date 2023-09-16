package arclibrary.util.command.paramHanders;

import arc.graphics.Color;
import arc.util.Reflect;
import arc.util.Structs;
import arclibrary.util.command.Arguments;
import arclibrary.util.command.BetterCommandHandler;
import arclibrary.util.command.ParamHandler;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class ColorParamHandler implements ParamHandler<Color> {
    public static final Pattern hexColorRegex = Pattern.compile("#?([0123456789abcdefABCDEF]{6}|[0123456789abcdefABCDEF]{8})");
    public String errorMessage;

    public ColorParamHandler(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public Result<Color> handle(BetterCommandHandler.BCommand command, Arguments arguments, int index) {
        String string = arguments.getRawString(index);
        Color result;
        if (hexColorRegex.matcher(string).matches()) {
            result = Color.valueOf(string);
        } else {
            Field found = Structs.find(Color.class.getDeclaredFields(), it -> it.getName().equals(string));
            if (found != null && found.getType() == Color.class) {
                result = Reflect.get(found);
            } else {
                return Result.error(errorMessage);
            }
        }
        return Result.success(result);
    }
}
