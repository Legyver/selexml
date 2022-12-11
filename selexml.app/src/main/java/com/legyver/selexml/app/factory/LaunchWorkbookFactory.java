package com.legyver.selexml.app.factory;

import com.legyver.core.exception.CoreException;
import com.legyver.fenxlib.api.locator.LocationContext;
import com.legyver.fenxlib.widgets.filetree.registry.FileTreeRegistry;
import com.legyver.fenxlib.widgets.filetree.tree.FileTreeItem;
import com.legyver.fenxlib.widgets.filetree.utils.LocationUtils;
import com.legyver.selexml.app.task.WorkspaceAccumulator;
import com.legyver.selexml.app.task.WorkspaceScope;
import com.legyver.selexml.app.ui.widget.status.StatusMonitor;
import com.legyver.selexml.app.ui.widget.workspace.Workspace;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.function.BiFunction;

public class LaunchWorkbookFactory implements BiFunction<FileTreeRegistry, FileTreeItem, EventHandler<ActionEvent>> {
    private static final Logger logger = LogManager.getLogger(LaunchWorkbookFactory.class);

    public static final String MENU_ITEM_NAME = "Launch Workbook";
    private final SuffixFileFilter suffixFileFilter;
    private final TabPane tabPane;
    private final StatusMonitor statusMonitor;
    private ObjectProperty<TreeView> treeViewProperty = new SimpleObjectProperty<>();


    public LaunchWorkbookFactory(SuffixFileFilter suffixFileFilter, TabPane tabPane, StatusMonitor statusMonitor) {
        this.suffixFileFilter = suffixFileFilter;
        this.tabPane = tabPane;
        this.statusMonitor = statusMonitor;
    }

    @Override
    public EventHandler<ActionEvent> apply(FileTreeRegistry fileTreeRegistry, FileTreeItem fileTreeItem) {
        return event -> {
            if (treeViewProperty.get() == null) {
                try {
                    LocationContext fileExplorerLocation = fileTreeRegistry.getFileExplorerLocation();
                    TreeView treeView = LocationUtils.findTreeViewForFileExplorer(fileExplorerLocation);
                    treeViewProperty.set(treeView);
                } catch (CoreException coreException) {
                    logger.error("Error locating tree view", coreException);
                }
            }
            TreeView treeView = treeViewProperty.get();
            MultipleSelectionModel multipleSelectionModel = treeView.getSelectionModel();
            ObservableList<FileTreeItem> selectedItems = multipleSelectionModel.getSelectedItems();
            List<File> filesInScope = new WorkspaceAccumulator(suffixFileFilter).filter(selectedItems);

            WorkspaceScope workspaceScope = new WorkspaceScope(filesInScope);
            statusMonitor.messageProperty().setValue("Evaluating XML");
            String label = null;
            for (int i = 0; i < selectedItems.size(); i++) {
                String itemName = selectedItems.get(i).getName();
                itemName = itemName.substring(0, itemName.lastIndexOf('.'));
                if (label == null) {
                    label = itemName;
                } else {
                    label += ", " + itemName;
                }
                if (label.length() > 100 && i < selectedItems.size() - 1) {
                    label += "...";
                }
            }

            //add a new tab
            int tabCount = tabPane.getTabs().size();
            tabPane.getTabs().add(new Tab(label, new Workspace(workspaceScope)));
            tabPane.getSelectionModel().select(tabCount);

            Platform.runLater(() -> {
                workspaceScope.init();
            });
        };
    }
}
