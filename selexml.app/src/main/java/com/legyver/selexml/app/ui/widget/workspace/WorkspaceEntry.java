package com.legyver.selexml.app.ui.widget.workspace;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class WorkspaceEntry extends Control {
    private final ObservableList<Node> workspaceNodes = FXCollections.observableArrayList();
    private final StringProperty text = new SimpleStringProperty();
    private final BooleanProperty pulse = new SimpleBooleanProperty();

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

    public ObservableList<Node> workspaceNodesProperty() {
        return workspaceNodes;
    }

    public boolean isPulse() {
        return pulse.get();
    }

    public BooleanProperty pulseProperty() {
        return pulse;
    }

    public void setPulse(boolean pulse) {
        this.pulse.set(pulse);
    }
}
