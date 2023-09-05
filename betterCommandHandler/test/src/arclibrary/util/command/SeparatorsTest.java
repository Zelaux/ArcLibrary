package arclibrary.util.command;

import arc.math.Mathf;
import org.junit.Assert;
import org.junit.Test;

import static arc.util.Structs.arr;

public class SeparatorsTest extends AdvancedCommandHandlerPartTest {

    @Test
    public void testDots() {
        handler.defaultSeparators('.');
        handler.bregister("test", "<1><2><3>", "", (args) -> {
            Assert.assertArrayEquals(arr("1", "2", "3"), args.getCopyRawStrings());
        });
        handleMessage("test 1 2 3", BetterCommandHandler.BResponseType.fewArguments);
        handleMessage("test 1.2.3", BetterCommandHandler.BResponseType.valid);
    }

    @Test
    public void testNewLines() {
        handler.defaultSeparators('\n',' ','\t');
        handler.bregister("test", "<1><2><3>", "", (args) -> {
            Assert.assertArrayEquals(arr("1", "2", "3"), args.getCopyRawStrings());
        });
        handleMessage("test 1 2 3", BetterCommandHandler.BResponseType.valid);
        handleMessage("test 1\n2 3", BetterCommandHandler.BResponseType.valid);
        handleMessage("test 1\n2\t3", BetterCommandHandler.BResponseType.valid);
        handleMessage("test 1 2\t3", BetterCommandHandler.BResponseType.valid);
        handleMessage("test 1 2 3", BetterCommandHandler.BResponseType.valid);
        handleMessage("test 1\t2\t3", BetterCommandHandler.BResponseType.valid);
        handleMessage("test 1\n2\n3", BetterCommandHandler.BResponseType.valid);
    }
}
