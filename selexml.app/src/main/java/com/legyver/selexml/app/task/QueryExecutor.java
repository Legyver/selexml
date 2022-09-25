package com.legyver.selexml.app.task;

import com.legyver.core.exception.CoreException;
import com.legyver.selexml.api.query.XmlGraphSearchCriteria;
import com.legyver.selexml.api.query.exception.QuerySyntaxException;
import com.legyver.selexml.api.query.text.SqlSyntaxInterpreter;
import com.legyver.selexml.app.ui.widget.IQueryResult;
import com.legyver.selexml.app.ui.widget.QueryError;
import com.legyver.selexml.app.ui.widget.QueryResultReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QueryExecutor {
    private static final Logger logger = LogManager.getLogger(QueryExecutor.class);
    private final WorkspaceScope workspaceScope;
    private final SqlSyntaxInterpreter sqlSyntaxInterpreter;

    public QueryExecutor(WorkspaceScope workspaceScope) {
        this.workspaceScope = workspaceScope;
        this.sqlSyntaxInterpreter = new SqlSyntaxInterpreter();
    }

    public IQueryResult execute(String query) {
        try {
            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            WorkspaceScope.SearchResult result = workspaceScope.search(criteria);
            return new QueryResultReport(result.getResultFile().toURI(), result.getSize());
        } catch (QuerySyntaxException e) {
            return new QueryError(e.getMessage());
        } catch (CoreException exception) {
            logger.error(exception);
            String message = exception.getMessage();
            Throwable throwable = exception;
            while (message == null && throwable != null) {
                throwable = throwable.getCause();
                message = throwable.getMessage();
            }
            return new QueryError(message);
        }
    }
}
