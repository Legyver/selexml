package com.legyver.selexml.ui.widget.status;

import com.legyver.fenxlib.core.impl.factory.BottomRegionFactory;

public class BottomRegionStatusFactory extends BottomRegionFactory {

    public BottomRegionStatusFactory() {
        super(new StatusMonitorFactory());
    }
}
