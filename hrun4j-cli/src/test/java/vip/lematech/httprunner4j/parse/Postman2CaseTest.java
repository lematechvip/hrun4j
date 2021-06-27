package vip.lematech.httprunner4j.parse;

import org.testng.Assert;
import org.testng.annotations.Test;
import vip.lematech.httprunner4j.cli.commands.Postman2Case;

import java.io.File;

/**
 * website http://lematech.vip/
 * @author chenfanghang
 * @version 1.0.0
 */
public class Postman2CaseTest {

    @Test
    public void testPostman2Case() {
        Postman2Case postman2Case = new Postman2Case();
        File file = new File("src/test/resources/postman/postman_collection.json");
        String target = "src/test/resources/postman";
        int code = postman2Case.parse(file, target);
        Assert.assertEquals(code, 0);
    }
}
