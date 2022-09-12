package com.legyver.selexml.app.factory;

import com.legyver.fenxlib.widgets.filetree.factory.DefaultFileTreeItemContextMenuFactory;
import com.legyver.fenxlib.widgets.filetree.factory.FileTreeItemContextMenuItemFactory;
import com.legyver.fenxlib.widgets.filetree.factory.TreeItemChildFactory;
import com.legyver.fenxlib.widgets.filetree.tree.FileTreeItem;
import com.legyver.fenxlib.widgets.filetree.tree.TreeFile;
import com.legyver.fenxlib.widgets.filetree.tree.TreeFolder;
import com.legyver.selexml.app.ui.widget.status.StatusMonitor;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TabPane;
import org.apache.commons.io.filefilter.SuffixFileFilter;

public class SelexmlTreeItemFactory extends TreeItemChildFactory {

    public SelexmlTreeItemFactory(SuffixFileFilter suffixFileFilter, TabPane tabPane, StatusMonitor statusMonitor) {
        super(new DefaultFileTreeItemContextMenuFactory(
            new FileTreeItemContextMenuItemFactory(LaunchWorkbookFactory.MENU_ITEM_NAME, new LaunchWorkbookFactory(suffixFileFilter, tabPane, statusMonitor))
        ));
    }

    protected void initializeContextMenuItemEnabledProperties(FileTreeItem parentFileTreeItem, TreeFile treeFile) {
        super.initializeContextMenuItemEnabledProperties(parentFileTreeItem, treeFile);
        treeFile.addMenuItemEnabledProperty(LaunchWorkbookFactory.MENU_ITEM_NAME, new SimpleBooleanProperty(true));
    }

    protected void initializeContextMenuItemEnabledProperties(FileTreeItem parentFileTreeItem, TreeFolder treeFolder) {
        super.initializeContextMenuItemEnabledProperties(parentFileTreeItem, treeFolder);
        treeFolder.addMenuItemEnabledProperty(LaunchWorkbookFactory.MENU_ITEM_NAME, new SimpleBooleanProperty(true));
    }
}
