package com.legyver.selexml.query.from;

import com.legyver.selexml.query.XmlGraphSearchCriteria;
import com.legyver.selexml.query.select.XmlNodeSelector;
import com.legyver.selexml.query.where.XmlWhereClause;
import com.legyver.utils.graphjxml.XmlGraph;

import java.util.ArrayList;
import java.util.List;

public class XmlFromClause {
    private final List<XmlNodeSelector> fromTypes;

    public XmlFromClause(List<XmlNodeSelector> fromTypes) {
        this.fromTypes = fromTypes;
    }

    public List<XmlNodeSelector> getFromTypes() {
        return fromTypes;
    }

    public static class Builder {
        private final XmlGraphSearchCriteria.Builder builder;
        private final XmlWhereClause.Builder whereClauseBuilder;
        private List<XmlNodeSelector> from = new ArrayList<>();

        public Builder(XmlGraphSearchCriteria.Builder builder, XmlWhereClause.Builder whereClauseBuilder) {
            this.builder = builder;
            this.whereClauseBuilder = whereClauseBuilder;
        }

        public XmlFromClause build() {
            return new XmlFromClause(from);
        }

        public Builder fromElementNamed(String from) {
            this.from.add(new XmlNodeSelector(from, XmlGraph.NodeType.ELEMENT));
            return this;
        }

        public Builder fromAttributeNamed(String from) {
            this.from.add(new XmlNodeSelector(from, XmlGraph.NodeType.ATTRIBUTE));
            return this;
        }

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

    }
}
