package com.legyver.selexml.app.ui.widget;

public class QueryError implements IQueryResult {
    private final String errorMessage;

    public QueryError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getStatus() {
        return "Error";
    }

    public String getMessage() {
        return errorMessage;
    }
}
