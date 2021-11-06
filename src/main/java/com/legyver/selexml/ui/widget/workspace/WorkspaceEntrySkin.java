package com.legyver.selexml.ui.widget.workspace;

import javafx.scene.control.SkinBase;
import javafx.scene.control.TextArea;

public class WorkspaceEntrySkin extends SkinBase<WorkspaceEntry> {
    private final TextArea textArea;

    public WorkspaceEntrySkin(WorkspaceEntry workspaceEntry) {
        super(workspaceEntry);
        this.textArea = new TextArea();
        workspaceEntry.textProperty().bind(textArea.textProperty());

        getChildren().add(textArea);
    }
}
