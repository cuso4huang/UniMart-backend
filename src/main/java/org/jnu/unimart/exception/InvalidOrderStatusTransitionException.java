package org.jnu.unimart.exception;

public class InvalidOrderStatusTransitionException extends RuntimeException {
    public InvalidOrderStatusTransitionException(String s) {
        super(s);
    }
}