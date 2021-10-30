package com.legyver.selexml.query.where;

import com.legyver.selexml.query.select.XmlNodeSelector;

/**
 * The where condition of the query
 */
public class XmlWhereCondition {
    /**
     * The selector for the element/attribute to test
     */
    private final XmlNodeSelector xmlNodeSelector;
    /**
     * The value of the element or attribute
     */
    private final String value;
    /**
     * The match condition
     */
    private final XmlSelectConditionMatchType matchCondition;

    /**
     * Construct a where condition
     * @param xmlNodeSelector the type (element or attribute)
     * @param value the target value/pattern of the element or attribute
     * @param matchCondition the match condition
     */
    public XmlWhereCondition(XmlNodeSelector xmlNodeSelector, String value, XmlSelectConditionMatchType matchCondition) {
        this.value = value;
        this.xmlNodeSelector = xmlNodeSelector;
        this.matchCondition = matchCondition;
    }

    /**
     * Get the value of the element or attribute
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the type of the element or attribute
     * @return element or attribute or null
     */
    public XmlNodeSelector getXmlNodeSelector() {
        return xmlNodeSelector;
    }

    /**
     * Get the match condition of the element or attribute
     * @return the match condition
     */
    public XmlSelectConditionMatchType getMatchCondition() {
        return matchCondition;
    }

}
