package com.mflintoff.calculator.function;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author Malcolm Flintoff
 */
public class LetFunctionTest {

    // not much to test here -- most of the interesting functionality of let occurs in ExpressionEvaluator class
    @Test
    public void testLet() throws FunctionExecutionException {
        String result = new LetFunction().execute(Arrays.asList("3", "5", "7"));
        assertEquals("let function had unexpected result", "7", result);
    }

}
