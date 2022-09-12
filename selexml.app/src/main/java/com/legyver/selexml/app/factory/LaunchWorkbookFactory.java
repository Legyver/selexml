package com.legyver.selexml.app.factory;

import com.legyver.fenxlib.widgets.filetree.registry.FileTreeRegistry;
import com.legyver.fenxlib.widgets.filetree.tree.FileTreeItem;
import com.legyver.selexml.app.task.WorkspaceAccumulator;
import com.legyver.selexml.app.task.WorkspaceScope;
import com.legyver.selexml.app.ui.widget.status.StatusMonitor;
import com.legyver.selexml.app.ui.widget.workspace.Workspace;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import java.io.File;
import java.util.List;
import java.util.function.BiFunction;

public class LaunchWorkbookFactory implements BiFunction<FileTreeRegistry, FileTreeItem, EventHandler<ActionEvent>> {
    public static final String MENU_ITEM_NAME = "Launch Workbook";
    private final SuffixFileFilter suffixFileFilter;
    private final TabPane tabPane;
    private final StatusMonitor statusMonitor;


    public LaunchWorkbookFactory(SuffixFileFilter suffixFileFilter, TabPane tabPane, StatusMonitor statusMonitor) {
        this.suffixFileFilter = suffixFileFilter;
        this.tabPane = tabPane;
        this.statusMonitor = statusMonitor;
    }

    @Override
    public EventHandler<ActionEvent> apply(FileTreeRegistry fileTreeRegistry, FileTreeItem fileTreeItem) {
        return event -> {
            List<File> filesInScope = new WorkspaceAccumulator(suffixFileFilter).filter(fileTreeItem);

            WorkspaceScope workspaceScope = new WorkspaceScope(filesInScope);
            statusMonitor.messageProperty().setValue("Evaluating XML");

            //add a new tab
            int tabCount = tabPane.getTabs().size();
            tabPane.getTabs().add(new Tab(fileTreeItem.getName(), new Workspace(workspaceScope)));
            tabPane.getSelectionModel().select(tabCount);

            Platform.runLater(() -> {
                workspaceScope.init();
            });
        };
    }
}
