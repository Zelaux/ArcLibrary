package arclibrary.util.command;

import org.junit.Test;

public class ParserTest extends AdvancedCommandHandlerPartTest {
    protected static void checkError(String message, Runnable runnable) {
        //noinspection unchecked
        checkError(message, new Class[]{CommandParamParseException.class}, runnable);
    }
@Test
    public void testUnexpectedChar() {

        checkError("Unexpected char 'd' at [11:12]\n" +
                "[a] <b...> d[c] <d>\n" +
                "           ^\n" +
                "           Unexpected char 'd'", () -> {
            //noinspection ParameterOrder,VariadicParamPosition
            CommandParamParser.parse("[a] <b...> d[c] <d>");
        });
        checkError("Cannot be more than one variadic parameter! at [11:17]\n" +
                "[a] <b...> [c...] <d>\n" +
                "           ^^^^^^\n" +
                "           Cannot be more than one variadic parameter!", () -> {
            //noinspection ParameterOrder,VariadicParamPosition
            CommandParamParser.parse("[a] <b...> [c...] <d>");
        });
        checkError("Malformed param '<>' at [15:17]\n" +
                "[a] <b...> [c] <>\n" +
                "               ^^\n" +
                "               Malformed param '<>'", () -> {
            //noinspection ParameterOrder,VariadicParamPosition
            CommandParamParser.parse("[a] <b...> [c] <>");
        });
        checkError(null, () -> {
            //noinspection ParameterOrder,VariadicParamPosition
            CommandParamParser.parse("[a]<b...>[c]<d>");
        });
    }
}
