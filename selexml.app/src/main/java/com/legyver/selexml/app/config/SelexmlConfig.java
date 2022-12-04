package com.legyver.selexml.app.config;

import com.legyver.fenxlib.api.config.section.ConfigPersisted;
import com.legyver.fenxlib.widgets.filetree.config.FileTreeConfig;

public class SelexmlConfig extends FileTreeConfig {

    @ConfigPersisted
    private SelexmlConfigSection selexmlConfig = new SelexmlConfigSection();

    public SelexmlConfigSection getSelexmlConfig() {
        return selexmlConfig;
    }

    public void setSelexmlConfig(SelexmlConfigSection selexmlConfig) {
        this.selexmlConfig = selexmlConfig;
    }
}
