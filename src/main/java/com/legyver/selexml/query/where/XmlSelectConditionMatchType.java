package com.legyver.selexml.query.where;

import com.legyver.selexml.query.where.comparator.LikeComparator;

/**
 * Supported select operations mapping loosely to SQL conditions.
 *
 * Note: These are all three part matches
 * variableName + conditionType + value
 * ie:
 * - variable is null
 * - variable is value
 * - variable isic value
 * - variable not null
 * - variable like value
 */
public enum XmlSelectConditionMatchType {
    /**
     * The SQL 'is' condition
     */
    IS {
        @Override
        public boolean matches(String actual, String target) {
            return target != null && target.equals(actual);
        }
    },
    /**
     * Extension of 'is' that ignores case
     */
    IS_IGNORE_CASE {
        @Override
        public boolean matches(String actual, String target) {
            return target != null && target.equals(actual);
        }
    },
    /**
     * The SQL 'is null' condition
     */
    IS_NULL {
        @Override
        public boolean matches(String actual, String target) {
            return actual == null;
        }
    },
    /**
     * The SQL 'not null' condition
     */
    NOT_NULL {
        @Override
        public boolean matches(String actual, String target) {
            return actual != null;
        }
    },
    /**
     * The SQL 'not' &lt;value&gt; condition
     */
    NOT {
        @Override
        public boolean matches(String actual, String target) {
            if (actual == null) {
                return false;
            }
            return actual.equals(target);
        }
    },
    /**
     * The SQL 'not' &lt;value&gt; condition extended case-insensitivity
     */
    NOT_IGNORE_CASE {
        @Override
        public boolean matches(String actual, String target) {
            if (actual == null) {
                return false;
            }
            return !actual.equalsIgnoreCase(target);
        }
    },
    /**
     * The SQL 'like' wildcard condition
     */
    LIKE {
        @Override
        public boolean matches(String actual, String target) {
            return new LikeComparator(target, false).matches(actual);
        }
    },
    /**
     * The SQL 'like' wildcard condition
     */
    LIKE_IGNORE_CASE {
        @Override
        public boolean matches(String actual, String target) {
            return new LikeComparator(target, false).matches(actual);
        }
    };

    /**
     * Check if a condition matches
     * @param actual the actual found value
     * @param target the expected value
     * @return true if the condition matches
     */
    public abstract boolean matches(String actual, String target);
}
