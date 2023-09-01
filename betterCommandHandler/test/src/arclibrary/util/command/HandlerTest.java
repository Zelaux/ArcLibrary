package arclibrary.util.command;

import arc.math.Mathf;
import org.junit.Assert;
import org.junit.Test;

public class HandlerTest extends AdvancedCommandHandlerPartTest {

    @Test
    public void testInt() {
        handler.registerHandler("digit", DefaultCommandHandlers.intHandler);
        handler.aregister("test1", "<digit(digit)>", "", (args) -> {
            Assert.assertEquals(Integer.class, args.getParam(0).getClass());
            int param = args.getParam(0);
            Assert.assertEquals(123, param);
        });
        handleMessage("test1 123", BetterCommandHandler.BResponseType.valid);
        handleMessage("test1 1dd", BetterCommandHandler.BResponseType.nonValidParam);
    }

    @Test
    public void testFloat() {
        handler.registerHandler("float", DefaultCommandHandlers.floatHandler);
        handler.aregister("test2", "<float(float)>", "", (args) -> {
            Assert.assertEquals(Float.class, args.getParam(0).getClass());
            float param = args.getParam(0);
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
        handler.aregister("test3", "<flag(bool)>", "", (args) -> {
            boolean testvalue = tests[testIdx[0]++];
            Assert.assertEquals(Boolean.class, args.getParam(0).getClass());
            boolean param = args.getParam(0);
            Assert.assertEquals(testvalue, param);
        });
        handleMessage("test3 true", BetterCommandHandler.BResponseType.valid);
        handleMessage("test3 false", BetterCommandHandler.BResponseType.valid);
        handleMessage("test3 ddd", BetterCommandHandler.BResponseType.valid);

    }
}
