package com.legyver.selexml.api.query.from;

import com.legyver.selexml.api.query.XmlGraphSearchCriteria;
import com.legyver.selexml.api.query.select.XmlNodeSelector;
import com.legyver.selexml.api.query.where.XmlWhereClause;
import com.legyver.utils.graphjxml.XmlGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the from clause of a select statement.
 */
public class XmlFromClause {
    private final List<XmlNodeSelector> xmlNodeSelectors;

    /**
     * Construct a from clause with the specified connectors
     * @param xmlNodeSelectors the selectors for the from clause
     */
    public XmlFromClause(List<XmlNodeSelector> xmlNodeSelectors) {
        this.xmlNodeSelectors = xmlNodeSelectors;
    }

    /**
     * Get the selectors for the from clause
     * @return the from selectors
     */
    public List<XmlNodeSelector> getXmlNodeSelectors() {
        return xmlNodeSelectors;
    }

    /**
     * Builder to construct a from clause
     */
    public static class Builder {
        private final XmlGraphSearchCriteria.Builder builder;
        private final XmlWhereClause.Builder whereClauseBuilder;
        private List<XmlNodeSelector> from = new ArrayList<>();

        /**
         * Construct a builder for a from clause
         * @param builder the parent criteria builder
         * @param whereClauseBuilder the where clause builder to transition to on 'where'
         */
        public Builder(XmlGraphSearchCriteria.Builder builder, XmlWhereClause.Builder whereClauseBuilder) {
            this.builder = builder;
            this.whereClauseBuilder = whereClauseBuilder;
        }

        /**
         * Build the from clause
         * @return the from clause
         */
        public XmlFromClause build() {
            return new XmlFromClause(from);
        }

        /**
         * Build a query without a where clause
         * @return query criteria
         */
        public XmlGraphSearchCriteria buildNoWhere() {
            return builder.build();
        }

        /**
         * Will query only elements for the provided text.
         * @param from the name of the element
         * @return the from query builder
         */
        public Builder fromElementNamed(String from) {
            this.from.add(new XmlNodeSelector(from, XmlGraph.NodeType.ELEMENT));
            return this;
        }

        /**
         * Will query only attributes  for the provided text.
         * @param from the name of the attribute
         * @return the from query builder
         */
        public Builder fromAttributeNamed(String from) {
            this.from.add(new XmlNodeSelector(from, XmlGraph.NodeType.ATTRIBUTE));
            return this;
        }

        /**
         * Will query both elements and attributes for the provided text.
         * @param from the name of the attribute/element
         * @return the from query builder
         */
        public Builder fromAnyNamed(String from) {
            this.from.add(new XmlNodeSelector(from, null));
            return this;
        }

        /**
         * Transition to the where clause builder for an element or attribute named
         * @param named the name of the element or attribute
         * @return the XmlWhereClause builder
         */
        public XmlWhereClause.WhereOperationBuilder whereElementOrAttributeNamed(String named) {
            return whereClauseBuilder.whereElementOrAttributeNamed(named);
        }

        /**
         * Transition to the where clause builder for an element named
         * @param named the name of the element
         * @return the XmlWhereClause builder
         */
        public XmlWhereClause.WhereOperationBuilder whereElementNamed(String named) {
            return whereClauseBuilder.whereElementNamed(named);
        }

        /**
         * Transition to the where clause builder for an attribute named
         * @param named the name of the attribute
         * @return the XmlWhereClause builder
         */
        public XmlWhereClause.WhereOperationBuilder whereAttributeNamed(String named) {
            return whereClauseBuilder.whereAttributeNamed(named);
        }

        /**
         * Transition to the where clause builder for an attribute named
         * @param named the name of the attribute
         * @return the XmlWhereClause builder
         */
        public XmlWhereClause.WhereOperationBuilder whereAnyNamed(String named) {
            return whereClauseBuilder.whereAnyNamed(named);
        }



    }
}
