package arclibrary.util.command;

import org.junit.Test;
import zelauxtests.StringAssert;

public class OverridingTest extends AdvancedCommandHandlerPartTest {


    @Test
    public void testOverridingReport() {
        handler.bregister("test1", "", () -> {
        });
        handler.bregister("test1", "", () -> {
        });
        StringAssert.assertStartsWith("Overriding command 'test1 ' Creation stacktrace: \n" +
                "arclibrary.util.command.StackTraceException\n" +
                "\tat arclibrary.util.command.OverridingTest.testOverridingReport(OverridingTest.java:11)", buffer.toString().replace(System.lineSeparator(), "\n"));
    }

    @Test
    public void testOverridingNotAllowed() {
        handler.allowNameOverriding = false;
        handler.bregister("test1", "", () -> {
        });
        //noinspection unchecked
        checkError(it -> StringAssert.assertStartsWith("Overriding command 'test1 ' Creation stacktrace: \n" +
                "arclibrary.util.command.StackTraceException\n" +
                "\tat arclibrary.util.command.OverridingTest.testOverridingNotAllowed(OverridingTest.java:23)", it.replace(System.lineSeparator(),"\n")), new Class[]{CommandOverridingNotAllowed.class}, () -> {
            handler.bregister("test1", "", () -> {
            });
        });
    }

}
