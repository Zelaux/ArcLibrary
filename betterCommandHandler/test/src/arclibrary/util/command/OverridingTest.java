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
        String message = buffer.toString().replace(System.lineSeparator(), "\n");
        StringAssert.assertStartsWith("Try to override command 'test1 ' Command creating stacktrace:\n" +
                "arclibrary.util.command.CreationStackTrace\n" +
                "\tat arclibrary.util.command.OverridingTest.testOverridingReport(OverridingTest.java:11)", message);
        StringAssert.assertContains("Overriding stacktrace:\n" +
                "arclibrary.util.command.StackTrace\n" +
                "\tat arclibrary.util.command.OverridingTest.testOverridingReport(OverridingTest.java:13)", message);
    }

    @Test
    public void testOverridingNotAllowed() {
        handler.allowNameOverriding = false;
        handler.bregister("test1", "", () -> {
        });
        //noinspection unchecked
        checkError(it -> {
            String message = it.replace(System.lineSeparator(), "\n");
            StringAssert.assertStartsWith("Try to override command 'test1 ' Command creating stacktrace:\n" +
                    "arclibrary.util.command.CreationStackTrace\n" +
                    "\tat arclibrary.util.command.OverridingTest.testOverridingNotAllowed(OverridingTest.java:27)", message);
            StringAssert.assertContains("Overriding stacktrace:\n" +
                    "arclibrary.util.command.StackTrace\n" +
                    "\tat arclibrary.util.command.OverridingTest.lambda$testOverridingNotAllowed$5(OverridingTest.java:39)", message);
        }, new Class[]{CommandOverridingNotAllowed.class}, () -> {
            handler.bregister("test1", "", () -> {
            });
        });
    }

}
