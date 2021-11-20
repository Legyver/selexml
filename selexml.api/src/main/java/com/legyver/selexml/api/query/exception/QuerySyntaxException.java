package com.legyver.selexml.api.query.exception;

import com.legyver.core.exception.CoreException;

/**
 * Exception when a query is invalid
 */
public class QuerySyntaxException extends CoreException {
    /**
     * the received query
     */
    private final String value;

    /**
     * Construct an Exception for a query
     * @param aMessage the message to display
     * @param value the received query
     */
    public QuerySyntaxException(String aMessage, String value) {
        super(aMessage);
        this.value = value;
    }

    /**
     * Get the query text received
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
