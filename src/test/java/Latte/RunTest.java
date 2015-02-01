package Latte;

import org.junit.Test;

public class RunTest
{

    /**
     * Test main
     */
    @Test
    public void main_String_PositiveTest() throws Exception {
        Run.main(new String[]{"tests/good/core011.lat"});
    }
}