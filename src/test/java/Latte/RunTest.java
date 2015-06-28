package Latte;

import org.junit.Test;

public class RunTest
{

    /**
     * Test main
     */
    //@Test
    public void main_String_PositiveTest() throws Exception {
        for(int i = 1; i <= 9; i++){
            Run.main(new String[]{"tests/good/core00"+i+".lat"});
        }
        for(int i = 10; i <= 22; i++){
            Run.main(new String[]{"tests/good/core0"+i+".lat"});
        }
    }

    @Test
    public void main_String_BadSynteax() throws Exception {

        System.err.println("\n====================\n");
        for(int i = 1; i <= 2; i++){
            Run.main(new String[]{"tests/bad/bad00"+i+".lat", "true"});
        }
//        for(int i = 10; i <= 13; i++){
//            Run.main(new String[]{"tests/bad/bad0"+i+".lat"});
//        }
//        for(int i = 15; i <= 27; i++){
//            Run.main(new String[]{"tests/bad/bad0"+i+".lat"});
//        }
    }


    @Test
    public void main_String_NegativeTest7() throws Exception {
        System.err.println("\n====================\n");
        for(int i = 6; i <= 7; i++){
            Run.main(new String[]{"tests/bad/bad00"+i+".lat", "true"});
        }
    }
}