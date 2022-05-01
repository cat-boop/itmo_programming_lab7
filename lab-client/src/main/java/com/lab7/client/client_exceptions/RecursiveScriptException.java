package com.lab7.common.exceptions;

/**
 * exception class shows that script contains recursion
 */
public class RecursiveScriptException extends RuntimeException {
    public RecursiveScriptException(String message) {
        super(message);
    }
}
