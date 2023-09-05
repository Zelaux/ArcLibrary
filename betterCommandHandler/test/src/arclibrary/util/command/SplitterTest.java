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
        ParamsPattern params = CommandParamParser.parse("[a] <b...> [c] <d>");
        Assert.assertTrue(ParamSplitter.split("1", params).few);

        //noinspection ParameterOrder,VariadicParamPosition
        Assert.assertTrue(ParamSplitter.split("1 2 3 4", CommandParamParser.parse("<a> <b> <c>")).many);
        Assert.assertArrayEquals(
                array("1", "2"),
                ParamSplitter.split("1 2", params).args.getCopyRawStrings()
        );
        Assert.assertArrayEquals(
                array(),
                ParamSplitter.split("", CommandParamParser.parse("")).args.getCopyRawStrings()
        );
        Assert.assertTrue(
                ParamSplitter.split("", CommandParamParser.parse("<1>")).few
        );
        Assert.assertArrayEquals(
                array("1", "2", "3"),
                ParamSplitter.split("1 2 3", params).args.getCopyRawStrings()
        );
        Assert.assertArrayEquals(
                array("1", "2", "3", "4"),
                ParamSplitter.split("1 2 3 4", params).args.getCopyRawStrings()
        );
        Assert.assertArrayEquals(
                array("1", "2 3", "4", "5"),
                ParamSplitter.split("1 2 3 4 5", params).args.getCopyRawStrings()
        );
    }
}
