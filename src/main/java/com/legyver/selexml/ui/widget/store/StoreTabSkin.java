package com.legyver.selexml.ui.widget.store;

import com.legyver.fenxlib.core.impl.web.DesktopWeblink;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class StoreTabSkin extends SkinBase<StoreTab> {
    private FlowPane flowPane;
    public StoreTabSkin(StoreTab storeTab) {
        super(storeTab);
        flowPane = new FlowPane();
        TextFlow textFlow = new TextFlow();
        textFlow.getChildren().add(new Text("Come support us at our "));
        textFlow.getChildren().add(new DesktopWeblink("store", "https://products.legyver.com/"));
        flowPane.getChildren().add(textFlow);
        getChildren().add(flowPane);
    }
}
