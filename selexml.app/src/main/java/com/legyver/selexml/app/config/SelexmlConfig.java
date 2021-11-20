package com.legyver.selexml.config;

import com.legyver.fenxlib.core.impl.config.JsonApplicationConfig;

import java.util.Map;

public class SelexmlConfig extends JsonApplicationConfig {
    /**
     * Construct an Application Config the will marshall the config to-from JSON
     *
     * @param map the map of values to save
     */
    public SelexmlConfig(Map map) {
        super(map);
    }
}
