package Latte;

import org.junit.Test;

public class RunTest
{




    @Test
    public void main_String_SingleTest() throws Exception {

        System.err.println("\n====================\n");
        Run.main(new String[]{"tests/good/core012.lat", "true"});
    }

    @Test
    public void main_String_NegativeTest() throws Exception {


        System.err.println("\n====================\n");
        for(int i = 1; i <= 27; i++){
            if (i == 14) continue;
            Run.main(new String[]{"tests/bad/bad0" + String.format("%02d", i) + ".lat", "true"});
        }
        for(int i = 1; i <= 22; i++){
            System.err.print(i+": ");
            Run.main(new String[]{"tests/good/core0" + String.format("%02d", i) + ".lat", "true"});
        }
    }
}