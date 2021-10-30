package com.legyver.selexml.query.text;

import com.legyver.selexml.query.XmlGraphSearchConstants;
import com.legyver.selexml.query.XmlGraphSearchCriteria;
import com.legyver.selexml.query.exception.QuerySyntaxException;
import com.legyver.selexml.query.from.XmlFromClause;
import com.legyver.selexml.query.select.XmlNodeSelector;
import com.legyver.selexml.query.select.XmlSelectClause;
import com.legyver.selexml.query.where.XmlSelectConditionMatchType;
import com.legyver.selexml.query.where.XmlWhereClause;
import com.legyver.selexml.query.where.XmlWhereCondition;
import com.legyver.utils.graphjxml.XmlGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SqlSyntaxInterpreter {

    public XmlGraphSearchCriteria parse(String text) throws QuerySyntaxException {
        StringTokenizer stringTokenizer = new StringTokenizer(text);
        if (!stringTokenizer.hasMoreElements()) {
            throw new QuerySyntaxException("Unable to tokenize query: " + text, text);
        }

        XmlSelectClause xmlSelectClause = processSelect(text, stringTokenizer);
        XmlFromClause xmlFromClause = processFrom(text, stringTokenizer);
        XmlWhereClause xmlWhereClause = processWhere(text, stringTokenizer);

        return new XmlGraphSearchCriteria(xmlSelectClause, xmlFromClause, xmlWhereClause);
    }

    private XmlSelectClause processSelect(String text, StringTokenizer stringTokenizer) throws QuerySyntaxException {
        String next = stringTokenizer.nextToken();
        if (!XmlGraphSearchConstants.SELECT.equalsIgnoreCase(next)) {
            throw new QuerySyntaxException("Unknown command [" + next + "] in query: " + text, next);
        }
        List<XmlNodeSelector> selections = new ArrayList<>();
        next = processMulti(selections, stringTokenizer, XmlGraphSearchConstants.FROM);
        if (selections.isEmpty()) {
            throw new QuerySyntaxException("Expected SELECT * or SELECT element or attribute names in query: " + text, next);
        }
        if (!stringTokenizer.hasMoreTokens() || !XmlGraphSearchConstants.FROM.equalsIgnoreCase(next)) {
            throw new QuerySyntaxException("SELECT clause should be followed with FROM keyword: " + text, next);
        }
        return new XmlSelectClause(selections);
    }

    private XmlFromClause processFrom(String text, StringTokenizer stringTokenizer) throws QuerySyntaxException {
        List<XmlNodeSelector> selections = new ArrayList<>();
        String next = processMulti(selections, stringTokenizer, XmlGraphSearchConstants.WHERE);
        if (selections.isEmpty()) {
            throw new QuerySyntaxException("Expected FROM * or FROM element or attribute names in query: " + text, next);
        }
        return new XmlFromClause(selections);
    }

    private XmlWhereClause processWhere(String text, StringTokenizer stringTokenizer) throws QuerySyntaxException {
        List<XmlWhereCondition> selections = new ArrayList<>();

        while (stringTokenizer.hasMoreTokens()) {
            String variableName = stringTokenizer.nextToken();
            if (!stringTokenizer.hasMoreTokens()) {
                throw new QuerySyntaxException("Expected criteria in the format VAR CONDITION VALUE.  Actual: " + text, variableName);
            } else {
                String condition = stringTokenizer.nextToken();
                if (!stringTokenizer.hasMoreTokens()) {
                    throw new QuerySyntaxException("Expected criteria in the format VAR CONDITION VALUE.  Actual: " + text, condition);
                } else {
                    String value = stringTokenizer.nextToken();
                    XmlWhereCondition xmlWhereCondition = parseCondition(text, variableName, condition, value, stringTokenizer);
                    selections.add(xmlWhereCondition);
                }
            }
        }
        return new XmlWhereClause(selections);
    }

    private XmlWhereCondition parseCondition(String text, String variableName, String condition, String value, StringTokenizer stringTokenizer) throws QuerySyntaxException {
        XmlWhereCondition xmlWhereCondition;
        //below to handle cases where clauses are separated with ',' instead of 'and'
        if (value.endsWith(",")) {
            value = value.substring(0, value.length() - 1);
        }
        XmlNodeSelector xmlNodeSelector = parseSelector(variableName);

        if (XmlGraphSearchConstants.IS.equalsIgnoreCase(condition) || XmlGraphSearchConstants.EQUALS_SYMBOL.equals(condition)) {
            if (XmlGraphSearchConstants.NULL.equalsIgnoreCase(value)) {
                xmlWhereCondition = new XmlWhereCondition(xmlNodeSelector, value, XmlSelectConditionMatchType.IS_NULL);
            } else {
                xmlWhereCondition = new XmlWhereCondition(xmlNodeSelector, value, XmlSelectConditionMatchType.IS);
            }
        } else if (XmlGraphSearchConstants.IS_IGNORECASE.equalsIgnoreCase(condition) || XmlGraphSearchConstants.EQUALS_SYMBOL_IGNORE_CASE.equals(condition)) {
            xmlWhereCondition = new XmlWhereCondition(xmlNodeSelector, value, XmlSelectConditionMatchType.IS_IGNORE_CASE);
        } else if (XmlGraphSearchConstants.LIKE.equalsIgnoreCase(condition)) {
            xmlWhereCondition = new XmlWhereCondition(xmlNodeSelector, value, XmlSelectConditionMatchType.LIKE);
        } else if (XmlGraphSearchConstants.LIKE_IGNORECASE.equalsIgnoreCase(condition)) {
            xmlWhereCondition = new XmlWhereCondition(xmlNodeSelector, value, XmlSelectConditionMatchType.LIKE_IGNORE_CASE);
        } else if (XmlGraphSearchConstants.NOT.equalsIgnoreCase(condition) || XmlGraphSearchConstants.NOT_EQUALS_SYMBOL.equals(condition) || XmlGraphSearchConstants.LESS_THAN_GREATER_THAN_SYMBOL.equals(condition)) {
            if (XmlGraphSearchConstants.NULL.equalsIgnoreCase(value)) {
                xmlWhereCondition = new XmlWhereCondition(xmlNodeSelector, value, XmlSelectConditionMatchType.NOT_NULL);
            } else {
                xmlWhereCondition = new XmlWhereCondition(xmlNodeSelector, value, XmlSelectConditionMatchType.NOT);
            }
        } else if (XmlGraphSearchConstants.NOT_IGNORECASE.equalsIgnoreCase(condition) || XmlGraphSearchConstants.NOT_EQUALS_SYMBOL_IGNORE_CASE.equals(condition) || XmlGraphSearchConstants.LESS_THAN_GREATER_THAN_SYMBOL_IGNORE_CASE.equals(condition)) {
            if (XmlGraphSearchConstants.NULL.equalsIgnoreCase(value)) {
                xmlWhereCondition = new XmlWhereCondition(xmlNodeSelector, value, XmlSelectConditionMatchType.NOT_NULL);
            } else {
                xmlWhereCondition = new XmlWhereCondition(xmlNodeSelector, value, XmlSelectConditionMatchType.NOT_IGNORE_CASE);
            }
        } else if ((XmlGraphSearchConstants.AND.equalsIgnoreCase(variableName) || ",".equalsIgnoreCase(variableName)) && stringTokenizer.hasMoreTokens()) {
            //shift right to offset for 'and'
            xmlWhereCondition = parseCondition(text, condition, value, stringTokenizer.nextToken(), stringTokenizer);
        } else if (XmlGraphSearchConstants.OR.equalsIgnoreCase(variableName) && stringTokenizer.hasMoreTokens()) {
            throw new QuerySyntaxException("'OR' keyword not supported at this time", condition);
        } else {
            throw new QuerySyntaxException("unknown condition keyword [" + condition + "] in: " + text, condition);
        }
        return xmlWhereCondition;
    }


    private String processMulti(List<XmlNodeSelector> selections, StringTokenizer stringTokenizer, String stoppingKeyword) {
        String next = stringTokenizer.nextToken();
        if (XmlGraphSearchConstants.ALL.equals(next)) {
            selections.add(new XmlNodeSelector(next, null));
            //we need to read next token to say consistent with else block
            if (stringTokenizer.hasMoreTokens()) {
                next = stringTokenizer.nextToken();
            } else {
                next = null;
            }
        } else {
            next = stripCommasAndAccumulate(next, selections);
            while (!stoppingKeyword.equalsIgnoreCase(next) && stringTokenizer.hasMoreTokens()) {
                next = stringTokenizer.nextToken();
                //we don't want to add the stopping keyword as a value
                if (!stoppingKeyword.equalsIgnoreCase(next)) {
                    next = stripCommasAndAccumulate(next, selections);
                }
            }
        }
        return next;
    }

    private String stripCommasAndAccumulate(String next, List<XmlNodeSelector> selections) {
        if (next != null && next.length() > 0) {
            int indexComma = next.indexOf(',');
            if (indexComma < 0) {
                XmlNodeSelector selector = parseSelector(next);
                if (!selections.contains(selector)) {
                    selections.add(selector);
                }
            } else {
                String prefix = next.substring(0, indexComma);
                next = next.substring(indexComma + 1);
                stripCommasAndAccumulate(prefix, selections);
                next = stripCommasAndAccumulate(next, selections);
            }
        }
        return next;
    }

    private XmlNodeSelector parseSelector(String selector) {
        String named = selector;
        XmlGraph.NodeType nodeType = null;
        if (selector.contains(":")) {
            String[] splits = selector.split(":");
            named =  splits[1];
            String sNodeType = splits[0];
            if (XmlGraphSearchConstants.ELEMENTS_NAMED.equalsIgnoreCase(sNodeType)) {
                nodeType = XmlGraph.NodeType.ELEMENT;
            } else if (XmlGraphSearchConstants.ATTRIBUTES_NAMED.equalsIgnoreCase(sNodeType)) {
                nodeType = XmlGraph.NodeType.ATTRIBUTE;
            }
        }
        return new XmlNodeSelector(named, nodeType);
    }

}
