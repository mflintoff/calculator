package com.mflintoff.calculator.function;

/**
 * Thrown when there is an error executing a function.
 *
 * @author Malcolm Flintoff
 */
public class FunctionExecutionException extends Exception {

    /**
     * Constructs a FunctionExecutionException with the given error message.
     *
     * @param msg the error message.
     */
    public FunctionExecutionException(String msg) {
        super(msg);
    }

    /**
     * Constructs a FunctionExecutionException with the given error message and cause.
     *
     * @param msg the error message.
     * @param cause the cause.
     */
    public FunctionExecutionException(String msg, Exception cause) {
        super(msg, cause);
    }

}
