package com.mflintoff.calculator.function;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author Malcolm Flintoff
 */
public class DivideFunctionTest {
    @Test
    public void testDivide() throws FunctionExecutionException {
        String result = new DivideFunction().execute(Arrays.asList("20", "2", "5"));
        assertEquals("Division had unexpected result", "2", result);
    }

    @Test
    public void testDivideDecimals() throws FunctionExecutionException {
        String result = new DivideFunction().execute(Arrays.asList("10.4", "4"));
        assertEquals("Division with decimal values had unexpected result", "2.6", result);
    }

    @Test
    public void testDivideNegativeValue() throws FunctionExecutionException {
        String result = new DivideFunction().execute(Arrays.asList("5", "-10"));
        assertEquals("Division of negative value had unexpected result", "-0.5", result);
    }

    @Test(expected = FunctionExecutionException.class)
    public void testDivideByZero() throws FunctionExecutionException {
        new DivideFunction().execute(Arrays.asList("10", "0"));
    }

    @Test
    public void testRepeatingDecimals() throws FunctionExecutionException {
        // we expect 8 decimal points of precision in this case
        String result = new DivideFunction().execute(Arrays.asList("100", "3"));
        assertEquals("Division with repeating decimals had unexpected result", "33.33333333", result);
    }

    @Test(expected = FunctionExecutionException.class)
    public void testDivideInvalidArguments() throws FunctionExecutionException {
        new DivideFunction().execute(Arrays.asList("3", "a"));
    }

}
