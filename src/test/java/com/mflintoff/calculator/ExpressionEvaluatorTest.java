package com.mflintoff.calculator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Malcolm Flintoff.
 */
public class ExpressionEvaluatorTest {

    @Test
    public void testAddition() throws EvaluationException {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        String result = evaluator.evaluate("add(add(3,2),add(5,10))");
        assertEquals("addition had unexpected result", "20", result);
    }

    @Test
    public void testMultiplication() throws EvaluationException {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        String result = evaluator.evaluate("mult(mult(3,3),mult(5,2))");
        assertEquals("multiplication had unexpected result", "90", result);
    }

    @Test
    public void testDivision() throws EvaluationException {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        String result = evaluator.evaluate("div(div(100,5),div(10,2))");
        assertEquals("division had unexpected result", "4", result);
    }

    @Test
    public void testSubtraction() throws EvaluationException {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        String result = evaluator.evaluate("sub(sub(10,2),sub(4,3))");
        assertEquals("subtraction had unexpected result", "7", result);
    }

    @Test
    public void testExtraWhitespace() throws EvaluationException {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        String result = evaluator.evaluate("     add( 3,    add( 5, 5)  )   ");
        assertEquals("evaluation with extra whitespace had unexpected result", "13", result);
    }

    @Test
    public void testCaseSensitivity() throws EvaluationException {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        String result = evaluator.evaluate("sub(10,SUB(3,2))");
        assertEquals("evaluation with mixed upper and lower case had unexpected result", "9", result);
    }

    @Test(expected = EvaluationException.class)
    public void testNotEnoughArguments() throws EvaluationException {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.evaluate("add(2)");
    }

    @Test(expected = EvaluationException.class)
    public void testTooManyArguments() throws EvaluationException {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.evaluate("let(a, 3, 5, add(a, 5)");
    }

    @Test(expected = EvaluationException.class)
    public void testUnknownFunction() throws EvaluationException {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.evaluate("add(3, unknown(a, 5)");
    }

    @Test(expected = EvaluationException.class)
    public void testMissingBrackets() throws EvaluationException {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.evaluate("add(3, add(3,3");
    }

    @Test
    public void testDecimals() throws EvaluationException {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        String result = evaluator.evaluate("add(3.5, sub(2.2, 3.21))");
        assertEquals("evaluation of non integers had unexpected result", "2.49", result);
    }

    @Test
    public void testMixedFunctions() throws EvaluationException {
        String result;
        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        result = evaluator.evaluate("let(a, 5, let(b, mult(a, 10), add(b, a)))");
        assertEquals("evaluation had unexpected result", "55", result);

        result = evaluator.evaluate("let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b)))");
        assertEquals("evaluation had unexpected result", "40", result);
    }

}
