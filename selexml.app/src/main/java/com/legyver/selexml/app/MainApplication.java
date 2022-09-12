package com.legyver.selexml.app;

import com.legyver.fenxlib.api.config.options.ApplicationOptions;
import com.legyver.fenxlib.api.context.ApplicationContext;
import com.legyver.fenxlib.api.context.ResourceScope;
import com.legyver.fenxlib.api.controls.ControlsFactory;
import com.legyver.fenxlib.api.locator.DefaultLocationContext;
import com.legyver.fenxlib.api.scene.controls.options.TabPaneOptions;
import com.legyver.fenxlib.core.controls.factory.SceneFactory;
import com.legyver.fenxlib.core.layout.BorderPaneApplicationLayout;
import com.legyver.fenxlib.core.layout.options.CenterRegionOptions;
import com.legyver.fenxlib.core.layout.options.LeftRegionOptions;
import com.legyver.fenxlib.widgets.filetree.SimpleFileExplorer;
import com.legyver.fenxlib.widgets.filetree.factory.SimpleFileExplorerFactory;
import com.legyver.fenxlib.widgets.filetree.factory.SimpleFileExplorerOptions;
import com.legyver.fenxlib.widgets.filetree.factory.TreeItemChildFactory;
import com.legyver.fenxlib.widgets.filetree.registry.FileTreeRegistry;
import com.legyver.fenxlib.widgets.filetree.scan.FileWatchHandler;
import com.legyver.selexml.app.config.ApplicationOptionsBuilder;
import com.legyver.selexml.app.config.SelexmlConfig;
import com.legyver.selexml.app.css.CssResource;
import com.legyver.selexml.app.factory.MenuBarFactory;
import com.legyver.selexml.app.factory.SelexmlTreeItemFactory;
import com.legyver.selexml.app.ui.ApplicationUIModel;
import com.legyver.selexml.app.ui.widget.status.StatusMonitor;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;

public class MainApplication extends Application {

    public static final String TABS = "_tabs";
    private static Logger logger;
    private static ApplicationOptions applicationOptions;
    private ApplicationUIModel uiModel;

    public static void main(String[] args) {
        try {
            applicationOptions = new ApplicationOptionsBuilder()
                    .appName("Selexml")
                    .customAppConfigInstantiator(SelexmlConfig::new)
                    .uiModel(new ApplicationUIModel())
                    .styleSheetUrl(CssResource.getApplicationCSS(), ResourceScope.APPLICATION)
                    .styleSheetUrl(CssResource.getPopupCSS(), ResourceScope.POPUPS)
                    .resourceBundle("com.legyver.selexml.app.bundle")
                    .build();//build() calls bootstrap() which inits logging
            logger = LogManager.getLogger(MainApplication.class);
            launch(args);
        } catch (Exception e) {
            if (logger != null) {
                logger.error("Crash in main: ", e);
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            logger.info("Initializing application");
            applicationOptions.startup(this, primaryStage);
            uiModel = (ApplicationUIModel) ApplicationContext.getUiModel();

            URL stylesheet = MainApplication.class.getResource("application.css");
            if (stylesheet == null) {
                logger.error("unable to load stylesheet");
            }
            SceneFactory sceneFactory = new SceneFactory(primaryStage);

            //Any files added via import or filesystem watches on added directories will be added here
            FileTreeRegistry fileTreeRegistry = new FileTreeRegistry();

            SuffixFileFilter suffixFileFilter = new SuffixFileFilter(".xml");
            TabPane tabPane = ControlsFactory.make(TabPane.class, new TabPaneOptions().name(TABS));
            StatusMonitor statusMonitor = new StatusMonitor();
            TreeItemChildFactory treeItemChildFactory = new SelexmlTreeItemFactory(suffixFileFilter, tabPane, statusMonitor);

            //while watching file system, only auto-add folders and xml files
            FileWatchHandler fileWatchHandler = new FileWatchHandler.Builder()
                    .fileFilter(suffixFileFilter)
                    .childFactory(treeItemChildFactory)
                    .build(fileTreeRegistry);
            SimpleFileExplorerOptions simpleFileExplorerOptions = new SimpleFileExplorerOptions()
                    .fileTreeRegistry(fileTreeRegistry)
                    .fileWatchHandler(fileWatchHandler);

            SimpleFileExplorer simpleFileExplorer = new SimpleFileExplorerFactory()
                    .makeNode(new DefaultLocationContext("Files"), simpleFileExplorerOptions);

            BorderPaneApplicationLayout borderPaneApplicationLayout = new BorderPaneApplicationLayout.BorderPaneBuilder()
                    .title("selexml.title")
                    .width(600.0)
                    .height(800.0)
                    .menuBar(new MenuBarFactory(getClass()).makeMenuBar())
                    .leftRegionOptions(new LeftRegionOptions(simpleFileExplorer))
                    .centerRegionOptions(new CenterRegionOptions(tabPane))
//                    .bottomRegionControlOptions(new BottomRegionOptions(statusMonitor))
                    .build();

            Scene scene = sceneFactory.makeScene(borderPaneApplicationLayout);
            primaryStage.show();
            logger.info("Selexml initialized");
        } catch (Exception ex) {
            logger.error("Error in MainApplication.start() " + ex.getMessage(), ex);
            System.exit(1);
        }

    }

    public ApplicationUIModel getUiModel() {
        return uiModel;
    }
}
