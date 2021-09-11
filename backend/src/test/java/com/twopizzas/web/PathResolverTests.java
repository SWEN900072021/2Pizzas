package com.twopizzas.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class PathResolverTests {

    @Test
    @DisplayName("GIVEN path matches WHEN test THEN result isMatches is true")
    void test() {
        // GIVEN
        String path = "/test/test";
        PathResolver resolver = PathResolver.of(path);

        // WHEN
        PathResolver.PathResult result = resolver.test(path);

        // THEN
        Assertions.assertTrue(result.isMatch());
    }

    @Test
    @DisplayName("GIVEN path does not match WHEN test THEN result isMatches is false")
    void test2() {
        // GIVEN
        String path = "/test/test";
        PathResolver resolver = PathResolver.of(path);

        // WHEN
        PathResolver.PathResult result = resolver.test("/bad/test");

        // THEN
        Assertions.assertFalse(result.isMatch());
    }

    @Test
    @DisplayName("GIVEN path with variables matches WHEN test THEN result isMatches is true and variable values captured")
    void test3() {
        // GIVEN
        String path = "/test/{testId}";
        PathResolver resolver = PathResolver.of(path);

        // WHEN
        String testId = UUID.randomUUID().toString();
        PathResolver.PathResult result = resolver.test("/test/" + testId);

        // THEN
        Assertions.assertTrue(result.isMatch());
        Assertions.assertTrue(result.getPathVariable("testId").isPresent());
        Assertions.assertEquals(testId, result.getPathVariable("testId").get());
    }

    @Test
    @DisplayName("GIVEN path with variables does not match WHEN test THEN result isMatches is false and variable values captured")
    void test4() {
        // GIVEN
        String path = "/test/{testId}";
        PathResolver resolver = PathResolver.of(path);

        // WHEN
        String testId = UUID.randomUUID().toString();
        PathResolver.PathResult result = resolver.test("/bad/" + testId);

        // THEN
        Assertions.assertFalse(result.isMatch());
        Assertions.assertFalse(result.getPathVariable("testId").isPresent());
    }
}
