package com.legyver.selexml.app.ui.widget;

import java.net.URI;

public class QueryResultReport implements IQueryResult {
    private final URI uri;
    private final int resultSize;

    public QueryResultReport(URI uri, int resultSize) {
        this.uri = uri;
        this.resultSize = resultSize;
    }

    @Override
    public String getStatus() {
        return "OK";
    }

    public URI getUri() {
        return uri;
    }

    public int getResultSize() {
        return resultSize;
    }
}
