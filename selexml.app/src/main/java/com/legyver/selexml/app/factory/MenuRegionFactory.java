package com.legyver.selexml.app.factory;

import com.legyver.fenxlib.core.api.locator.query.ComponentQuery;
import com.legyver.fenxlib.core.impl.factory.TextFieldFactory;
import com.legyver.fenxlib.core.impl.factory.TopRegionFactory;
import com.legyver.fenxlib.core.impl.factory.menu.*;
import com.legyver.fenxlib.core.impl.factory.options.BorderPaneInitializationOptions;
import com.legyver.fenxlib.widgets.about.AboutMenuItemFactory;
import com.legyver.selexml.app.config.SelexmlVersionInfo;
import javafx.scene.layout.StackPane;

import java.util.Optional;

public class MenuRegionFactory extends TopRegionFactory {

    public MenuRegionFactory(SelexmlVersionInfo selexmlVersionInfo) {
        super(new LeftMenuOptions(
                        new MenuFactory("File",
                                new ExitMenuItemFactory("Exit")
                        )
                ),
                new CenterOptions(new TextFieldFactory(false)),
                new RightMenuOptions(
                        new MenuFactory("Help",
                                new AboutMenuItemFactory("About", MenuRegionFactory::centerContentReference, selexmlVersionInfo.getAboutPageOptions())
                        )
                ));
    }

    public static StackPane centerContentReference() {
        Optional<StackPane> center = new ComponentQuery.QueryBuilder()
                .inRegion(BorderPaneInitializationOptions.REGION_CENTER)
                .type(StackPane.class).execute();
        return center.get();
    }
}
