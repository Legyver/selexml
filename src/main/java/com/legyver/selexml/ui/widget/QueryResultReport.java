package com.legyver.selexml.ui.widget;

public class QueryResultReport implements IQueryResult {
    private final String url;
    private final int resultSize;

    public QueryResultReport(String url, int resultSize) {
        this.url = url;
        this.resultSize = resultSize;
    }

    @Override
    public String getStatus() {
        return "OK";
    }

    public String getUrl() {
        return url;
    }

    public int getResultSize() {
        return resultSize;
    }
}
