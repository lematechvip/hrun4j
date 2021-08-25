package vip.lematech.hrun4j.parse;

import org.testng.Assert;
import org.testng.annotations.Test;
import vip.lematech.hrun4j.cli.commands.Postman2Case;

import java.io.File;

/**
 * website https://www.lematech.vip/
 * @author chenfanghang
 * @version 1.0.1
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
