package com.legyver.selexml.task;

public class QueryValidator {
    private final String queryText;
    private String errorMessage;

    public QueryValidator(String query) {
        this.queryText = query;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isValid() {
        return false;
    }
}
