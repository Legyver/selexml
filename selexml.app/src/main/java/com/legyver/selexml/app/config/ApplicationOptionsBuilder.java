package com.legyver.selexml.app.config;

import com.legyver.fenxlib.api.config.options.ApplicationOptions;
import com.legyver.fenxlib.extensions.tuktukfx.config.TaskLifecycleMixin;

public class ApplicationOptionsBuilder extends ApplicationOptions.Builder<ApplicationOptionsBuilder> implements TaskLifecycleMixin {

    public ApplicationOptionsBuilder() {
        super();
        registerLifecycleHook(shutDownThreadPoolOnExit());
    }
}
