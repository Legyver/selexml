package com.legyver.selexml.app;

import com.legyver.core.exception.CoreException;
import com.legyver.fenxlib.core.impl.config.options.ApplicationOptions;
import com.legyver.fenxlib.core.impl.context.ApplicationContext;
import com.legyver.fenxlib.core.impl.factory.BorderPaneFactory;
import com.legyver.fenxlib.core.impl.factory.SceneFactory;
import com.legyver.fenxlib.core.impl.factory.StackPaneRegionFactory;
import com.legyver.fenxlib.core.impl.factory.TabPaneFactory;
import com.legyver.fenxlib.core.impl.factory.decorator.RegisterAsDecorator;
import com.legyver.fenxlib.core.impl.factory.options.BorderPaneInitializationOptions;
import com.legyver.fenxlib.core.impl.factory.options.RegionInitializationOptions;
import com.legyver.fenxlib.widgets.filetree.factory.SimpleFileExplorerFactory;
import com.legyver.fenxlib.widgets.filetree.factory.TreeItemChildFactory;
import com.legyver.fenxlib.widgets.filetree.registry.FileTreeRegistry;
import com.legyver.fenxlib.widgets.filetree.scan.FileWatchHandler;
import com.legyver.selexml.app.config.ApplicationOptionsBuilder;
import com.legyver.selexml.config.SelexmlConfig;
import com.legyver.selexml.app.config.SelexmlVersionInfo;
import com.legyver.selexml.app.factory.MenuRegionFactory;
import com.legyver.selexml.app.factory.SelexmlTreeItemFactory;
import com.legyver.selexml.app.ui.ApplicationUIModel;
import com.legyver.selexml.app.ui.widget.status.BottomRegionStatusFactory;
import com.legyver.selexml.app.ui.widget.store.StoreTabFactory;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainApplication extends Application {

    public static final String TABS = "_tabs";
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

            SuffixFileFilter suffixFileFilter = new SuffixFileFilter(".xml");
            TreeItemChildFactory treeItemChildFactory = new SelexmlTreeItemFactory(suffixFileFilter);


            //while watching file system, only auto-add folders and xml files
            FileWatchHandler fileWatchHandler = new FileWatchHandler.Builder()
                    .fileFilter(suffixFileFilter)
                    .childFactory(treeItemChildFactory)
                    .build(fileTreeRegistry);

            BorderPaneInitializationOptions options = new BorderPaneInitializationOptions.Builder()
                    .left(new RegionInitializationOptions.SideBuilder("Files")
                            .displayContentByDefault()
                            .factory(new StackPaneRegionFactory(false,
                                    new SimpleFileExplorerFactory(fileTreeRegistry, fileWatchHandler)
                            ))
                    )
                    .center(new RegionInitializationOptions.Builder()
                            .factory(new StackPaneRegionFactory(true,
                                    new RegisterAsDecorator(new TabPaneFactory(new StoreTabFactory()), TABS)
                            ))
                    )
                    .top(new RegionInitializationOptions.Builder()
                            .displayContentByDefault()
                            .factory(new MenuRegionFactory(getSelexmlVersionInfo()))
                    )
                    .bottom(new RegionInitializationOptions.SideAwareBuilder()
                            .factory(new BottomRegionStatusFactory())
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
