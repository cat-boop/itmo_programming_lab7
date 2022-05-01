package com.lab7.common.exceptions;

/**
 * exception class shows that script contains error
 */
public class ReadElementFromScriptException extends RuntimeException {
    public ReadElementFromScriptException(String message) {
        super(message);
    }
}
