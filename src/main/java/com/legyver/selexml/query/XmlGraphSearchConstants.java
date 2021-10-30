package com.legyver.selexml.query;

/**
 * Constants to be accounted for when queries are typed
 */
public interface XmlGraphSearchConstants {
    //SQL ports
    /**
     * The SQL 'select' keyword
     */
    String SELECT = "select";
    /**
     * The SQL 'from' keyword
     */
    String FROM = "from";
    /**
     * The SQL 'where' keyword
     */
    String WHERE = "where";
    /**
     * The SQL 'and' keyword
     */
    String AND = "and";
    /**
     * The SQL 'or' keyword
     */
    String OR = "or";
    /**
     * The SQL wildcard for all
     */
    String ALL = "*";

    //sql match ports
    /**
     * The SQL '=' condition
     */
    String EQUALS_SYMBOL = "=";
    /**
     * The SQL '!=' condition
     */
    String NOT_EQUALS_SYMBOL = "!=";
    /**
     * The SQL '&gt;' condition
     */
    String GREATER_THAN_SYMBOL = ">";
    /**
     * The SQL '&lt;' condition
     */
    String LESS_THAN_SYMBOL = "<";
    /**
     * The SQL '&lt;&gt;' condition
     */
    String LESS_THAN_GREATER_THAN_SYMBOL = "<>";

    /**
     * The SQL 'is' condition
     */
    String IS = "is";
    /**
     * The SQL 'not' negator
     */
    String NOT = "not";
    /**
     * The SQL 'null' special value;
     */
    String NULL = "null";
    /**
     * The SQL 'is null' condition
     */
    String IS_NULL = IS + " " + NULL;
    /**
     * The SQL 'is not null' condition
     */
    String NOT_NULL = NOT + " " + NULL;
    /**
     * The SQL 'like' condition that supports wild cards
     */
    String LIKE = "like";

    //custom extensions
    /**
     * The SQL 'is' condition extended for case-insensitivity
     */
    String IS_IGNORECASE = "is_";
    /**
     * The SQL 'not' negator extended for case-insensitivity
     */
    String NOT_IGNORECASE = "not_";
    /**
     * The SQL 'like' condition extended for case-insensitivity
     */
    String LIKE_IGNORECASE = "like_";
    /**
     * The SQL '=' condition extended for case-insensitivity
     */
    String EQUALS_SYMBOL_IGNORE_CASE = "=_";
    /**
     * The SQL '!=' condition extended for case-insensitivity
     */
    String NOT_EQUALS_SYMBOL_IGNORE_CASE = "!=_";
    /**
     * The SQL '&lt;&gt;' condition extended for case-insensitivity
     */
    String LESS_THAN_GREATER_THAN_SYMBOL_IGNORE_CASE = "<>_";

    /**
     * Match attributes named
     */
    String ATTRIBUTES_NAMED = "$a";
    /**
     * Match elements named
     */
    String ELEMENTS_NAMED = "$e";

}
