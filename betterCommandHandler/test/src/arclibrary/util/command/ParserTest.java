package arclibrary.util.command;

import arclibrary.util.command.BetterCommandHandler.BCommandParam;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest extends AdvancedCommandHandlerPartTest {
    protected static void checkError(String message, Runnable runnable) {
        //noinspection unchecked
        checkError(message, new Class[]{ParamPatternParseException.class}, runnable);
    }

    static <T> T[] arr(T... arr) {
        return arr;
    }

    @Test
    public void testUnexpectedChar() {
        checkError("Unexpected char 'd' at [11:12]\n" +
                "[a] <b...> d[c] <d>\n" +
                "           ^\n" +
                "           Unexpected char 'd'", () -> {
            //noinspection ParameterOrder,VariadicParamPosition
            ParamPatternParser.parse("[a] <b...> d[c] <d>");
        });
        checkError("Cannot be more than one variadic parameter! at [11:17]\n" +
                "[a] <b...> [c...] <d>\n" +
                "           ^^^^^^\n" +
                "           Cannot be more than one variadic parameter!", () -> {
            //noinspection ParameterOrder,VariadicParamPosition
            ParamPatternParser.parse("[a] <b...> [c...] <d>");
        });
        checkError("Malformed param '<>' at [15:17]\n" +
                "[a] <b...> [c] <>\n" +
                "               ^^\n" +
                "               Malformed param '<>'", () -> {
            //noinspection ParameterOrder,VariadicParamPosition
            ParamPatternParser.parse("[a] <b...> [c] <>");
        });
        checkError(null, () -> {
            //noinspection ParameterOrder,VariadicParamPosition
            ParamPatternParser.parse("[a]<b...>[c]<d>");
        });
    }

    @Test
    public void testParsingVariadic() {
        Assert.assertArrayEquals(arr(new BCommandParam("it", true, true, null)), ParamPatternParser.parse("[it...]").params);
    }
    @Test
    public void testHandlers() {
        Assert.assertArrayEquals(arr(new BCommandParam("it(some)", true, true, "some")), ParamPatternParser.parse("[it(some)...]").params);
        Assert.assertArrayEquals(arr(new BCommandParam("it(some)", true, true, "some")), ParamPatternParser.parse("[it...(some)]").params);
    }
}
