package arclibrary.util.command;

import arc.graphics.Color;
import arc.math.Mathf;
import arclibrary.util.command.paramHanders.ColorParamHandler;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

public class HandlerTest extends AdvancedCommandHandlerPartTest {

    @Test
    public void testInt() {
        handler.registerHandler("digit", DefaultCommandHandlers.intHandler);
        handler.bregister("test1", "<digit(digit)>", "", (args) -> {

            Assert.assertEquals(Integer.class, args.get(0).getClass());

            int param = args.get(0);
            Assert.assertEquals(123, param);
        });
        handleMessage("test1 123", BetterCommandHandler.BResponseType.valid);
        handleMessage("test1 1dd", BetterCommandHandler.BResponseType.nonValidParam);
    }

    @Test
    public void testFloat() {
        handler.registerHandler("float", DefaultCommandHandlers.floatHandler);
        handler.bregister("test2", "<float(float)>", "", (args) -> {
            Assert.assertEquals(Float.class, args.get(0).getClass());
            float param = args.get(0);
            Assert.assertEquals(12.3f, param, Mathf.FLOAT_ROUNDING_ERROR);
        });
        handleMessage("test2 12.3", BetterCommandHandler.BResponseType.valid);
        handleMessage("test2 1dd", BetterCommandHandler.BResponseType.nonValidParam);
    }

    @Test
    public void testBool() {
        handler.registerHandler("bool", DefaultCommandHandlers.booleanHandler);
        boolean[] tests = {
                true, false, false
        };
        int[] testIdx = {0};
        handler.bregister("test3", "<flag(bool)>", "", (args) -> {
            boolean testvalue = tests[testIdx[0]++];

            Assert.assertEquals(Boolean.class, args.get(0).getClass());

            boolean param = args.get(0);
            Assert.assertEquals(testvalue, param);
        });
        handleMessage("test3 true", BetterCommandHandler.BResponseType.valid);
        handleMessage("test3 false", BetterCommandHandler.BResponseType.valid);
        handleMessage("test3 ddd", BetterCommandHandler.BResponseType.valid);

    }

    @Test
    public void testColor() {
        handler.registerHandler("color", new ColorParamHandler("unknown color"));
        Color color = new Color(0.5f, 0.5f, 0.5f, 0.5f);
        Color[] tests = {
                Color.red,
                Color.red,
                Color.green,
                Color.green,
                Color.magenta,
                Color.magenta,
                color
        };
        int[] testIdx = {0};
        handler.bregister("test", "<color(color)>", "", (args) -> {
            Color testvalue = tests[testIdx[0]++];

            Assert.assertEquals(Color.class, args.get(0).getClass());

            Color param = args.get(0);
            Assert.assertEquals(testvalue, param);
        });
        handleMessage("test FF0000", BetterCommandHandler.BResponseType.valid);
        handleMessage("test red", BetterCommandHandler.BResponseType.valid);
        handleMessage("test #00FF00", BetterCommandHandler.BResponseType.valid);
        handleMessage("test green", BetterCommandHandler.BResponseType.valid);
        handleMessage("test #FF00FF", BetterCommandHandler.BResponseType.valid);
        handleMessage("test magenta", BetterCommandHandler.BResponseType.valid);
        handleMessage("test #00FF00d", BetterCommandHandler.BResponseType.nonValidParam);
        handleMessage("test 123", BetterCommandHandler.BResponseType.nonValidParam);
        handleMessage("test hahaColor", BetterCommandHandler.BResponseType.nonValidParam);
        handleMessage("test #" + color, BetterCommandHandler.BResponseType.valid);

    }
}
