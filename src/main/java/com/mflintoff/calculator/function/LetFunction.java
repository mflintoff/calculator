package com.mflintoff.calculator.function;

import java.util.List;

/**
 * <p>A “let” operator for assigning values to variables:
 * <i>let(&lt;variable name&gt;,&lt;value expression&gt;,&lt;expression where variable is used&gt;)</i></p>
 * As with arithmetic functions, the value expression and the expression where the variable is used may be an arbitrary
 * expression. 
 *
 * @author Malcolm Flintoff.
 */
public class LetFunction implements Function {

    public String getName() {
        return "let";
    }

    public String execute(List<String> arguments) throws FunctionExecutionException {
        return arguments.get(2);
    }

    public int getMinNumberOfArgsSupported() {
        return 3;
    }

    public int getMaxNumberOfArgsSupported() {
        return 3;
    }

}
