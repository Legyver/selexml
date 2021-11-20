package com.legyver.selexml.api;

import com.legyver.selexml.api.query.XmlGraphSearchConstants;
import com.legyver.selexml.api.query.XmlGraphSearchCriteria;
import com.legyver.selexml.api.query.from.XmlFromClause;
import com.legyver.selexml.api.query.select.XmlNodeSelector;
import com.legyver.selexml.api.query.select.XmlSelectClause;
import com.legyver.selexml.api.query.where.XmlSelectConditionMatchType;
import com.legyver.selexml.api.query.where.XmlWhereClause;
import com.legyver.selexml.api.query.where.XmlWhereCondition;
import com.legyver.utils.graphjxml.XmlGraph;

import java.util.*;
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
    private final XmlGraph originalGraph;
    private final Map<Integer, Cohesion> levelCohesion = new HashMap<>();
    private final Map<String, Cohesion> termCohesion = new HashMap<>();
    /**
     * Construct an indexed searchable graph
     * @param xmlGraph the xml graph
     */
    public XmlSearchableGraph(XmlGraph xmlGraph) {
        addNode(0, xmlGraph);
        this.originalGraph = xmlGraph;
    }

    private void addNode(int level, XmlGraph graphNode) {
        if (level > 0) {
            //don't add the root node
            String name = graphNode.getName();
            List<ValueNode> valueNodes = nodes.computeIfAbsent(name, x -> new ArrayList<>());
            valueNodes.add(new ValueNode(graphNode));

            //tabulate cohesion data
            {
                Cohesion cohesion = levelCohesion.computeIfAbsent(level, x -> new Cohesion());
                Map<String, Integer> cohesionMap;
                if (graphNode.getNodeType() == XmlGraph.NodeType.ATTRIBUTE) {
                    cohesionMap = cohesion.countByValueAttribute;
                } else {
                    cohesionMap = cohesion.countByValueElement;
                }
                Integer currentScore = cohesionMap.computeIfAbsent(name, x -> 0);
                cohesionMap.put(name, currentScore + 1);
            }
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
        XmlSelectClause selectClause = criteria.getSelect();
        XmlFromClause xmlFromClause = criteria.getFrom();
        List<XmlNodeSelector> selectFrom = xmlFromClause.getXmlNodeSelectors();

        FilterScenario filterScenario = getFilterScenario(criteria, selectFrom);;

        XmlGraph resultGraph;
        if (filterScenario == FilterScenario.ALL_NO_FROM) {
            resultGraph = originalGraph;
        } else if (filterScenario == FilterScenario.ALL_FROM_COHESIVE_ELEMENTS) {
            resultGraph = new XmlGraph("selexml", null, XmlGraph.NodeType.ELEMENT);
            copyNodesToResultGraph(selectClause, selectFrom, resultGraph, originalGraph.getChildren());
        } else if (filterScenario == FilterScenario.ALL_FROM_COHESIVE_ATTRIBUTES) {
            resultGraph = new XmlGraph("selexml", null, XmlGraph.NodeType.ELEMENT);
            copyNodesToResultGraph(selectClause, selectFrom, resultGraph, originalGraph.getChildren());
        } else if (filterScenario == FilterScenario.ALL_FROM_INCOHESIVE) {
            List<ValueNode> matches = filter(criteria.getWhere());
            //merge all the result nodes into one result graph
            resultGraph = new XmlGraph("selexml", null, XmlGraph.NodeType.ELEMENT);
            copyMatchesToResultGraph(selectClause, selectFrom, resultGraph, matches);
        } else if (filterScenario == FilterScenario.SEARCH) {
            List<ValueNode> matches = filter(criteria.getWhere());
            //merge all the result nodes into one result graph
            resultGraph = new XmlGraph("selexml", null, XmlGraph.NodeType.ELEMENT);
            copyMatchesToResultGraph(selectClause, selectFrom, resultGraph, matches);
        } else {
            resultGraph = new XmlGraph("selexml", null, XmlGraph.NodeType.ELEMENT);
        }

        return resultGraph;
    }

    private void copyNodesToResultGraph(XmlSelectClause selectClause, List<XmlNodeSelector> selectFrom, XmlGraph resultGraph, List<XmlGraph> nodes) {
        List<XmlNodeSelector> selectNamed = selectClause.getSelectElementsNamed();
        for (XmlGraph node : nodes) {
            fleshOutParentTree(selectFrom, resultGraph, selectNamed, node);
        }
    }

    private void copyMatchesToResultGraph(XmlSelectClause selectClause, List<XmlNodeSelector> selectFrom, XmlGraph resultGraph, List<ValueNode> matches) {
        List<XmlNodeSelector> selectNamed = selectClause.getSelectElementsNamed();
        for (ValueNode match : matches) {
            XmlGraph node = match.node;
            fleshOutParentTree(selectFrom, resultGraph, selectNamed, node);
        }
    }

    private void fleshOutParentTree(List<XmlNodeSelector> selectFrom, XmlGraph resultGraph, List<XmlNodeSelector> selectNamed, XmlGraph node) {
        if (selectFrom.isEmpty() || (selectFrom.size() == 1 && XmlGraphSearchConstants.ALL.equals(selectFrom.iterator().next().getName()))) {
            //get nodes up to but not including the root node (the graph root parent is null)
            XmlGraph parentGraph = findParent(null, node, (g) -> g.getParent() == null || g.getParent().getParent() == null);
            resultGraph.accept(parentGraph);
        } else {
            for (XmlNodeSelector fromSelector : selectFrom) {
                String named = fromSelector.getName();
                XmlGraph.NodeType nodeType = fromSelector.getNodeType();
                //get nodes specified
                XmlGraph parentGraph = findParent(selectNamed, node, (g) -> named.equals(g.getName()) && (nodeType == null || g.getNodeType() == nodeType));
                resultGraph.accept(parentGraph);
            }
        }
    }

    /**
     * Adding this method to avoid the performance hit of needlessly searching thousands of records
     * @param criteria
     * @param selectFrom
     * @return
     */
    private FilterScenario getFilterScenario(XmlGraphSearchCriteria criteria, List<XmlNodeSelector> selectFrom) {
        FilterScenario filterScenario = FilterScenario.SEARCH;
        if (criteria.getWhere().getWhereConditions().isEmpty()) {
            //instead of searching, since we know we're getting everything start with the top nodes and prune down
            //first check if we're pruning at all

            if (selectFrom.isEmpty()) {
                //base case: select * from *;
                filterScenario = FilterScenario.ALL_NO_FROM;
            } else if (selectFrom.isEmpty() || selectFrom.size() == 1) {
                //check if the one element is '*', ie: select * from *
                XmlNodeSelector selector = selectFrom.iterator().next();
                if (XmlGraphSearchConstants.ALL.equals(selector.getName())) {
                    if (selector.getNodeType() == null) {
                        //select * from *
                        filterScenario = FilterScenario.ALL_NO_FROM;
                    } else if (selector.getNodeType() == XmlGraph.NodeType.ELEMENT) {
                        //select * from $e:*
                        Cohesion cohesion = this.levelCohesion.get(1);
                        if (Cohesion.ALL_ELEMENTS == cohesion.score()) {
                            //all first order nodes are elements
                            filterScenario = FilterScenario.ALL_FROM_COHESIVE_ELEMENTS;
                        } else {
                            filterScenario = FilterScenario.ALL_FROM_INCOHESIVE;
                        }
                    } else if (selector.getNodeType() == XmlGraph.NodeType.ATTRIBUTE) {
                        //select * from $a:*
                        Cohesion cohesion = this.levelCohesion.get(1);
                        if (Cohesion.ALL_ATTRIBUTES == cohesion.score()) {
                            //all first order nodes are attributes
                            filterScenario = FilterScenario.ALL_FROM_COHESIVE_ATTRIBUTES;
                        } else {
                            filterScenario = FilterScenario.ALL_FROM_INCOHESIVE;
                        }
                    }
                } else {
                    filterScenario = FilterScenario.SEARCH;
                }
            } else {
                filterScenario = FilterScenario.SEARCH;
            }
        }
        return filterScenario;
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
//        if (xmlWhereClause.getWhereConditions().isEmpty()) {
//            for (String key : nodes.keySet()) {
//                //just add top-level nodes
//                List<ValueNode> valueNodes = nodes.get(key);
//                for (ValueNode valueNode : valueNodes) {
//                    XmlGraph graph = valueNode.node;
//                    if (graph.getParent() != null && graph.getParent().getParent() == null) {
//                        aggregate(matches, null, key, null, null);
//                    }
//                }
//            }
//        }

        for (XmlWhereCondition xmlWhereCondition : xmlWhereClause.getWhereConditions()) {
            XmlNodeSelector xmlNodeSelector = xmlWhereCondition.getXmlNodeSelector();
            String named = xmlNodeSelector.getName();
            XmlGraph.NodeType nodeType = xmlNodeSelector.getNodeType();
            String value = xmlWhereCondition.getValue();
            XmlSelectConditionMatchType xmlSelectOperation = xmlWhereCondition.getMatchCondition();
            aggregate(matches, nodeType, named, xmlSelectOperation, value);
        }
        return matches;
    }

    private void aggregate(List<ValueNode> matches, XmlGraph.NodeType nodeType, String named, XmlSelectConditionMatchType xmlSelectOperation, String value) {
        List<ValueNode> valueNodes = nodes.get(named);
        if (valueNodes != null) {
            valueNodes = valueNodes.stream()
                    .filter(v -> xmlSelectOperation == null || xmlSelectOperation.matches(v.value, value))
                    .collect(Collectors.toList());
            if (nodeType != null) {
                valueNodes = valueNodes.stream()
                        .filter(v -> v.node.getNodeType() == nodeType)
                        .collect(Collectors.toList());
            }
            matches.addAll(valueNodes);
        }
    }

    private static class ValueNode {
        private final String value;
        private final XmlGraph node;

        private ValueNode(XmlGraph node) {
            this.value = node.getValue();
            this.node = node;
        }
    }

    private static class Cohesion {
        private static int ALL_ELEMENTS = 1;
        private static int ALL_ATTRIBUTES = 2;
        private Map<String, Integer> countByValueElement = new HashMap<>();
        private Map<String, Integer> countByValueAttribute = new HashMap<>();
        private Integer score;

        int score() {
            if (score != null) {
                return score;
            }
            if (countByValueElement.isEmpty() && countByValueAttribute.isEmpty()) {
                score = 0;
            } else if (countByValueElement.size() == 1 && countByValueAttribute.isEmpty()) {
                score = ALL_ELEMENTS;
            } else if (countByValueAttribute.size() == 1 && countByValueElement.isEmpty()) {
                score = ALL_ATTRIBUTES;
            } else if (countByValueElement.size() > 1 || countByValueElement.size() > 1) {
                score = -1;
            } else {
                score = -2;
            }
            countByValueAttribute = null;
            countByValueElement = null;
            return score;
        }
    }

    private enum FilterScenario {
        ALL_NO_FROM,
        ALL_FROM_COHESIVE_ELEMENTS,
        ALL_FROM_COHESIVE_ATTRIBUTES,
        ALL_FROM_INCOHESIVE,
        SEARCH
    }
}
