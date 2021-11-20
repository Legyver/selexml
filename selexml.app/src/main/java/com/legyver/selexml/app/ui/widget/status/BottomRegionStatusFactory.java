package com.legyver.selexml.app.ui.widget.status;

import com.legyver.fenxlib.core.impl.factory.BottomRegionFactory;

public class BottomRegionStatusFactory extends BottomRegionFactory {

    public BottomRegionStatusFactory() {
        super(new StatusMonitorFactory());
    }
}
