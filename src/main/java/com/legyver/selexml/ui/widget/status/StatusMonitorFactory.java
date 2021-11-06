package com.legyver.selexml.ui.widget.status;

import com.legyver.core.exception.CoreException;
import com.legyver.fenxlib.core.api.factory.AlignedNodeFactory;
import com.legyver.fenxlib.core.api.locator.LocationContext;
import com.legyver.fenxlib.core.impl.context.ApplicationContext;

public class StatusMonitorFactory implements AlignedNodeFactory<StatusMonitor> {
    @Override
    public StatusMonitor makeNode(LocationContext locationContext) throws CoreException {
        StatusMonitor statusMonitor = new StatusMonitor();
        ApplicationContext.getComponentRegistry().register(locationContext, statusMonitor);
        return statusMonitor;
    }

    @Override
    public EnqueueAlignment getAlignment() {
        return EnqueueAlignment.RIGHT;
    }
}
