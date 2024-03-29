package arclibrary.util.command;


import org.junit.Assert;
import org.junit.Test;

public class SplitterTest {
    static <T> T[] array(T... array) {
        return array;
    }

    @Test
    public void test() {
        //noinspection ParameterOrder,VariadicParamPosition
        ParamPattern params = ParamPatternParser.parse("[a] <b...> [c] <d>");
        Assert.assertTrue(ArgumentSplitter.split("1", params).few);

        //noinspection ParameterOrder,VariadicParamPosition
        Assert.assertTrue(ArgumentSplitter.split("1 2 3 4", ParamPatternParser.parse("<a> <b> <c>")).many);
        Assert.assertArrayEquals(
                array("1", "2"),
                ArgumentSplitter.split("1 2", params).args.getCopyRawStrings()
        );
        Assert.assertArrayEquals(
                array(),
                ArgumentSplitter.split("", ParamPatternParser.parse("")).args.getCopyRawStrings()
        );
        Assert.assertTrue(
                ArgumentSplitter.split("", ParamPatternParser.parse("<1>")).few
        );
        Assert.assertArrayEquals(
                array("1", "2", "3"),
                ArgumentSplitter.split("1 2 3", params).args.getCopyRawStrings()
        );
        Assert.assertArrayEquals(
                array("1", "2", "3", "4"),
                ArgumentSplitter.split("1 2 3 4", params).args.getCopyRawStrings()
        );
        Assert.assertArrayEquals(
                array("1", "2 3", "4", "5"),
                ArgumentSplitter.split("1 2 3 4 5", params).args.getCopyRawStrings()
        );
    }
}
