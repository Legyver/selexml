package com.legyver.selexml.app.ui.widget.workspace;

import com.legyver.fenxlib.core.web.DesktopWeblink;
import com.legyver.selexml.app.ui.widget.IQueryResult;
import com.legyver.selexml.app.ui.widget.QueryResultReport;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListView;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class WorkspaceSkin extends SkinBase<Workspace> {
    private final VBox vBox;
    private final ListView listView;

    public WorkspaceSkin(Workspace workspace) {
        super(workspace);
        vBox = new VBox();
        this.listView = new ListView();

        WorkspaceEntry workspaceEntry = new WorkspaceEntry();
        vBox.getChildren().add(workspaceEntry);
        vBox.getChildren().add(listView);

        workspaceEntry.pulseProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue != oldValue) {
                   Platform.runLater(() -> {
                       workspace.executeQuery(workspaceEntry.getText());
                   });
                }
            }
        });

        workspace.queryResultProperty().addListener(new ChangeListener<IQueryResult>() {
            @Override
            public void changed(ObservableValue<? extends IQueryResult> observable, IQueryResult oldValue, IQueryResult newValue) {
                if (newValue instanceof QueryResultReport) {
                    QueryResultReport queryResultReport = (QueryResultReport) newValue;

                    TextFlow textFlow = new TextFlow();
                    Text text = new Text(queryResultReport.getResultSize() + " matches.  ");
                    textFlow.getChildren().add(text);
                    Text queryText = new Text(workspaceEntry.getText());
                    textFlow.getChildren().add(queryText);

                    DesktopWeblink desktopWeblink = new DesktopWeblink("See result XML", queryResultReport.getUri());
                    textFlow.getChildren().add(desktopWeblink);

                    listView.getItems().add(textFlow);
                }
            }
        });

        getChildren().add(vBox);
    }


}
