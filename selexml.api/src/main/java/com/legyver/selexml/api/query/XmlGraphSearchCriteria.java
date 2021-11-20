package com.legyver.selexml.api.query;


import com.legyver.selexml.api.query.from.XmlFromClause;
import com.legyver.selexml.api.query.select.XmlSelectClause;
import com.legyver.selexml.api.query.where.XmlWhereClause;

/**
 * Criteria for querying a graph
 */
public class XmlGraphSearchCriteria {
    /**
     * The select clause
     * ie:
     * - select course
     * - select *
     * - select course, instructor
     * - select $e:course, $e:instructor, $a:day
     */
    private final XmlSelectClause select;
    /**
     * The from clause
     * ie:
     * - from course
     * - from $e:course
     * - from $a:type
     * - from *
     */
    private final XmlFromClause from;
    /**
     * The where clause
     * ie:
     * - where $e:instructor is 'Kaplan' and $e:day is 'T'
     * - where $a:type is 'disease'
     * - where instructor is 'Kaplan'
     *
     * $e: - only match elements named
     * $a: - only match attributes named
     * When neither is specified, it matches both
     */
    private final XmlWhereClause where;

    /**
     * Construct the criteria for querying a graph
     * @param select the select clause
     * @param from the from clause
     * @param where the where clause
     */
    public XmlGraphSearchCriteria(XmlSelectClause select, XmlFromClause from, XmlWhereClause where) {
        this.select = select;
        this.from = from;
        this.where = where;
    }

    /**
     * Get the select clause
     * @return the select clause
     */
    public XmlSelectClause getSelect() {
        return select;
    }

    /**
     * Get the from clause
     * @return the from clause
     */
    public XmlFromClause getFrom() {
        return from;
    }

    /**
     * Get the where clause
     * @return the where clause
     */
    public XmlWhereClause getWhere() {
        return where;
    }

    /**
     * Builder for constructing XmlGraphSearchCriteria
     */
    public static class Builder {
        private final XmlSelectClause.Builder selectBuilder;
        private final XmlFromClause.Builder fromClauseBuilder;
        private final XmlWhereClause.Builder whereClauseBuilder;


        /**
         * Construct a builder
         */
        public Builder() {
            whereClauseBuilder = new XmlWhereClause.Builder(this);
            fromClauseBuilder = new XmlFromClause.Builder(this, whereClauseBuilder);
            selectBuilder = new XmlSelectClause.Builder(fromClauseBuilder);
        }

        /**
         * Build the criteria
         * @return the criteria
         */
        public XmlGraphSearchCriteria build() {
            XmlSelectClause xmlSelectClause = selectBuilder.build();
            XmlFromClause xmlFromClause = fromClauseBuilder.build();
            XmlWhereClause whereClause = whereClauseBuilder.build();
            return new XmlGraphSearchCriteria(xmlSelectClause, xmlFromClause, whereClause);
        }

        /**
         * Create a select clause
         * @return the select clause builder
         */
        public XmlSelectClause.Builder select() {
            return selectBuilder;
        }

        /**
         * Create a 'select *' clause
         * @return the where clause builder
         */
        public XmlFromClause.Builder selectAll() {
            return fromClauseBuilder;
        }
    }

}
