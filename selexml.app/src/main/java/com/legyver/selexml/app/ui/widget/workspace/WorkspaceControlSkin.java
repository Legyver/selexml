package com.legyver.selexml.app.ui.widget.workspace;

import com.legyver.core.exception.CoreException;
import com.legyver.fenxlib.api.controls.ControlsFactory;
import com.legyver.fenxlib.api.icons.options.IconOptions;
import com.legyver.fenxlib.api.scene.layout.options.HBoxOptions;
import com.legyver.fenxlib.controls.icon.IconControl;
import com.legyver.fenxlib.icons.standard.IcoMoonFontEnum;
import com.legyver.fenxlib.icons.standard.IcoMoonIconOptions;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorkspaceControlSkin extends SkinBase<WorkspaceControl> {
    private static final Logger logger = LogManager.getLogger(WorkspaceControlSkin.class);
    private HBox hBox;
    private final IconControl iconControl;
    private final TextArea textArea;


    public WorkspaceControlSkin(WorkspaceControl workspaceControl) {
        super(workspaceControl);
        this.textArea = new TextArea();
        this.iconControl = new IconControl();
        IconOptions iconOptions = new IcoMoonIconOptions.Builder()
                .icoMoonIcon(IcoMoonFontEnum.ICON_PLAY)
                .iconColor(Color.GREEN)
                .iconSize(20)
                .build();
        iconControl.setIconOptions(iconOptions);
        try {
            this.hBox = ControlsFactory.make(HBox.class, new HBoxOptions().name("controls"));
        } catch (CoreException e) {
            logger.error(e);
            this.hBox = new HBox();
        }

        hBox.setPadding(new Insets(4, 2.5, 2.5,10));
        hBox.getChildren().add(iconControl);
        hBox.setId("workspace-control-controls-box");

        iconControl.setOnMouseClicked(new RunSelectedTextQuery());
        workspaceControl.textProperty().bind(textArea.textProperty());

        BorderPane layout = new BorderPane();
        layout.setTop(hBox);
        layout.setCenter(textArea);

        getChildren().add(layout);
    }

    private class RunSelectedTextQuery implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            WorkspaceControl workspaceControl = getSkinnable();
            workspaceControl.setSelectedText(textArea.getSelectedText());
            workspaceControl.setPulse(!workspaceControl.isPulse());
        }
    }
}
