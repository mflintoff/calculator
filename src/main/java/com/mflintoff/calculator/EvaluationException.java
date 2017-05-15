package com.mflintoff.calculator;

/**
 * Thrown when an error occurs evaluating an expression.
 *
 * @author Malcolm Flintoff
 */
public class EvaluationException extends Exception {

    /**
     * Constructs an EvaluationException with the given error message.
     *
     * @param msg the error message.
     */
    public EvaluationException(String msg) {
        super(msg);
    }

    /**
     * Constructs an EvaluationException with the specified error message and cause.
     *
     * @param msg the error message.
     * @param cause the cause.
     */
    public EvaluationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
