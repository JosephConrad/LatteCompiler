package Latte;

import org.junit.Test;

public class RunTest
{

    /**
     * Test main
     */
    @Test
    public void main_String_PositiveTest() throws Exception {
        Run.main(new String[]{"tests/good/core002.lat"});
        Run.main(new String[]{"tests/good/core003.lat"});
        Run.main(new String[]{"tests/good/core004.lat"});
        Run.main(new String[]{"tests/good/core005.lat"});
        Run.main(new String[]{"tests/good/core006.lat"});
        Run.main(new String[]{"tests/good/core007.lat"});
        Run.main(new String[]{"tests/good/core008.lat"});
        Run.main(new String[]{"tests/good/core009.lat"});
        Run.main(new String[]{"tests/good/core011.lat"});
        Run.main(new String[]{"tests/good/core014.lat"});
        Run.main(new String[]{"tests/good/core016.lat"});
        Run.main(new String[]{"tests/good/core019.lat"});
        Run.main(new String[]{"tests/good/core020.lat"});
        Run.main(new String[]{"tests/good/core021.lat"});
        Run.main(new String[]{"tests/good/core022.lat"});
    }
}