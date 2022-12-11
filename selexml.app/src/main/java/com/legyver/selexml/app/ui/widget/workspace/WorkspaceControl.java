package com.legyver.selexml.app.ui.widget.workspace;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class WorkspaceControl extends Control {
    private final StringProperty text = new SimpleStringProperty();
    private final BooleanProperty pulse = new SimpleBooleanProperty();
    private final StringProperty selectedText = new SimpleStringProperty();

    @Override
    protected Skin<?> createDefaultSkin() {
        return new WorkspaceControlSkin(this);
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

    public boolean isPulse() {
        return pulse.get();
    }

    public BooleanProperty pulseProperty() {
        return pulse;
    }

    public void setPulse(boolean pulse) {
        this.pulse.set(pulse);
    }

    public String getSelectedText() {
        return selectedText.get();
    }

    public StringProperty selectedTextProperty() {
        return selectedText;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText.set(selectedText);
    }
}
