package com.legyver.selexml.query.select;

import com.legyver.utils.graphjxml.XmlGraph;

import java.util.Objects;

public class XmlNodeSelector {
    /**
     * The name of the element or attribute to be queried from
     */
    private final String name;
    /**
     * The nullable type (element or attribute).
     * If null, all matching name will be matched
     */
    private final XmlGraph.NodeType nodeType;

    public XmlNodeSelector(String name, XmlGraph.NodeType nodeType) {
        this.name = name;
        this.nodeType = nodeType;
    }

    public String getName() {
        return name;
    }

    public XmlGraph.NodeType getNodeType() {
        return nodeType;
    }

    public boolean matches(XmlGraph node) {
        return node.getName() != null && node.getName().equalsIgnoreCase(name) && (nodeType == null || nodeType == node.getNodeType());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XmlNodeSelector that = (XmlNodeSelector) o;
        return name.equals(that.name) && nodeType == that.nodeType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, nodeType);
    }

}
