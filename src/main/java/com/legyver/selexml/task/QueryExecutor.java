package com.legyver.selexml.task;

import com.legyver.selexml.ui.widget.IQueryResult;
import com.legyver.selexml.ui.widget.QueryError;
import com.legyver.selexml.ui.widget.QueryResultReport;

public class QueryExecutor {
    private final WorkspaceScope workspaceScope;

    public QueryExecutor(WorkspaceScope workspaceScope) {
        this.workspaceScope = workspaceScope;
    }

    public IQueryResult execute(String query) {
        QueryValidator queryValidator = new QueryValidator(query);
        if (!queryValidator.isValid()) {
            return new QueryError(queryValidator.getErrorMessage());
        }
        return new QueryResultReport(null, workspaceScope.getSize());
    }
}
