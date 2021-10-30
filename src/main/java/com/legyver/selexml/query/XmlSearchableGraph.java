package com.legyver.selexml.query;

import com.legyver.selexml.query.from.XmlFromClause;
import com.legyver.selexml.query.select.XmlNodeSelector;
import com.legyver.selexml.query.select.XmlSelectClause;
import com.legyver.selexml.query.where.XmlSelectConditionMatchType;
import com.legyver.selexml.query.where.XmlWhereClause;
import com.legyver.selexml.query.where.XmlWhereCondition;
import com.legyver.utils.graphjxml.XmlGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A searchable graph wrapper for a xml graph
 * This is done so that it can be re-used and not calculated for every search
 */
public class XmlSearchableGraph {
    /**
     * Nodes in the map
     */
    private Map<String, List<ValueNode>> nodes = new HashMap<>();

    /**
     * Construct an indexed searchable graph
     * @param xmlGraph the xml graph
     */
    public XmlSearchableGraph(XmlGraph xmlGraph) {
        addNode(0, xmlGraph);
    }

    private void addNode(long level, XmlGraph graphNode) {
        if (level > 0) {
            //don't add the root node
            String name = graphNode.getName();
            List<ValueNode> valueNodes = nodes.computeIfAbsent(name, x -> new ArrayList<>());
            valueNodes.add(new ValueNode(graphNode));
        }
        for (XmlGraph node : graphNode.getChildren()) {
            addNode(level + 1, node);
        }
    }

    /**
     * Search a XML graph
     * @param criteria the criteria for the search
     * @return the result graph
     */
    public XmlGraph search(XmlGraphSearchCriteria criteria) {
        List<ValueNode> matches = filter(criteria.getWhere());
        //merge all the result nodes into one result graph
        XmlGraph resultGraph = new XmlGraph("result", null, XmlGraph.NodeType.ELEMENT);
        XmlSelectClause selectClause = criteria.getSelect();
        XmlFromClause xmlFromClause = criteria.getFrom();
        List<XmlNodeSelector> selectFrom = xmlFromClause.getFromTypes();
        List<XmlNodeSelector> selectNamed = selectClause.getSelectElementsNamed();
        for (ValueNode match : matches) {
            if (selectFrom.isEmpty() || (selectFrom.size() == 1 && XmlGraphSearchConstants.ALL.equals(selectFrom.iterator().next().getName()))) {
                //get nodes up to but not including the root node (the graph root parent is null)
                XmlGraph parentGraph = findParent(null, match.node, (g) -> g.getParent().getParent() == null);
                resultGraph.accept(parentGraph);
            } else {
                for (XmlNodeSelector namedType : selectFrom) {
                    String named = namedType.getName();
                    XmlGraph.NodeType nodeType = namedType.getNodeType();
                    //get nodes specified
                    XmlGraph parentGraph = findParent(selectNamed, match.node, (g) -> named.equals(g.getName()) && (nodeType == null || g.getNodeType() == nodeType));
                    resultGraph.accept(parentGraph);
                }
            }
        }

        return resultGraph;
    }

    private XmlGraph findParent(List<XmlNodeSelector> selectNamed, XmlGraph childHierarchy, Predicate<XmlGraph> test) {
        XmlGraph parent = childHierarchy.getParent();

        if (selectNamed != null && !selectNamed.isEmpty()) {
            //create a copy of the parent with only the children we care about
            parent = new XmlGraph(parent.getName(), parent.getParent(), parent.getNodeType());
            parent.accept(childHierarchy);
            for (XmlGraph child : parent.getChildren()) {
                for (XmlNodeSelector selector: selectNamed) {
                    if (selector.matches(child)) {
                        parent.accept(child);
                    }
                }
            }
        }

        if (!test.test(parent)) {
            parent = findParent(selectNamed, parent, test);
        }
        return parent;
    }

    private List<ValueNode> filter(XmlWhereClause xmlWhereClause) {
        List<ValueNode> matches = new ArrayList<>();

        for (XmlWhereCondition xmlWhereCondition : xmlWhereClause.getWhereConditions()) {
            XmlNodeSelector xmlNodeSelector = xmlWhereCondition.getXmlNodeSelector();
            String named = xmlNodeSelector.getName();
            XmlGraph.NodeType nodeType = xmlNodeSelector.getNodeType();
            String value = xmlWhereCondition.getValue();
            XmlSelectConditionMatchType xmlSelectOperation = xmlWhereCondition.getMatchCondition();

            List<ValueNode> valueNodes = nodes.get(named);
            if (valueNodes != null) {
                valueNodes = valueNodes.stream()
                        .filter(v -> xmlSelectOperation.matches(v.value, value))
                        .collect(Collectors.toList());
                if (nodeType != null) {
                    valueNodes = valueNodes.stream()
                            .filter(v -> v.node.getNodeType() == nodeType)
                            .collect(Collectors.toList());
                }
                matches.addAll(valueNodes);
            }
        }
        return matches;
    }

    private static class ValueNode {
        private final String value;
        private final XmlGraph node;

        private ValueNode(XmlGraph node) {
            this.value = node.getValue();
            this.node = node;
        }
    }
}
