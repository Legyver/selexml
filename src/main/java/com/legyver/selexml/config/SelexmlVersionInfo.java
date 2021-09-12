package com.legyver.selexml.config;

import com.legyver.fenxlib.widgets.about.AboutPageOptions;

import java.util.Properties;

public class SelexmlVersionInfo {
    private final AboutPageOptions aboutPageOptions;

    public SelexmlVersionInfo() {
        aboutPageOptions = new AboutPageOptions.Builder(getClass())
                .dependenciesFile("licenses/license.properties")
                .buildPropertiesFile("buildlabel.properties")
                .copyrightPropertiesFile("copyright.properties")
                .title("Selexml")
                .intro("Query your xml with Selexml")
                .additionalInfo("""
                        Designed to make querying XML as easy as if it were a relational database, Selexml allows you to
                        map your xml element names to tables so you can construct meaningful queries of large datasets. 
                        """)
                .build();
    }

    public AboutPageOptions getAboutPageOptions() {
        return aboutPageOptions;
    }

    public Properties getBuildProperties() {
        return aboutPageOptions.getBuildProperties();
    }
}
