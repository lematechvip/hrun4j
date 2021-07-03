package vip.lematech.httprunner4j.parse;

import org.testng.Assert;
import org.testng.annotations.Test;
import vip.lematech.httprunner4j.cli.commands.Swagger2Api;

/**
 * website http://lematech.vip/
 * @author chenfanghang
 * @version 1.0.0
 */
public class Swagger2ApiTest {

    @Test
    public void testSwagger2Api() {
        Swagger2Api postman2Case = new Swagger2Api();
        String url = "https://petstore.swagger.io/v2/swagger.json";
        String target = "src/test/resources/swagger";
        int code = postman2Case.parse(url, target);
        Assert.assertEquals(code, 0);
    }
}
