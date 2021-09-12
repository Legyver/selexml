package com.legyver.selexml.task.parse;

import com.legyver.core.exception.CoreException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.util.*;

public class XmlFileReader {
    private static final Logger logger = LogManager.getLogger(XmlFileReader.class);
    private final File file;
    private XMLInputFactory2 xmlif;

    public XmlFileReader(File file) {
        this.file = file;
        try {
            xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
            xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
            xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            xmlif.setProperty(XMLInputFactory.IS_COALESCING, false);
            xmlif.configureForSpeed();
        } catch (Exception ex) {
            logger.error("Error configuring StAX parser", ex);
        }
    }

    public Graph parse() throws CoreException {
        Graph graph = new Graph(null, null);
        graph.file = file;
        XMLStreamReader2 xmlr = null;
        try {
            xmlr = xmlif.createXMLStreamReader(file);
            parse(xmlr, graph);
        } catch (XMLStreamException ex) {
            if (xmlr != null) {
                try {
                    xmlr.closeCompletely();
                } catch (XMLStreamException e) {
                    logger.error("Error closing stream", ex);
                }
            }
            logger.error("Error parsing file: " + file.getAbsolutePath(), ex);
        }
        return graph;
    }

    private void parse(XMLStreamReader2 xmlr, Graph graph) throws XMLStreamException {
        Graph parent = graph;
        while (xmlr.hasNext()) {
            int eventType = xmlr.getEventType();
            switch (eventType) {
                case XMLEvent.START_ELEMENT:
                    String currentElement = xmlr.getPrefixedName();
                    Graph child = new Graph(currentElement, parent);
                    child.context.append("<").append(currentElement);
                    addAttributes(xmlr, child);
                    child.context.append(">");
                    parent.children.put(child.name, child);
                    parent = child;
                    break;
                case XMLEvent.CHARACTERS:
                    String content = xmlr.getText().trim();
                    parent.value = content;
                    parent.context.append(content);
                    break;
                case XMLEvent.END_ELEMENT:
                    if (parent.name != null) {
                        parent.context.append("</").append(parent.name).append(">");
                    }
                    parent = parent.parent;
                    break;
                default: //noop
            }
        }
    }

    private void addAttributes(XMLStreamReader2 xmlr, Graph parent) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        for (int i = 0; i < xmlr.getAttributeCount(); i++) {
            String name = xmlr.getAttributeName(i).toString();
            String value = xmlr.getAttributeValue(i);
            Graph child = new Graph(name, parent);
            child.value = value;
            stringJoiner.add(name + "=" + value);
            parent.children.put(child.name, child);
        }
        parent.context.append(stringJoiner);
    }

    public static class Graph {
        private File file;
        private final String name;
        private final Graph parent;
        private String value;
        private final StringBuilder context = new StringBuilder();
        private final Map<String, Graph> children = new HashMap<>();

        private Graph(String name, Graph parent) {
            this.name = name;
            this.parent = parent;
        }

        public String getName() {
            return name;
        }

        public Graph getParent() {
            return parent;
        }

        public String getValue() {
            return value;
        }

        public StringBuilder getContext() {
            return context;
        }

        public Map<String, Graph> getChildren() {
            return children;
        }

        public File getFile() {
            return file;
        }
    }

}
