package arclibrary.util.command;

import arc.struct.ObjectMap;
import arc.util.Reflect;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimpleHandlerTest extends AdvancedCommandHandlerPartTest {
    final StringBuilder output = new StringBuilder();

    @Before
    public void prepare() {
        output.setLength(0);
        handler.getCommandList().clear();
        //noinspection rawtypes
        ObjectMap commands = Reflect.get(BetterCommandHandler.class, handler, "commands");
        commands.clear();

        handler.aregister("empty", "", () -> {
            output.append("empty");
        });
        handler.aregister("optional", "[arg]", "", args -> {
            output.append("optional ").append(args.hasParam(0) ? args.getRawString(0) : "\n");
        });
        handler.aregister("required", "<arg>", "", args -> {
            output.append("required ").append(args.getRawString(0));
        });
        handler.setPrefix("");
    }

    @Test
    public void testEmptyArgs() {
        handleMessage("empty", BetterCommandHandler.BResponseType.valid);
        assertOutput("empty");

        handleMessage("optional", BetterCommandHandler.BResponseType.valid);
        assertOutput("optional \n");

        handleMessage("required", BetterCommandHandler.BResponseType.fewArguments);
        assertOutput("");
    }

    @Test
    public void testManyArgument() {
        handleMessage("required 1 2", BetterCommandHandler.BResponseType.manyArguments);
        assertOutput("");
    }

    @Test
    public void testNoCommand() {
        handler.setPrefix("/");
        handleMessage("hello world", BetterCommandHandler.BResponseType.noCommand);
    }

    @Test
    public void testUnknownCommand() {
        handleMessage("hello", BetterCommandHandler.BResponseType.unknownCommand);
    }

    private void assertOutput(String empty) {
        Assert.assertEquals(empty, output.toString());
        output.setLength(0);
    }

}
