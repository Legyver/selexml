package com.legyver.selexml.ui.widget.store;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class StoreTab extends Control {

    @Override
    protected Skin<?> createDefaultSkin() {
        return new StoreTabSkin(this);
    }
}
