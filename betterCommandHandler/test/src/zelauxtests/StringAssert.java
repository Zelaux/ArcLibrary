package zelauxtests;

import org.junit.Assert;
import org.junit.ComparisonFailure;

public class StringAssert {
    public static void assertStartsWith(String expected, String actual) {
        if (actual != null && actual.startsWith(expected)) {
        } else if (expected != null && actual != null) {
            throw new ComparisonFailure("", expected, actual);
        } else {
            Assert.fail(format(null, (Object) expected, (Object) actual));
        }
    }
    public static void assertContains(String expected, String actual) {
        if (actual != null && actual.contains(expected)) {
        } else if (expected != null && actual != null) {
            throw new ComparisonFailure("", expected, actual);
        } else {
            Assert.fail(format(null, (Object) expected, (Object) actual));
        }
    }
    static String format(String message, Object expected, Object actual) {
        String formatted = "";
        if (message != null && !message.equals("")) {
            formatted = message + " ";
        }
        String expectedString = String.valueOf(expected);
        String actualString = String.valueOf(actual);
        if (expectedString.equals(actualString)) {
            return formatted + "expected: "
                    + formatClassAndValue(expected, expectedString)
                    + " but was: " + formatClassAndValue(actual, actualString);
        } else {
            return formatted + "expected:<" + expectedString + "> but was:<"
                    + actualString + ">";
        }
    }

    private static String formatClassAndValue(Object value, String valueString) {
        String className = value == null ? "null" : value.getClass().getName();
        return className + "<" + valueString + ">";
    }
}
