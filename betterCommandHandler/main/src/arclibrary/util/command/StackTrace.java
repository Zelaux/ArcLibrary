package arclibrary.util.command;

import arc.func.Boolf;
import arc.util.Strings;

public class StackTrace extends RuntimeException {
    StackTrace() {
        super();
    }

    public static StackTrace create() {
        return new StackTrace();
    }

    public String getStringStackTrace(Boolf<StackTraceElement> skipper) {
        StackTraceElement[] stackTrace = getStackTrace();
        int offset = 1;
        while (offset < stackTrace.length && skipper.get(stackTrace[offset])) {
            offset++;
        }
        StackTraceElement[] newStackTrace = new StackTraceElement[stackTrace.length - offset];
        System.arraycopy(stackTrace, offset, newStackTrace, 0, newStackTrace.length);
        setStackTrace(newStackTrace);
        return Strings.getStackTrace(this);
    }

    public static class AmountSkipper implements Boolf<StackTraceElement> {
        public int amount;

        public AmountSkipper(int amount) {
            this.amount = amount;
        }

        @Override
        public boolean get(StackTraceElement stackTraceElement) {
            amount--;
            return amount >= 0;
        }
    }
}
