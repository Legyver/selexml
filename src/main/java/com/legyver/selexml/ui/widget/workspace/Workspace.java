package com.legyver.selexml.ui.widget.workspace;

import com.legyver.selexml.task.QueryExecutor;
import com.legyver.selexml.task.WorkspaceScope;
import com.legyver.selexml.ui.widget.IQueryResult;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class Workspace extends Control {
    private final WorkspaceScope workspaceScope;
    private final ObjectProperty<IQueryResult> queryResult = new SimpleObjectProperty<>();

    public Workspace(WorkspaceScope workspaceScope) {
        this.workspaceScope = workspaceScope;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new WorkspaceSkin(this);
    }

    public IQueryResult getQueryResult() {
        return queryResult.get();
    }

    public ObjectProperty<IQueryResult> queryResultProperty() {
        return queryResult;
    }

    public void setQueryResult(IQueryResult queryResult) {
        this.queryResult.set(queryResult);
    }

    public void executeQuery(String query) {
        queryResult.setValue(new QueryExecutor(workspaceScope).execute(query));
    }
}
