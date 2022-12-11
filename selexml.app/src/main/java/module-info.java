module com.legyver.selexml.app {
    requires com.legyver.fenxlib.api;
    requires com.legyver.fenxlib.core;
    requires com.legyver.fenxlib.config.json;
    requires com.legyver.fenxlib.extensions.tuktukfx;
    requires com.legyver.fenxlib.widgets.about;
    requires com.legyver.fenxlib.widgets.filetree;
    requires javafx.graphics;
    requires javafx.controls;

    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.apache.logging.log4j;
    requires com.legyver.selexml.api;
    requires com.legyver.utils.graphjxml;

    exports com.legyver.selexml.app to javafx.graphics;
    exports com.legyver.selexml.app.config to com.fasterxml.jackson.databind, com.legyver.utils.ruffles;
    opens com.legyver.selexml.app.config to com.legyver.fenxlib.config.json;
    opens com.legyver.selexml.app;
}