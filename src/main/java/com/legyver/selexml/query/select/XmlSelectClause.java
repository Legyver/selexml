package com.legyver.selexml.query.select;

import com.legyver.selexml.query.from.XmlFromClause;
import com.legyver.utils.graphjxml.XmlGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Select clause for searching an XML graph
 */
public class XmlSelectClause {
    /**
     * The names of elements to be selected
     */
    private final List<XmlNodeSelector> selectElementsNamed;

    /**
     * Construct a select clause for searching an XML graph
     * @param selectElementsNamed the elements to search for
     */
    public XmlSelectClause(List<XmlNodeSelector> selectElementsNamed) {
        this.selectElementsNamed = selectElementsNamed;
    }

    /**
     * Get the list of element names to search for
     * @return the list of element names to search for
     */
    public List<XmlNodeSelector> getSelectElementsNamed() {
        return selectElementsNamed;
    }

    /**
     * Builder for constructing an XmlSelectClause
     */
    public static class Builder {
        private final XmlFromClause.Builder builder;
        private final List<XmlNodeSelector> elementNames = new ArrayList<>();

        /**
         * Construct a builder
         * @param builder the parent builder
         */
        public Builder(XmlFromClause.Builder builder) {
            this.builder = builder;
        }

        /**
         * Build the select clause
         * @return the XML select clause
         */
        public XmlSelectClause build() {
            return new XmlSelectClause(elementNames);
        }

        /**
         * Select an elementNamed
         * @param elementName the name of the element to select
         * @return this builder
         */
        public Builder elementNamed(String elementName) {
            this.elementNames.add(new XmlNodeSelector(elementName, XmlGraph.NodeType.ELEMENT));
            return this;
        }

        public XmlFromClause.Builder fromAnyNamed(String from){
            return builder.fromAnyNamed(from);
        }
        public XmlFromClause.Builder fromElementNamed(String from){
            return builder.fromElementNamed(from);
        }
        public XmlFromClause.Builder fromAttributeNamed(String from){
            return builder.fromAttributeNamed(from);
        }




    }
}
