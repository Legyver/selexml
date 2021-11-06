package com.legyver.selexml.factory;

import com.legyver.fenxlib.core.api.locator.query.ComponentQuery;
import com.legyver.fenxlib.core.impl.factory.options.BorderPaneInitializationOptions;
import com.legyver.fenxlib.widgets.filetree.registry.FileTreeRegistry;
import com.legyver.fenxlib.widgets.filetree.tree.FileTreeItem;
import com.legyver.selexml.task.WorkspaceAccumulator;
import com.legyver.selexml.task.WorkspaceScope;
import com.legyver.selexml.ui.widget.status.StatusMonitor;
import com.legyver.selexml.ui.widget.workspace.Workspace;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import java.io.File;
import java.util.List;
import java.util.function.BiFunction;

import static com.legyver.selexml.MainApplication.TABS;

public class LaunchWorkbookFactory implements BiFunction<FileTreeRegistry, FileTreeItem, EventHandler<ActionEvent>> {
    public static final String MENU_ITEM_NAME = "Launch Workbook";
    private final SuffixFileFilter suffixFileFilter;

    public LaunchWorkbookFactory(SuffixFileFilter suffixFileFilter) {
        this.suffixFileFilter = suffixFileFilter;
    }

    @Override
    public EventHandler<ActionEvent> apply(FileTreeRegistry fileTreeRegistry, FileTreeItem fileTreeItem) {
        return event -> {
            List<File> filesInScope = new WorkspaceAccumulator(suffixFileFilter).filter(fileTreeItem);

            WorkspaceScope workspaceScope = new WorkspaceScope(filesInScope);
            StatusMonitor statusMonitor = (StatusMonitor) new ComponentQuery.QueryBuilder()
                    .inRegion(BorderPaneInitializationOptions.REGION_BOTTOM)
                    .type(StatusMonitor.class)
                    .execute().get();
            statusMonitor.messageProperty().setValue("Evaluating XML");

            //add a new tab
            TabPane centerTabs = (TabPane) new ComponentQuery.QueryBuilder()
                    .inRegion(BorderPaneInitializationOptions.REGION_CENTER)
                    .named(TABS).execute().get();
            int tabCount = centerTabs.getTabs().size();
            centerTabs.getTabs().add(new Tab(fileTreeItem.getName(), new Workspace(workspaceScope)));
            centerTabs.getSelectionModel().select(tabCount);
        };
    }
}
