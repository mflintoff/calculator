package com.mflintoff.calculator.function;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author Malcolm Flintoff
 */
public class SubtractFunctionTest {
    @Test
    public void testSubtraction() throws FunctionExecutionException {
        String result = new SubtractFunction().execute(Arrays.asList("5", "3", "2"));
        assertEquals("subtraction had unexpected result", "0", result);
    }

    @Test
    public void testSubtractDecimals() throws FunctionExecutionException {
        String result = new SubtractFunction().execute(Arrays.asList("5.335", "3.1"));
        assertEquals("subtraction with decimal values had unexpected result", "2.235", result);
    }

    @Test
    public void testSubtractNegativeValue() throws FunctionExecutionException {
        String result = new SubtractFunction().execute(Arrays.asList("5", "-10"));
        assertEquals("subtraction of negative value had unexpected result", "15", result);
    }

    @Test(expected = FunctionExecutionException.class)
    public void testSubtractInvalidArguments() throws FunctionExecutionException {
        new SubtractFunction().execute(Arrays.asList("3", "a"));
    }

}
