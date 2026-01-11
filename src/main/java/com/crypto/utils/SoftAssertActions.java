package com.crypto.utils;

import java.util.Collection;
import java.util.Map;

import org.testng.asserts.SoftAssert;

public class SoftAssertActions extends SoftAssert {
    private final SoftAssert softAssert;

    public SoftAssertActions() {
        this.softAssert = new SoftAssert();
    }

    public SoftAssert getSoftAssert() {
        return softAssert;
    }

    public void assertAll() {
        softAssert.assertAll();
    }

    // ========== Basic Assertions ==========

    public void assertEquals(Object actual, Object expected, String message) {
        softAssert.assertEquals(actual, expected, message);
    }

    public void assertEquals(Object actual, Object expected) {
        softAssert.assertEquals(actual, expected);
    }

    public void assertNotEquals(Object actual, Object expected, String message) {
        softAssert.assertNotEquals(actual, expected, message);
    }

    public void assertNotEquals(Object actual, Object expected) {
        softAssert.assertNotEquals(actual, expected);
    }

    public void assertTrue(boolean condition, String message) {
        softAssert.assertTrue(condition, message);
    }

    public void assertTrue(boolean condition) {
        softAssert.assertTrue(condition);
    }

    public void assertFalse(boolean condition, String message) {
        softAssert.assertFalse(condition, message);
    }

    public void assertFalse(boolean condition) {
        softAssert.assertFalse(condition);
    }

    // ========== Null Assertions ==========

    public void assertNull(Object object, String message) {
        softAssert.assertNull(object, message);
    }

    public void assertNull(Object object) {
        softAssert.assertNull(object);
    }

    public void assertNotNull(Object object, String message) {
        softAssert.assertNotNull(object, message);
    }

    public void assertNotNull(Object object) {
        softAssert.assertNotNull(object);
    }

    // ========== Same/NotSame Assertions ==========

    public void assertSame(Object actual, Object expected, String message) {
        softAssert.assertSame(actual, expected, message);
    }

    public void assertSame(Object actual, Object expected) {
        softAssert.assertSame(actual, expected);
    }

    public void assertNotSame(Object actual, Object expected, String message) {
        softAssert.assertNotSame(actual, expected, message);
    }

    public void assertNotSame(Object actual, Object expected) {
        softAssert.assertNotSame(actual, expected);
    }

    // ========== String Assertions ==========

    public void assertContains(String actual, String expected, String message) {
        softAssert.assertTrue(actual != null && actual.contains(expected),
                message != null ? message : String.format("Expected '%s' to contain '%s'", actual, expected));
    }

    public void assertContains(String actual, String expected) {
        assertContains(actual, expected, null);
    }

    public void assertNotContains(String actual, String expected, String message) {
        softAssert.assertTrue(actual == null || !actual.contains(expected),
                message != null ? message : String.format("Expected '%s' to not contain '%s'", actual, expected));
    }

    public void assertNotContains(String actual, String expected) {
        assertNotContains(actual, expected, null);
    }

    public void assertStartsWith(String actual, String prefix, String message) {
        softAssert.assertTrue(actual != null && actual.startsWith(prefix),
                message != null ? message : String.format("Expected '%s' to start with '%s'", actual, prefix));
    }

    public void assertStartsWith(String actual, String prefix) {
        assertStartsWith(actual, prefix, null);
    }

    public void assertEndsWith(String actual, String suffix, String message) {
        softAssert.assertTrue(actual != null && actual.endsWith(suffix),
                message != null ? message : String.format("Expected '%s' to end with '%s'", actual, suffix));
    }

    public void assertEndsWith(String actual, String suffix) {
        assertEndsWith(actual, suffix, null);
    }

    public void assertMatches(String actual, String regex, String message) {
        softAssert.assertTrue(actual != null && actual.matches(regex),
                message != null ? message : String.format("Expected '%s' to match pattern '%s'", actual, regex));
    }

    public void assertMatches(String actual, String regex) {
        assertMatches(actual, regex, null);
    }

    public void assertNotMatches(String actual, String regex, String message) {
        softAssert.assertTrue(actual == null || !actual.matches(regex),
                message != null ? message : String.format("Expected '%s' to not match pattern '%s'", actual, regex));
    }

    public void assertNotMatches(String actual, String regex) {
        assertNotMatches(actual, regex, null);
    }

    public void assertEqualsIgnoreCase(String actual, String expected, String message) {
        softAssert.assertTrue(actual != null && actual.equalsIgnoreCase(expected),
                message != null ? message
                        : String.format("Expected '%s' to equal (ignore case) '%s'", actual, expected));
    }

    public void assertEqualsIgnoreCase(String actual, String expected) {
        assertEqualsIgnoreCase(actual, expected, null);
    }

    // ========== Collection Assertions ==========

    public void assertEmpty(Collection<?> collection, String message) {
        softAssert.assertTrue(collection != null && collection.isEmpty(),
                message != null ? message : "Expected collection to be empty");
    }

    public void assertEmpty(Collection<?> collection) {
        assertEmpty(collection, null);
    }

    public void assertNotEmpty(Collection<?> collection, String message) {
        softAssert.assertTrue(collection != null && !collection.isEmpty(),
                message != null ? message : "Expected collection to not be empty");
    }

    public void assertNotEmpty(Collection<?> collection) {
        assertNotEmpty(collection, null);
    }

    public void assertContains(Collection<?> collection, Object item, String message) {
        softAssert.assertTrue(collection != null && collection.contains(item),
                message != null ? message : String.format("Expected collection to contain '%s'", item));
    }

    public void assertContains(Collection<?> collection, Object item) {
        assertContains(collection, item, null);
    }

    public void assertNotContains(Collection<?> collection, Object item, String message) {
        softAssert.assertTrue(collection == null || !collection.contains(item),
                message != null ? message : String.format("Expected collection to not contain '%s'", item));
    }

    public void assertNotContains(Collection<?> collection, Object item) {
        assertNotContains(collection, item, null);
    }

    public void assertSize(Collection<?> collection, int expectedSize, String message) {
        softAssert.assertEquals(collection != null ? collection.size() : 0, expectedSize,
                message != null ? message : String.format("Expected collection size to be %d", expectedSize));
    }

    public void assertSize(Collection<?> collection, int expectedSize) {
        assertSize(collection, expectedSize, null);
    }

    // ========== Array Assertions ==========

    public void assertArrayEquals(Object[] actual, Object[] expected, String message) {
        softAssert.assertEquals(actual, expected, message);
    }

    public void assertArrayEquals(Object[] actual, Object[] expected) {
        assertArrayEquals(actual, expected, null);
    }

    public void assertArrayEquals(byte[] actual, byte[] expected, String message) {
        softAssert.assertEquals(actual, expected, message);
    }

    public void assertArrayEquals(byte[] actual, byte[] expected) {
        assertArrayEquals(actual, expected, null);
    }

    public void assertArrayEquals(short[] actual, short[] expected, String message) {
        softAssert.assertEquals(actual, expected, message);
    }

    public void assertArrayEquals(short[] actual, short[] expected) {
        assertArrayEquals(actual, expected, null);
    }

    public void assertArrayEquals(int[] actual, int[] expected, String message) {
        softAssert.assertEquals(actual, expected, message);
    }

    public void assertArrayEquals(int[] actual, int[] expected) {
        assertArrayEquals(actual, expected, null);
    }

    public void assertArrayEquals(long[] actual, long[] expected, String message) {
        softAssert.assertEquals(actual, expected, message);
    }

    public void assertArrayEquals(long[] actual, long[] expected) {
        assertArrayEquals(actual, expected, null);
    }

    public void assertArrayEquals(float[] actual, float[] expected, String message) {
        softAssert.assertEquals(actual, expected, message);
    }

    public void assertArrayEquals(float[] actual, float[] expected) {
        assertArrayEquals(actual, expected, null);
    }

    public void assertArrayEquals(double[] actual, double[] expected, String message) {
        softAssert.assertEquals(actual, expected, message);
    }

    public void assertArrayEquals(double[] actual, double[] expected) {
        assertArrayEquals(actual, expected, null);
    }

    public void assertArrayEquals(char[] actual, char[] expected, String message) {
        softAssert.assertEquals(actual, expected, message);
    }

    public void assertArrayEquals(char[] actual, char[] expected) {
        assertArrayEquals(actual, expected, null);
    }

    public void assertArrayEquals(boolean[] actual, boolean[] expected, String message) {
        softAssert.assertEquals(actual, expected, message);
    }

    public void assertArrayEquals(boolean[] actual, boolean[] expected) {
        assertArrayEquals(actual, expected, null);
    }

    // ========== Numeric Assertions ==========

    public void assertGreaterThan(int actual, int expected, String message) {
        softAssert.assertTrue(actual > expected,
                message != null ? message : String.format("Expected %d to be greater than %d", actual, expected));
    }

    public void assertGreaterThan(int actual, int expected) {
        assertGreaterThan(actual, expected, null);
    }

    public void assertGreaterThanOrEqual(int actual, int expected, String message) {
        softAssert.assertTrue(actual >= expected,
                message != null ? message
                        : String.format("Expected %d to be greater than or equal to %d", actual, expected));
    }

    public void assertGreaterThanOrEqual(int actual, int expected) {
        assertGreaterThanOrEqual(actual, expected, null);
    }

    public void assertLessThan(int actual, int expected, String message) {
        softAssert.assertTrue(actual < expected,
                message != null ? message : String.format("Expected %d to be less than %d", actual, expected));
    }

    public void assertLessThan(int actual, int expected) {
        assertLessThan(actual, expected, null);
    }

    public void assertLessThanOrEqual(int actual, int expected, String message) {
        softAssert.assertTrue(actual <= expected,
                message != null ? message
                        : String.format("Expected %d to be less than or equal to %d", actual, expected));
    }

    public void assertLessThanOrEqual(int actual, int expected) {
        assertLessThanOrEqual(actual, expected, null);
    }

    public void assertGreaterThan(double actual, double expected, String message) {
        softAssert.assertTrue(actual > expected,
                message != null ? message : String.format("Expected %f to be greater than %f", actual, expected));
    }

    public void assertGreaterThan(double actual, double expected) {
        assertGreaterThan(actual, expected, null);
    }

    public void assertGreaterThanOrEqual(double actual, double expected, String message) {
        softAssert.assertTrue(actual >= expected,
                message != null ? message
                        : String.format("Expected %f to be greater than or equal to %f", actual, expected));
    }

    public void assertGreaterThanOrEqual(double actual, double expected) {
        assertGreaterThanOrEqual(actual, expected, null);
    }

    public void assertLessThan(double actual, double expected, String message) {
        softAssert.assertTrue(actual < expected,
                message != null ? message : String.format("Expected %f to be less than %f", actual, expected));
    }

    public void assertLessThan(double actual, double expected) {
        assertLessThan(actual, expected, null);
    }

    public void assertLessThanOrEqual(double actual, double expected, String message) {
        softAssert.assertTrue(actual <= expected,
                message != null ? message
                        : String.format("Expected %f to be less than or equal to %f", actual, expected));
    }

    public void assertLessThanOrEqual(double actual, double expected) {
        assertLessThanOrEqual(actual, expected, null);
    }

    // ========== Map Assertions ==========

    public void assertMapContainsKey(Map<?, ?> map, Object key, String message) {
        softAssert.assertTrue(map != null && map.containsKey(key),
                message != null ? message : String.format("Expected map to contain key '%s'", key));
    }

    public void assertMapContainsKey(Map<?, ?> map, Object key) {
        assertMapContainsKey(map, key, null);
    }

    public void assertMapContainsValue(Map<?, ?> map, Object value, String message) {
        softAssert.assertTrue(map != null && map.containsValue(value),
                message != null ? message : String.format("Expected map to contain value '%s'", value));
    }

    public void assertMapContainsValue(Map<?, ?> map, Object value) {
        assertMapContainsValue(map, value, null);
    }

    public void assertMapSize(Map<?, ?> map, int expectedSize, String message) {
        softAssert.assertEquals(map != null ? map.size() : 0, expectedSize,
                message != null ? message : String.format("Expected map size to be %d", expectedSize));
    }

    public void assertMapSize(Map<?, ?> map, int expectedSize) {
        assertMapSize(map, expectedSize, null);
    }

    // Remove the redundant assertAll method with Map parameter
}