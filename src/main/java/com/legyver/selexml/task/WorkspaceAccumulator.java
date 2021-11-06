package com.legyver.selexml.task;

import com.legyver.fenxlib.widgets.filetree.nodes.FileReference;
import com.legyver.fenxlib.widgets.filetree.nodes.INodeReference;
import com.legyver.fenxlib.widgets.filetree.tree.FileTreeItem;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WorkspaceAccumulator {
    private final SuffixFileFilter suffixFileFilter;

    public WorkspaceAccumulator(SuffixFileFilter suffixFileFilter) {
        this.suffixFileFilter = suffixFileFilter;
    }

    public List<File> filter(FileTreeItem fileTreeItem) {
        List<File> filesInScope = new ArrayList<>();
        accumulateRecursive(filesInScope, fileTreeItem);
        return filesInScope;
    }

    private void accumulateRecursive(List<File> filesInScope, FileTreeItem fileTreeItem) {
        INodeReference iNodeReference = fileTreeItem.getNodeReference();
        if (iNodeReference instanceof FileReference) {
            File file = ((FileReference) iNodeReference).getFile();
            if (file.isDirectory()) {
                List<FileTreeItem> children = fileTreeItem.getChildren();
                for (FileTreeItem child: children) {
                    accumulateRecursive(filesInScope, child);
                }
            } else {
                if (suffixFileFilter.accept(file)) {
                    filesInScope.add(file);
                }
            }
        }
    }
}
