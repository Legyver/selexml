package com.legyver.selexml.app.ui.widget.status;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class StatusMonitorSkin extends SkinBase<StatusMonitor> {
    private final StackPane stackPane;
    private final HBox hBox;

    public StatusMonitorSkin(StatusMonitor statusMonitor) {
        super(statusMonitor);
        this.stackPane = new StackPane();
        hBox = new HBox();

        Label label = new Label();
        label.textProperty().bind(statusMonitor.messageProperty());
        hBox.getChildren().add(label);

        ProgressBar progressBar = new ProgressBar();
        progressBar.progressProperty().bind(statusMonitor.progressProperty());
        hBox.getChildren().add(progressBar);

        stackPane.getChildren().add(hBox);
        progressBar.progressProperty().addListener((observable, oldValue, newValue) -> {
            Double newValueDouble = (Double) newValue;
            if (newValueDouble == null || newValueDouble.doubleValue() >= 1.0) {
                hBox.setVisible(false);
            } else if (newValueDouble.doubleValue() > 0.0) {
                hBox.setVisible(true);
            }
        });

        getChildren().add(stackPane);
    }
}
