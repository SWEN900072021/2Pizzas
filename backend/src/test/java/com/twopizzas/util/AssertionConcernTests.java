package com.twopizzas.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

public class AssertionConcernTests {

    @Test
    @DisplayName("GIVEN value is null WHEN notNull invoked THEN throws")
    void test() {
        // WHEN + THEN
        Assertions.assertThrows(ValueViolation.class, () -> new StubAssertionConcern().callNotNull(null));
    }

    @Test
    @DisplayName("GIVEN value is not null WHEN notNull invoked THEN returns value")
    void test2() {
        // GIVEN
        Object value = new Object();

        // WHEN
        Object notNull = new StubAssertionConcern().callNotNull(value);

        // THEN
        Assertions.assertEquals(value, notNull);
    }

    @Test
    @DisplayName("GIVEN value is blank WHEN notBlank invoked THEN throws")
    void test3() {
        // WHEN + THEN
        Assertions.assertThrows(ValueViolation.class, () -> new StubAssertionConcern().callNotBlank(" "));
    }

    @Test
    @DisplayName("GIVEN value is not blank WHEN notBlank invoked THEN returns value")
    void test4() {
        // GIVEN
        String value = " not blank";

        // WHEN
        String notBlank = new StubAssertionConcern().callNotBlank(value);

        // THEN
        Assertions.assertEquals(value, notBlank);
    }

    @Test
    @DisplayName("GIVEN value is null WHEN notBlank invoked THEN returns value")
    void test5() {
        // GIVEN
        String value = null;

        // WHEN
        String notBlank = new StubAssertionConcern().callNotBlank(value);

        // THEN
        Assertions.assertEquals(value, notBlank);
    }

    @Test
    @DisplayName("GIVEN value is empty WHEN notEmpty invoked THEN throws")
    void test6() {
        // WHEN + THEN
        Assertions.assertThrows(ValueViolation.class, () -> new StubAssertionConcern().callNotEmpty(Collections.emptyList()));
    }

    @Test
    @DisplayName("GIVEN value is not empty WHEN notEmpty invoked THEN returns value")
    void test7() {
        // GIVEN
        Collection<String> value = Collections.singletonList("not blank");

        // WHEN
        Collection<String> notEmpty = new StubAssertionConcern().callNotEmpty(value);

        // THEN
        Assertions.assertEquals(value, notEmpty);
    }

    @Test
    @DisplayName("GIVEN value is null WHEN notBlank invoked THEN returns value")
    void test8() {
        // GIVEN
        Collection<String> value = null;

        // WHEN
        Collection<String> notEmpty = new StubAssertionConcern().callNotEmpty(value);

        // THEN
        Assertions.assertEquals(value, notEmpty);
    }

    private static class StubAssertionConcern extends AssertionConcern {
        <T> T callNotNull(T value) {
            return notNull(value, "value");
        }

        String callNotBlank(String value) {
            return notBlank(value, "value");
        }

        <U, T extends Collection<U>> T callNotEmpty(T value) {
            return notEmpty(value, "value");
        }
    }
}
