package com.mflintoff.calculator.function;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author Malcolm Flintoff
 */
public class MultiplyFunctionTest {
    @Test
    public void testMultiply() throws FunctionExecutionException {
        String result = new MultiplyFunction().execute(Arrays.asList("5", "3", "2"));
        assertEquals("Multiplication had unexpected result", "30", result);
    }

    @Test
    public void testMultiplyDecimals() throws FunctionExecutionException {
        String result = new MultiplyFunction().execute(Arrays.asList("5.335", "3.1"));
        assertEquals("Multiplication with decimal values had unexpected result", "16.5385", result);
    }

    @Test
    public void testMultiplyNegativeValue() throws FunctionExecutionException {
        String result = new MultiplyFunction().execute(Arrays.asList("5", "-10"));
        assertEquals("Multiplication of negative value had unexpected result", "-50", result);
    }

    @Test(expected = FunctionExecutionException.class)
    public void testMultiplyInvalidArguments() throws FunctionExecutionException {
        new MultiplyFunction().execute(Arrays.asList("3", "a"));
    }

}
