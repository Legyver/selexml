package com.legyver.selexml.query.exception;

import com.legyver.core.exception.CoreException;

public class QuerySyntaxException extends CoreException {
    private final String value;
    public QuerySyntaxException(String aMessage, String value) {
        super(aMessage);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
