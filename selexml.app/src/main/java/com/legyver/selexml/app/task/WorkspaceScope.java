package com.legyver.selexml.app.task;

import com.legyver.core.exception.CoreException;
import com.legyver.selexml.api.query.XmlGraphSearchCriteria;
import com.legyver.selexml.api.XmlSearchableGraph;
import com.legyver.utils.graphjxml.XmlGraph;
import com.legyver.utils.graphjxml.reader.GraphXmlReader;
import com.legyver.utils.graphjxml.reader.XmlFileReader;
import com.legyver.utils.graphjxml.writer.GraphXmlWriter;
import com.legyver.utils.graphjxml.writer.XmlFileWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class WorkspaceScope {
    private final List<File> filesInScope;
    private final XmlFileReader xmlFileReader = new XmlFileReader(new GraphXmlReader());
    private XmlGraph mainGraph;
    private XmlSearchableGraph searchableGraph;

    public WorkspaceScope(List<File> filesInScope) {
        this.filesInScope = filesInScope;
    }

    public void init() {
        if (mainGraph == null) {
            mainGraph = new XmlGraph(null, null, XmlGraph.NodeType.ELEMENT);
            for (File file : filesInScope) {
                XmlGraph graph = xmlFileReader.parse(file);
                mainGraph.accept(graph);
            }
        }
        if (searchableGraph == null) {
            searchableGraph = new XmlSearchableGraph(mainGraph);
        }
    }

    public int getSize() {
        return filesInScope.size();
    }

    public SearchResult search(XmlGraphSearchCriteria criteria) throws CoreException {
        XmlGraph graph = searchableGraph.search(criteria);

        try {
            Path path = Files.createTempFile("select" + Math.random(), ".xml");
            File file = path.toFile();
            file.deleteOnExit();
            new XmlFileWriter(new GraphXmlWriter()).writeToFile(graph, file);
            return new SearchResult(file, graph.getChildren().size());
        } catch (IOException e) {
            throw new CoreException(e);
        }
    }

    public static class SearchResult {
        private final File resultFile;
        private final int size;

        public SearchResult(File resultFile, int size) {
            this.resultFile = resultFile;
            this.size = size;
        }

        public File getResultFile() {
            return resultFile;
        }

        public int getSize() {
            return size;
        }
    }
}
