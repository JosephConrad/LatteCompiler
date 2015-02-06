package Latte;

import org.junit.Test;

public class RunTest
{

    /**
     * Test main
     */
    @Test
    public void main_String_PositiveTest() throws Exception {
        for(int i = 1; i <= 9; i++){
            Run.main(new String[]{"tests/good/core00"+i+".lat"});
        }
        for(int i = 10; i <= 22; i++){
            Run.main(new String[]{"tests/good/core0"+i+".lat"});
        }
    }
}