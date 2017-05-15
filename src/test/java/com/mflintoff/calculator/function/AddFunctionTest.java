package com.mflintoff.calculator.function;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author Malcolm Flintoff
 */
public class AddFunctionTest {

    @Test
    public void testAddition() throws FunctionExecutionException {
        String result = new AddFunction().execute(Arrays.asList("5", "3", "2"));
        assertEquals("addition had unexpected result", "10", result);
    }

    @Test
    public void testAddDecimals() throws FunctionExecutionException {
        String result = new AddFunction().execute(Arrays.asList("5.335", "3.1"));
        assertEquals("addition with decimal values had unexpected result", "8.435", result);
    }

    @Test
    public void testAddNegativeValue() throws FunctionExecutionException {
        String result = new AddFunction().execute(Arrays.asList("5", "-10"));
        assertEquals("addition of negative value had unexpected result", "-5", result);
    }

    @Test(expected = FunctionExecutionException.class)
    public void testAddInvalidArguments() throws FunctionExecutionException {
        new AddFunction().execute(Arrays.asList("3", "a"));
    }
}
