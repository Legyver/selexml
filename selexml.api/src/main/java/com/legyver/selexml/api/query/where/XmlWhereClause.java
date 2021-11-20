package com.legyver.selexml.api.query.where;

import com.legyver.selexml.api.query.XmlGraphSearchCriteria;
import com.legyver.selexml.api.query.select.XmlNodeSelector;
import com.legyver.utils.graphjxml.XmlGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The where clause of an XML query
 */
public class XmlWhereClause {
    /**
     * List of where conditions to match.
     * Note: these are AND-joined so if multiple conditions are passed, all conditions must match
     */
    private final List<XmlWhereCondition> whereConditions;

    /**
     * Construct a where clause with the specified conditions
     * @param whereConditions the conditions to use
     */
    public XmlWhereClause(List<XmlWhereCondition> whereConditions) {
        this.whereConditions = whereConditions;
    }

    /**
     * Get the where conditions for the clause
     * @return the conditions
     */
    public List<XmlWhereCondition> getWhereConditions() {
        return whereConditions;
    }

    /**
     * Builder for constructing the where clause
     */
    public static class Builder {
        private final List<WhereOperationBuilder> whereBuilders = new ArrayList<>();
        private final XmlGraphSearchCriteria.Builder builder;

        /**
         * Construct an XML where clause Builder
         * @param builder the parent builder
         */
        public Builder(XmlGraphSearchCriteria.Builder builder) {
            this.builder = builder;
        }

        /**
         * Build the where clause
         * @return the where clause
         */
        public XmlWhereClause build() {
            List<XmlWhereCondition> whereMatches = whereBuilders.stream().map(WhereOperationBuilder::buildInternal).collect(Collectors.toList());
            return new XmlWhereClause(whereMatches);
        }

        /**
         * Transition to a where operation builder for an element or attribute named the specified value
         * @param named the name of the element or attribute
         * @return the where operation builder
         */
        public WhereOperationBuilder whereElementOrAttributeNamed(String named) {
            WhereOperationBuilder whereOperationBuilder = new WhereOperationBuilder(this, named);
            whereBuilders.add(whereOperationBuilder);
            return whereOperationBuilder;
        }

        /**
         * Transition to a where operation builder for an element named the specified value
         * @param named the name of the element
         * @return the where operation builder
         */
        public WhereOperationBuilder whereElementNamed(String named) {
            WhereOperationBuilder whereOperationBuilder = new WhereOperationBuilder(this, named, XmlGraph.NodeType.ELEMENT);
            whereBuilders.add(whereOperationBuilder);
            return whereOperationBuilder;
        }

        /**
         * Transition to a where operation builder for an attribute named the specified value
         * @param named the name of the attribute
         * @return the where operation builder
         */
        public WhereOperationBuilder whereAttributeNamed(String named) {
            WhereOperationBuilder whereOperationBuilder = new WhereOperationBuilder(this, named, XmlGraph.NodeType.ATTRIBUTE);
            whereBuilders.add(whereOperationBuilder);
            return whereOperationBuilder;
        }
    }

    /**
     * Builder for constructing the criteria part of the where clause
     */
    public static class WhereOperationBuilder {
        private final Builder whereBuilder;
        private final String name;
        private String value;
        private XmlGraph.NodeType type;
        private XmlSelectConditionMatchType operation;

        /**
         * Construct a Builder for constructing the criteria part of the where clause
         * @param whereBuilder the parent builder
         * @param named the name of the element or attribute
         * @param nodeType the nullable type (element or attribute)
         */
        public WhereOperationBuilder(Builder whereBuilder, String named, XmlGraph.NodeType nodeType) {
            this.whereBuilder = whereBuilder;
            this.name = named;
            this.type = nodeType;
        }

        /**
         * Construct a Builder for constructing the criteria part of the where clause
         * @param whereBuilder the parent builder
         * @param named the name of the element or attribute
         */
        public WhereOperationBuilder(Builder whereBuilder, String named) {
            this(whereBuilder, named, null);
        }

        private XmlWhereCondition buildInternal() {
            return new XmlWhereCondition(new XmlNodeSelector(name, type), value, operation);
        }

        /**
         * The condition for the match loosely mapping to the SQL 'is' condition
         * @param value the value to match
         * @return  a builder to append another where clause or build the existing
         */
        public ConjunctiveWhereBuilder is(String value) {
            return operationValue(XmlSelectConditionMatchType.IS, value);
        }

        /**
         * The condition for the match loosely mapping to the SQL 'like' condition
         * @param pattern the sql pattern to match
         * @return  a builder to append another where clause or build the existing
         */
        public ConjunctiveWhereBuilder like(String pattern) {
            return operationValue(XmlSelectConditionMatchType.LIKE, pattern);
        }

        /**
         * The condition for the match loosely mapping to the SQL 'is null' condition
         * @return a builder to append another where clause or build the existing
         */
        public ConjunctiveWhereBuilder isNull() {
            return operationValue(XmlSelectConditionMatchType.IS_NULL, null);
        }

        /**
         * The condition for the match loosely mapping to the SQL 'is not null' condition
         * @return  a builder to append another where clause or build the existing
         */
        public ConjunctiveWhereBuilder isNotNull() {
            return operationValue(XmlSelectConditionMatchType.NOT_NULL, null);
        }

        private ConjunctiveWhereBuilder operationValue(XmlSelectConditionMatchType operation, String value) {
            this.operation = operation;
            this.value = value;
            return new ConjunctiveWhereBuilder(whereBuilder);
        }
    }

    /**
     * Builder to append an additional where clause or build the expression
     */
    public static class ConjunctiveWhereBuilder {
        private final Builder whereBuilder;

        /**
         * Construct a builder that can append where clauses
         * @param whereBuilder the parent where clause builder
         */
        public ConjunctiveWhereBuilder(Builder whereBuilder) {
            this.whereBuilder = whereBuilder;
        }

        /**
         * Add another clause for the element or attribute named the specified value
         * @param named the name of the element or attribute
         * @return a new where operation builder
         */
        public WhereOperationBuilder andElementOrAttributeNamed(String named) {
            return whereBuilder.whereElementOrAttributeNamed(named);
        }

        /**
         * Add another clause for the element named the specified value
         * @param named the name of the element
         * @return a new where operation builder
         */
        public WhereOperationBuilder andElementNamed(String named) {
            return whereBuilder.whereElementNamed(named);
        }

        /**
         * Add another clause for the attribute named the specified value
         * @param named the name of the attribute
         * @return a new where operation builder
         */
        public WhereOperationBuilder andAttributeNamed(String named) {
            return whereBuilder.whereAttributeNamed(named);
        }

        /**
         * Construct the XMLGraph Search Criteria
         * @return the search criteria
         */
        public XmlGraphSearchCriteria build() {
            return whereBuilder.builder.build();
        }

    }
}
