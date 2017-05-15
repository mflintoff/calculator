package com.mflintoff.calculator.function;

import java.math.BigDecimal;
import java.util.List;

/**
 * A function that subtracts a series of numbers.
 *
 * @author Malcolm Flintoff.
 */
public class SubtractFunction implements Function {

    public String getName() {
        return "sub";
    }

    public String execute(List<String> arguments) throws FunctionExecutionException {
        BigDecimal result = null;
        for (String argument : arguments) {
            BigDecimal number;
            try {
                number = new BigDecimal(argument);
            } catch (NumberFormatException nfe) {
                throw new FunctionExecutionException("argument is not a valid number:  " + argument);
            }
            result = result == null ? number : result.subtract(number);
        }
        return result.toString();
    }

    public int getMinNumberOfArgsSupported() {
        return 2;
    }

    public int getMaxNumberOfArgsSupported() {
        return Integer.MAX_VALUE;
    }


}
