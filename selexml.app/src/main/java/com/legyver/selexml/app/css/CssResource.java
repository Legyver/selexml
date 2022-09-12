package com.legyver.selexml.app.css;

import java.net.URL;

public class CssResource {

    public static URL getApplicationCSS() {
        return CssResource.class.getResource("application.css");
    }
    public static URL getPopupCSS() {
        return CssResource.class.getResource("popups.css");
    }
}
