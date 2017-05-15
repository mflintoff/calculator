package com.mflintoff.calculator;

import com.mflintoff.calculator.function.AddFunction;
import com.mflintoff.calculator.function.DivideFunction;
import com.mflintoff.calculator.function.Function;
import com.mflintoff.calculator.function.FunctionExecutionException;
import com.mflintoff.calculator.function.LetFunction;
import com.mflintoff.calculator.function.MultiplyFunction;
import com.mflintoff.calculator.function.SubtractFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Evaluates expressions based on registered functions. The following expressions are registered by default: add, sub,
 * mult, div and let. For a description of each function, see the javadoc in the associated function classes. Additional
 * functions can be registered via the {@link #registerFunction(Function)} method.</p>
 *
 * <p>An example of an expression that uses add and mult functions: "add(2, mult(3, 5))"</p>
 *
 * @author Malcolm Flintoff
 */
public class ExpressionEvaluator {
    private static final Logger log = LoggerFactory.getLogger(ExpressionEvaluator.class);

    private final Map<String, Function> functions = new HashMap<String, Function>();

    /**
     * Constructs an ExpressionEvaluator with default functions registered.
     */
    public ExpressionEvaluator() {
        registerFunction(new AddFunction());
        registerFunction(new SubtractFunction());
        registerFunction(new DivideFunction());
        registerFunction(new MultiplyFunction());
        registerFunction(new LetFunction());
    }

    /**
     * Registers a function, used to evaluate expressions.
     *
     * @param function the function to register.
     */
    public void registerFunction(Function function) {
        functions.put(function.getName().toLowerCase(), function);
    }

    /**
     * Evaluates an expression. Any sub-expressions will be evaluated recursively, and a final result is returned.
     *
     * @param expression expression to evaluate. eg "2, add(3, 5)".
     * @return the result of the expression.
     * @throws EvaluationException if an error occurs during the evaluation.
     */
    public String evaluate(String expression) throws EvaluationException {
        log.info("evaluating expression: {}", expression);

        String trimmedExpression = expression.replaceAll("\\s","");
        String value = evaluate(trimmedExpression, Collections.<String,String> emptyMap());
        log.info("{}: expression evaluates to final result of {}", expression, value);
        return value;
    }

    /**
     * Evaluates an expression with variable substitution. Any sub-expressions will be evaluated recursively, and a
     * final result is returned.
     *
     * @param expression expression to evaluate. eg "2, add(3, 5)".
     * @param variables the variables to substitute into the expression.
     * @return the result of the expression.
     * @throws EvaluationException if an error occurs during the evaluation.
     */
    private String evaluate(String expression, Map<String, String> variables) throws EvaluationException {
        log.debug("evaluating expression {} with variables {}", expression, variables);
        if (!expression.contains("(")) {
            // no bracket, so treat expression as a raw value as opposed to a function
            String value = variables.containsKey(expression) ? variables.get(expression) : expression;
            log.debug("{}: expression is a single value which evaluates to {}", expression, value);
            return value;
        }

        // expression is a function
        int bracketIndex = expression.indexOf("(");
        String functionName = expression.substring(0, bracketIndex).toLowerCase();
        log.debug("{}: expression represents function '{}'", expression, functionName);

        if (expression.charAt(expression.length() - 1) != ')') {
            throw new EvaluationException(expression + ": missing end bracket for function: " + functionName);
        }

        // parse function arguments as raw string - we don't execute nested functions at this stage
        String argumentsString = expression.substring(bracketIndex + 1, expression.length() - 1);
        List<String> arguments = parseFunctionArguments(argumentsString);
        log.debug("{}: parsed the following function arguments: {}", expression, arguments);

        Function function = functions.get(functionName);
        if (function == null) {
            throw new EvaluationException(expression + ": unrecognized function: " + functionName);
        }

        validateFunctionArguments(function, arguments);
        log.debug("{}: confirmed that a valid number of arguments were provided", expression);

        // evaluate the arguments - "let" is a special case since it registers an additional variable
        log.debug("{}: evaluating arguments", expression);
        List<String> evaluatedArguments = new ArrayList<String>(arguments.size());
        if (function instanceof LetFunction) {
            String variableName = arguments.get(0);

            // recursively evaluate the second argument using existing variables
            String variableValue = evaluate(arguments.get(1), variables);
            log.debug("{}: variable {} evaluates to {}", expression, variableName, variableValue);

            // evaluate the third argument - the expression - using newly calculated variables along with existing ones
            // any sub-expressions of the third argument will also have access to the new variable
            Map<String, String> newVariables = new HashMap<String, String>();
            newVariables.putAll(variables);
            newVariables.put(variableName, variableValue);
            String letExpression = evaluate(arguments.get(2), newVariables);

            evaluatedArguments.add(variableName);
            evaluatedArguments.add(variableValue);
            evaluatedArguments.add(letExpression);

        } else {
            for (String argument : arguments) {
                // recursively evaluate arguments using existing variables
                evaluatedArguments.add(evaluate(argument, variables));
            }
        }

        // finally, execute the function with evaluated arguments and return the result
        log.debug("{}: executing function {} with evaluated arguments {}", expression, functionName, evaluatedArguments);
        String value = null;
        try {
            value = function.execute(evaluatedArguments);
        } catch (FunctionExecutionException e) {
            throw new EvaluationException("an error occurred executing function " + functionName + ":" + e.getMessage(), e);
        }
        log.debug("{}: expression evaluates to {}", expression, value);
        return value;
    }

    /**
     * Parse the function arguments. These could be raw values or functions themselves, but we parse them as raw
     * strings -- we don't evaluate them at this point.
     **
     * @param argumentsString everything within the brackets of a function call.
     * @return list of arguments.
     */
    private List<String> parseFunctionArguments(String argumentsString) {
        int nestedBracketDepth = 0;
        int charIndex = 0;
        int argSplitStartIndex = 0;
        List<String> arguments = new ArrayList<String>();
        while (++charIndex < argumentsString.length()) {
            char c = argumentsString.charAt(charIndex);
            if (c == '(') {
                nestedBracketDepth++;
            } else if (c == ')') {
                nestedBracketDepth--;
            } else if (c == ',' && nestedBracketDepth == 0){
                // top level comma (not nested in brackets)
                arguments.add(argumentsString.substring(argSplitStartIndex, charIndex));
                argSplitStartIndex = charIndex + 1;
            }
        }
        // parse the remaining argument, after the last comma
        arguments.add(argumentsString.substring(argSplitStartIndex, argumentsString.length()));
        return arguments;
    }

    /**
     * Ensures that the function is provided with a valid number of arguments. If unsupported number of arguments are
     * provided, an EvaluationException is thrown.
     *
     * @param function the Function to validate.
     * @param arguments the arguments that will be passed to the function.
     * @throws EvaluationException if an invalid number of arguments are provided to the function.
     */
    private void validateFunctionArguments(Function function, List<String> arguments) throws EvaluationException {
        int numArgs = arguments.size();
        int minNumArgsSupported = function.getMinNumberOfArgsSupported();
        int maxNumArgsSupported = function.getMaxNumberOfArgsSupported();
        if (numArgs < minNumArgsSupported) {
            throw new EvaluationException("Function " + function.getName() + " requires at least " + minNumArgsSupported + " arguments but only " + numArgs + " provided");
        } else if (numArgs > maxNumArgsSupported) {
            throw new EvaluationException("Function " + function.getName() + " supports at most " + maxNumArgsSupported + " arguments but " + numArgs + " provided");
        }
    }

}
