package com.legyver.selexml.api.query.select;

import com.legyver.utils.graphjxml.XmlGraph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Select node a node based on its name and (optional) type
 */
public class XmlNodeSelector {
    private static final Logger logger = LogManager.getLogger(XmlNodeSelector.class);

    /**
     * The name of the element or attribute to be queried from
     */
    private final String name;
    /**
     * The nullable type (element or attribute).
     * If null, all matching name will be matched
     */
    private final XmlGraph.NodeType nodeType;

    /**
     * Construct a node selector for the name and node type
     * @param name the name of the node
     * @param nodeType the type of the node
     */
    public XmlNodeSelector(String name, XmlGraph.NodeType nodeType) {
        this.name = name;
        this.nodeType = nodeType;
    }

    /**
     * Get the name of the node being selected
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the type of the node being selected
     * @return the type
     */
    public XmlGraph.NodeType getNodeType() {
        return nodeType;
    }

    /**
     * Check if a node matches the specified criteria
     * @param node the node
     * @return true if it matches
     */
    public boolean matches(XmlGraph node) {
        boolean matches =
                ("*".equals(name) || (
                        node.getName() != null && node.getName().equalsIgnoreCase(name)
                )) && (
                        nodeType == null || nodeType == node.getNodeType()
                );
        logger.info("{} type {} matches [{},{}]: {}", node.getName(), node.getNodeType(), name, nodeType, matches);
        return matches;
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
