package com.mflintoff.calculator.function;

import java.math.BigDecimal;
import java.util.List;

/**
 * Function that adds a series of numbers.
 *
 * @author Malcolm Flintoff.
 */
public class AddFunction implements Function {

    public String getName() {
        return "add";
    }

    public String execute(List<String> arguments) throws FunctionExecutionException{
        BigDecimal sum = new BigDecimal(0);
        for (String argument : arguments) {
            BigDecimal number;
            try {
                number = new BigDecimal(argument);
            } catch (NumberFormatException nfe) {
                throw new FunctionExecutionException("argument is not a valid number:  " + argument);
            }
            sum = sum.add(number);
        }
        return sum.toString();
    }

    public int getMinNumberOfArgsSupported() {
        return 2;
    }

    public int getMaxNumberOfArgsSupported() {
        return Integer.MAX_VALUE;
    }


}
