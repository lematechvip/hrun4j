package io.lematech.httprunner4j.utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;

public class MyMatcher<T extends Object> extends BaseMatcher<T> {

    private final T value;
    private final Boolean greater;
    public MyMatcher(T value,Boolean greater){
        this.value = value;
        this.greater = greater;
    }
    @Factory
    public static <T extends Number> MyMatcher<T> gt(T value) {
        return new MyMatcher<>(value, true);
    }

    @Factory
    public static <T extends Number> MyMatcher<T> lt(T value) {
        return new MyMatcher<>(value, false);
    }


    @Override
    public boolean matches(Object actual) {
        Class<?> clazz = actual.getClass();
        if (clazz == Integer.class) {
            return greater ? (Integer) actual > (Integer) value : (Integer) actual < (Integer) value;
        } else if (clazz == Long.class) {
            return greater ? (Long) actual > (Long) value : (Long) actual < (Long) value;
        } else if (clazz == Short.class) {
            return greater ? (Short) actual > (Short) value : (Short) actual < (Short) value;
        } else if (clazz == Double.class) {
            return greater ? (Double) actual > (Double) value : (Double) actual < (Double) value;
        } else if (clazz == Float.class) {
            return greater ? (Float) actual > (Float) value : (Float) actual > (Float) value;
        } else if (clazz == Byte.class) {
            return greater ? (Byte) actual > (Byte) value : (Byte) actual < (Byte) value;
        } else if (clazz == String.class){
            return actual.equals(value);
        }else {
            throw new AssertionError("The number type [" + clazz + "] not matched...");
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("compare value of two number");
    }

}
