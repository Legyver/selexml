package com.legyver.selexml;

import com.legyver.core.exception.CoreException;
import com.legyver.fenxlib.core.impl.config.options.ApplicationOptions;
import com.legyver.fenxlib.core.impl.context.ApplicationContext;
import com.legyver.fenxlib.core.impl.factory.BorderPaneFactory;
import com.legyver.fenxlib.core.impl.factory.BottomRegionFactory;
import com.legyver.fenxlib.core.impl.factory.SceneFactory;
import com.legyver.fenxlib.core.impl.factory.StackPaneRegionFactory;
import com.legyver.fenxlib.core.impl.factory.options.BorderPaneInitializationOptions;
import com.legyver.fenxlib.core.impl.factory.options.RegionInitializationOptions;
import com.legyver.fenxlib.widgets.filetree.factory.SimpleFileExplorerFactory;
import com.legyver.fenxlib.widgets.filetree.registry.FileTreeRegistry;
import com.legyver.fenxlib.widgets.filetree.scan.FileWatchHandler;
import com.legyver.selexml.config.ApplicationOptionsBuilder;
import com.legyver.selexml.config.SelexmlConfig;
import com.legyver.selexml.config.SelexmlVersionInfo;
import com.legyver.selexml.factory.MenuRegionFactory;
import com.legyver.selexml.ui.ApplicationUIModel;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainApplication extends Application {

    private static Logger logger;
    private static ApplicationOptions applicationOptions;
    private SelexmlVersionInfo selexmlVersionInfo;
    private ApplicationUIModel uiModel;

    public static void main(String[] args) throws CoreException {
        try {
            applicationOptions = new ApplicationOptionsBuilder()
                    .appName("Selexml")
                    .customAppConfigInstantiator(map -> new SelexmlConfig(map))
                    .uiModel(new ApplicationUIModel())
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
            applicationOptions.startup();
            uiModel = (ApplicationUIModel) ApplicationContext.getUiModel();
            selexmlVersionInfo = new SelexmlVersionInfo();

            SceneFactory sceneFactory = new SceneFactory(primaryStage, 1100, 750, MainApplication.class.getClassLoader().getResource("css/application.css"));

            //Any files added via import or filesystem watches on added directories will be added here
            FileTreeRegistry fileTreeRegistry = new FileTreeRegistry();

            //while watching file system, only auto-add folders and xml files
            FileWatchHandler fileWatchHandler = new FileWatchHandler.Builder()
                    .fileFilter(new SuffixFileFilter(".xml"))
                    .build(fileTreeRegistry);

            BorderPaneInitializationOptions options = new BorderPaneInitializationOptions.Builder()
                    .left(new RegionInitializationOptions.SideBuilder("Files")
                            .displayContentByDefault()
                            .factory(new StackPaneRegionFactory(false,
                                    new SimpleFileExplorerFactory(fileTreeRegistry, fileWatchHandler)
                            ))
                    )
                    .center(new RegionInitializationOptions.Builder()
                            //popup will display over this. See the centerContentReference Supplier above
                            .factory(new StackPaneRegionFactory(true))
                    )
                    .top(new RegionInitializationOptions.Builder()
                            .displayContentByDefault()
                            .factory(new MenuRegionFactory(getSelexmlVersionInfo()))
                    )
                    .bottom(new RegionInitializationOptions.SideAwareBuilder()
                            .factory(new BottomRegionFactory())
                    )
                    .build();

            BorderPane root = new BorderPaneFactory(options).makeBorderPane();

            primaryStage.setScene(sceneFactory.makeScene(root));
            primaryStage.setTitle("Selexml");
            primaryStage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("/legyver_selexml_icon.png")));
            primaryStage.show();
        } catch (Exception ex) {
            logger.error("Error in MainApplication.start() " + ex.getMessage(), ex);
            System.exit(1);
        }

    }

    public SelexmlVersionInfo getSelexmlVersionInfo() {
        return selexmlVersionInfo;
    }

    public ApplicationUIModel getUiModel() {
        return uiModel;
    }
}
