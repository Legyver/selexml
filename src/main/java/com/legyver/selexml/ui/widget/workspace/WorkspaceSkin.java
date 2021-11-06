package com.legyver.selexml.ui.widget.workspace;

import com.legyver.fenxlib.core.impl.web.DesktopWeblink;
import com.legyver.selexml.ui.widget.IQueryResult;
import com.legyver.selexml.ui.widget.QueryResultReport;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.SkinBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Iterator;

public class WorkspaceSkin extends SkinBase<Workspace> {

    private final ListView listView;

    public WorkspaceSkin(Workspace workspace) {
        super(workspace);
        this.listView = new ListView();

        WorkspaceEntry workspaceEntry = new WorkspaceEntry();
        workspaceEntry.setOnKeyPressed(new OnEnterHandler(workspaceEntry, listView.getItems().size()));
        listView.getItems().add(workspaceEntry);

        workspace.queryResultProperty().addListener(new ChangeListener<IQueryResult>() {
            @Override
            public void changed(ObservableValue<? extends IQueryResult> observable, IQueryResult oldValue, IQueryResult newValue) {
                if (newValue instanceof QueryResultReport) {
                    TextFlow textFlow = new TextFlow();
                    Text text = new Text(((QueryResultReport) newValue).getResultSize() + " matches.");
                    textFlow.getChildren().add(text);
                    DesktopWeblink desktopWeblink = new DesktopWeblink("See result XML", ((QueryResultReport) newValue).getUrl());
                    textFlow.getChildren().add(desktopWeblink);
                    listView.getItems().add(textFlow);

                    Platform.runLater(() -> {
                        //add a blank workbook entry below
                        WorkspaceEntry workspaceEntry = new WorkspaceEntry();
                        workspaceEntry.setOnKeyPressed(new OnEnterHandler(workspaceEntry, listView.getItems().size()));
                        listView.getItems().add(workspaceEntry);
                    });
                }
            }
        });

        getChildren().add(listView);
    }

    private class OnEnterHandler implements EventHandler<KeyEvent> {
        private final WorkspaceEntry workspaceEntry;
        private final int index;

        private OnEnterHandler(WorkspaceEntry workspaceEntry, int index) {
            this.workspaceEntry = workspaceEntry;
            this.index = index;
        }

        @Override
        public void handle(KeyEvent event) {
            if (event.getCode().equals(KeyCode.ENTER)) {
                Workspace workspace = getSkinnable();
                workspace.executeQuery(workspaceEntry.getText());
                ObservableList items = listView.getItems();
                //remove all old items (come after the executed one)
                int i = 0;
                for (Iterator<Node> itemIt = items.iterator(); itemIt.hasNext(); i++) {
                    if (i > index) {
                        itemIt.remove();
                    }
                }
            }
        }
    }
}
