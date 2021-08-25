package vip.lematech.hrun4j.model.postman;

/**
 * website https://www.lematech.vip/
 * @author chenfanghang
 * @version 1.0.1
 */
public enum PostmanRequestBodyModeEnum {

    RAW("raw"), FORM_DATA("formdata"), URLENCODED("urlencoded"), FILE("file");

    private String value;

    PostmanRequestBodyModeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
