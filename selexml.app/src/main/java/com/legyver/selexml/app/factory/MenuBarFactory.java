package com.legyver.selexml.app.factory;

import com.legyver.core.exception.CoreException;
import com.legyver.fenxlib.core.menu.templates.MenuBuilder;
import com.legyver.fenxlib.core.menu.templates.section.FileExitMenuSection;
import com.legyver.fenxlib.widgets.about.AboutMenuSection;
import com.legyver.fenxlib.widgets.about.AboutPageOptions;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

public class MenuBarFactory {
    private final Class klass;

    public MenuBarFactory(Class klass) {
        this.klass = klass;
    }

    public MenuBar makeMenuBar() throws CoreException {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new MenuBuilder()
                .name("selexml.menu.label.file")
                .menuSection(new FileExitMenuSection())
                .build();
        menuBar.getMenus().add(fileMenu);

        AboutPageOptions aboutPageOptions = new AboutPageOptions.Builder(klass)
                .buildPropertiesFile("build.properties")
                .copyrightPropertiesFile("copyright.properties")
                .title("selexml.about.title")
                .intro("selexml.about.intro")
                .gist("selexml.about.gist")
                .build();
        Menu helpMenu = new MenuBuilder()
                .name("selexml.menu.label.help")
                .menuSection(new AboutMenuSection(aboutPageOptions))
                .build();
        menuBar.getMenus().add(helpMenu);

        return menuBar;
    }
}
