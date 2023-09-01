package arclibrary.util.command;

import arc.func.Cons;
import arc.util.Strings;
import arc.util.Structs;
import org.junit.Assert;

import java.util.Arrays;

public abstract class AdvancedCommandHandlerPartTest {
    protected final BetterCommandHandler handler;
    protected final StringBuilder buffer = new StringBuilder();

    {
        BetterCommandHandler.defaultNameOverridingLogger = new NameOverridingLogger.Impl(buffer::append);
        handler = new BetterCommandHandler();
    }

    protected static void checkError(String message, Class<? extends Throwable>[] exceptionTypes, Runnable runnable) {
        try {
            runnable.run();
            if (message != null) Assert.fail("Expected error with message: '" + message + "'");
        } catch (Exception e) {
            Class<?> exceptionClass = realClass(e);
            Assert.assertTrue("Unexpected exception type " + Strings.getStackTrace(e), Structs.contains(exceptionTypes, exceptionClass));
            Assert.assertEquals(message, e.getMessage());
        }
    }

    protected static void checkError(Cons<String> messageChecker, Class<? extends Throwable>[] exceptionTypes, Runnable runnable) {
        try {
            runnable.run();
            if (messageChecker != null) Assert.fail("Expected error : '" + Arrays.toString(exceptionTypes) + "'");
        } catch (Exception e) {
            Class<?> exceptionClass = realClass(e);
            Assert.assertTrue("Unexpected exception type " + Strings.getStackTrace(e), Structs.contains(exceptionTypes, exceptionClass));
            messageChecker.get(e.getMessage());
        }
    }

    private static Class<?> realClass(Object e) {
        Class<?> eClass = e.getClass();
        if (eClass.isAnonymousClass()) {
            eClass = eClass.getSuperclass();
        }
        return eClass;
    }

    protected BetterCommandHandler.BCommandResponse handleMessage(String text, BetterCommandHandler.BResponseType expectedType) {
        BetterCommandHandler.BCommandResponse response = handler.handleMessage(text);
        Assert.assertEquals(expectedType, response.aType);
        return response;
    }
}
