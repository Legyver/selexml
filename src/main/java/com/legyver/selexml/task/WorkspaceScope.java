package com.legyver.selexml.task;

import com.legyver.utils.graphjxml.XmlGraph;
import com.legyver.utils.graphjxml.reader.GraphXmlReader;
import com.legyver.utils.graphjxml.reader.XmlFileReader;

import java.io.File;
import java.util.List;

public class WorkspaceScope {
    private final List<File> filesInScope;
    private final XmlFileReader xmlFileReader = new XmlFileReader(new GraphXmlReader());
    private XmlGraph mainGraph;

    public WorkspaceScope(List<File> filesInScope) {
        this.filesInScope = filesInScope;
    }

    public void init() {
        mainGraph = new XmlGraph(null, null,  null);
        for (File file : filesInScope) {
            XmlGraph graph = xmlFileReader.parse(file);
            mainGraph.accept(graph.pop());
        }
    }

    public int getSize() {
        return filesInScope.size();
    }
}
