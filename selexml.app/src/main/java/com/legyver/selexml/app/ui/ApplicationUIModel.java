package com.legyver.selexml.app.ui;

import com.legyver.fenxlib.api.files.FileOptions;
import com.legyver.fenxlib.api.uimodel.RecentFileAware;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ApplicationUIModel implements RecentFileAware {
    private final ObservableList<FileOptions> recentFiles = FXCollections.observableArrayList();
    @Override
    public ObservableList<FileOptions> getRecentFiles() {
        return recentFiles;
    }
}
