module com.legyver.selexml.app {
    requires transitive com.legyver.selexml.api;
    requires javafx.graphics;
    requires org.apache.commons.io;
    requires org.apache.logging.log4j;
    requires transitive com.legyver.fenxlib.widgets.filetree;
    requires transitive com.legyver.fenxlib.core.impl;
    requires com.legyver.fenxlib.extensions.tuktukfx;
    requires com.legyver.fenxlib.widgets.about;
    requires com.legyver.utils.graphjxml;

    exports com.legyver.selexml.app to javafx.graphics;

}