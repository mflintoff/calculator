package com.mflintoff.calculator.function;

import java.util.List;

/**
 * A function used to evaluate expressions.
 *
 * @author Malcolm Flintoff.
 */
public interface Function {

    /**
     * Returns the name of the function. eg. "add".
     *
     * @return the name of the function.
     */
    public String getName();

    /**
     * Execute the function with the given arguments. This method deals with value arguments only. ie. sub-expression
     * arguments must be evaluated before being passed to this method.
     *
     * @param arguments list of arguments.
     * @return the calculated value.
     */
    public String execute(List<String> arguments) throws FunctionExecutionException;

    /**
     * Returns the minimum number of arguments supported by the function.
     *
     * @return the minimum number of arguments supported.
     */
    public int getMinNumberOfArgsSupported();

    /**
     * Returns the maximum number of arguments supported by the function.
     *
     * @return the maximum number of arguments supported.
     */
    public int getMaxNumberOfArgsSupported();

}
