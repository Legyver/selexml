package com.legyver.selexml.app.ui.widget.store;

import com.legyver.core.exception.CoreException;
import com.legyver.fenxlib.core.api.locator.LocationContext;
import com.legyver.fenxlib.core.impl.factory.TabContentFactory;
import javafx.scene.control.Tab;

public class StoreTabFactory implements TabContentFactory<Tab> {

    @Override
    public Tab makeNode(LocationContext locationContext) throws CoreException {
        Tab tab = new Tab("Store", new StoreTab());
        return tab;
    }
}
