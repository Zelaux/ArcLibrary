package arclibrary.util.command;

import arc.func.Boolf;
import arc.util.Strings;

public class StackTraceException extends RuntimeException {
    private StackTraceException() {
        super();
    }

    public static String getStringStackTrace(Boolf<StackTraceElement> skipper) {
        StackTraceException exception = new StackTraceException();
        StackTraceElement[] stackTrace = exception.getStackTrace();
        int offset = 1;
        while (offset < stackTrace.length && skipper.get(stackTrace[offset])) {
            offset++;
        }
        StackTraceElement[] newStackTrace=new StackTraceElement[stackTrace.length-offset];
        System.arraycopy(stackTrace,offset,newStackTrace,0,newStackTrace.length);
        exception.setStackTrace(newStackTrace);
        return Strings.getStackTrace(exception);
    }
}
