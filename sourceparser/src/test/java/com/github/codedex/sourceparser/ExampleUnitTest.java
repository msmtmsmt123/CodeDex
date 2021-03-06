package com.github.codedex.sourceparser;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void checkStringArray() {
        final String[] testArray = "test".split("\\.");
        assertEquals("test", testArray[0]);
        assertEquals(testArray.length, 1);
    }
}