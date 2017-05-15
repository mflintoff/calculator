package com.mflintoff.calculator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Malcolm Flintoff
 */
public class MainTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    private static final String USAGE_INSTRUCTIONS_STARTS_WITH = "usage: calculator";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");


    @Test
    public void testHelp() {
        String[] args = {"-h"};
        Main.main(args);
        assertTrue(systemOutRule.getLog().startsWith(USAGE_INSTRUCTIONS_STARTS_WITH));
    }

    @Test
    public void testShortParameters() {
        String[] args = {"-l", "ERROR", "-e", "add(2,2)"};
        Main.main(args);
        systemOutRule.enableLog();
        assertEquals("4" + LINE_SEPARATOR, systemOutRule.getLog());
    }

    @Test
    public void testLongParameters() {
        String[] args = {"-loglevel", "ERROR", "-expression", "div(10,2)"};
        Main.main(args);
        assertEquals("5" + LINE_SEPARATOR, systemOutRule.getLog());
    }

    @Test
    public void testInvalidLogLevel() {
        String[] args = {"-loglevel", "ERR", "-expression", "add(5,5)"};
        Main.main(args);
        String expectedOutputStartsWith = "invalid log level: ERR" + LINE_SEPARATOR + USAGE_INSTRUCTIONS_STARTS_WITH;
        assertTrue(systemOutRule.getLog().startsWith(expectedOutputStartsWith));
    }

    @Test
    public void testMissingExpression() {
        String[] args = {"-loglevel", "ERROR"};
        Main.main(args);
        String expectedOutputStartsWith = "Missing required option: expression" + LINE_SEPARATOR + USAGE_INSTRUCTIONS_STARTS_WITH;
        assertTrue(systemOutRule.getLog().startsWith(expectedOutputStartsWith));
    }
}
