package com.mflintoff.calculator.function;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Function that divides a series of numbers.
 *
 * @autho Malcolm Flintoff.
 */
public class DivideFunction implements Function {

    public String getName() {
        return "div";
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
            try {
                // we use 8 decimal point scale/precision - we need to define a scale here, otherwise we get
                // ArithmeticException when there is a non-terminating decimal (eg. 10 / 3)
                result = result == null ? number : result.divide(number, 8, RoundingMode.HALF_UP);
            } catch (ArithmeticException ae) {
                throw new FunctionExecutionException("error occurred during division: " + ae.getMessage(), ae);
            }
        }
        return result.stripTrailingZeros().toString();
    }

    public int getMinNumberOfArgsSupported() {
        return 2;
    }

    public int getMaxNumberOfArgsSupported() {
        return Integer.MAX_VALUE;
    }


}
