package com.legyver.selexml.ui.widget.workspace;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class WorkspaceEntry extends Control {
    private final StringProperty text = new SimpleStringProperty();

    @Override
    protected Skin<?> createDefaultSkin() {
        return new WorkspaceEntrySkin(this);
    }

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }
}
