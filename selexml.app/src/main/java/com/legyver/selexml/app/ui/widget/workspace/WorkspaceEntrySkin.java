package com.legyver.selexml.app.ui.widget.workspace;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Iterator;

public class WorkspaceEntrySkin extends SkinBase<WorkspaceEntry> {
    private final ObservableList<Node> workspaceNodes;
    private final TextArea textArea;

    public WorkspaceEntrySkin(WorkspaceEntry workspaceEntry) {
        super(workspaceEntry);
        this.workspaceNodes = workspaceEntry.workspaceNodesProperty();
        this.textArea = new TextArea();
        textArea.setOnKeyPressed(new OnEnterHandler(workspaceNodes.size()));
        workspaceEntry.textProperty().bind(textArea.textProperty());

        getChildren().add(textArea);
    }

    private class OnEnterHandler implements EventHandler<KeyEvent> {
        private final int index;

        private OnEnterHandler(int index) {
            this.index = index;
        }

        @Override
        public void handle(KeyEvent event) {
            if (event.getCode().equals(KeyCode.ENTER)) {
                WorkspaceEntry workspaceEntry = getSkinnable();
                workspaceEntry.setPulse(!workspaceEntry.isPulse());
                //remove all old items (come after the executed one)
                int i = 0;
                for (Iterator<Node> itemIt = workspaceNodes.iterator(); itemIt.hasNext(); i++) {
                    if (i > index) {
                        itemIt.remove();
                    }
                }
            }
        }
    }
}
