package com.legyver.selexml.app.config;

import com.legyver.fenxlib.api.config.section.ApplicationVersionedConfigSection;

public class SelexmlConfigSection implements ApplicationVersionedConfigSection {

    @Override
    public String getSectionName() {
        return "com.legyver.selexml.app";
    }
}
