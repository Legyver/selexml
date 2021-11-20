package com.legyver.selexml.api.query.text;

import com.legyver.core.function.ThrowingConsumer;
import com.legyver.selexml.api.query.XmlGraphSearchConstants;
import com.legyver.selexml.api.query.XmlGraphSearchCriteria;
import com.legyver.selexml.api.query.from.XmlFromClause;
import com.legyver.selexml.api.query.select.XmlNodeSelector;
import com.legyver.selexml.api.query.select.XmlSelectClause;
import com.legyver.selexml.api.query.where.XmlSelectConditionMatchType;
import com.legyver.selexml.api.query.where.XmlWhereClause;
import com.legyver.selexml.api.query.where.XmlWhereCondition;
import com.legyver.utils.graphjxml.XmlGraph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SqlSyntaxInterpreterTest {
    private final SqlSyntaxInterpreter sqlSyntaxInterpreter = new SqlSyntaxInterpreter();

    private void runTest(String clause, ThrowingConsumer<XmlGraphSearchCriteria> test) throws Exception {
        XmlGraphSearchCriteria xmlGraphSearchCriteria = sqlSyntaxInterpreter.parse(clause);
        test.accept(xmlGraphSearchCriteria);
    }

    private void runUpperAndLower(String clause, ThrowingConsumer<XmlGraphSearchCriteria> test) throws Exception {
        runTest(clause, test);
        runTest(clause.toUpperCase(), test);
    }

    @Test
    public void selectAllFromAllNoWhere() throws Exception {
        ThrowingConsumer<XmlGraphSearchCriteria> test = (xmlGraphSearchCriteria) -> {
            assertSelectAllFromAll(xmlGraphSearchCriteria);

            XmlWhereClause xmlWhereClause = xmlGraphSearchCriteria.getWhere();
            WhereAsserter whereAsserter = new WhereAsserter();
            whereAsserter.doAssert(xmlWhereClause);
        };
        runUpperAndLower("select * from *", test);
        runUpperAndLower("select * from *;", test);
    }

    @Test
    public void selectAllFromAllSimpleWhere() throws Exception {
        //equals
        ThrowingConsumer<XmlGraphSearchCriteria> testEq = (xmlGraphSearchCriteria) -> {
            assertSelectAllFromAll(xmlGraphSearchCriteria);

            WhereAsserter whereAsserter = new WhereAsserter(new WhereConditionAsserter(null,
                    "instructor", XmlSelectConditionMatchType.IS, "Kaplan"));
            whereAsserter.doAssert(xmlGraphSearchCriteria.getWhere());
        };
        runUpperAndLower("select * from * where instructor = Kaplan", testEq);
        runUpperAndLower("select * from * where instructor is Kaplan", testEq);
        runUpperAndLower("select * from * where instructor is Kaplan;", testEq);

        //equals comma-delimited
        ThrowingConsumer<XmlGraphSearchCriteria> testCommaDelimited = (xmlGraphSearchCriteria) -> {
            assertSelectAllFromAll(xmlGraphSearchCriteria);

            WhereAsserter whereAsserter = new WhereAsserter(new WhereConditionAsserter(null,
                    "day", XmlSelectConditionMatchType.IS, "T,W"));
            whereAsserter.doAssert(xmlGraphSearchCriteria.getWhere());
        };

        //equals ignore case
        ThrowingConsumer<XmlGraphSearchCriteria> testEIC = (xmlGraphSearchCriteria) -> {
            assertSelectAllFromAll(xmlGraphSearchCriteria);

            WhereAsserter whereAsserter = new WhereAsserter(new WhereConditionAsserter(null,
                    "instructor", XmlSelectConditionMatchType.IS_IGNORE_CASE, "Kaplan"));
            whereAsserter.doAssert(xmlGraphSearchCriteria.getWhere());
        };
        runUpperAndLower("select * from * where instructor =_ Kaplan", testEIC);
        runUpperAndLower("select * from * where instructor is_ Kaplan", testEIC);

        //not equals
        ThrowingConsumer<XmlGraphSearchCriteria> testNeq = (xmlGraphSearchCriteria) -> {
            assertSelectAllFromAll(xmlGraphSearchCriteria);

            WhereAsserter whereAsserter = new WhereAsserter(new WhereConditionAsserter(null,
                    "instructor", XmlSelectConditionMatchType.NOT, "Kaplan"));
            whereAsserter.doAssert(xmlGraphSearchCriteria.getWhere());
        };
        runUpperAndLower("select * from * where instructor not Kaplan", testNeq);
        runUpperAndLower("select * from * where instructor != Kaplan", testNeq);
        runUpperAndLower("select * from * where instructor <> Kaplan", testNeq);

        //not equals ignore case
        ThrowingConsumer<XmlGraphSearchCriteria> testNEIC = (xmlGraphSearchCriteria) -> {
            assertSelectAllFromAll(xmlGraphSearchCriteria);

            WhereAsserter whereAsserter = new WhereAsserter(new WhereConditionAsserter(null,
                    "instructor", XmlSelectConditionMatchType.NOT_IGNORE_CASE, "Kaplan"));
            whereAsserter.doAssert(xmlGraphSearchCriteria.getWhere());
        };
        runUpperAndLower("select * from * where instructor not_ Kaplan", testNEIC);
        runUpperAndLower("select * from * where instructor !=_ Kaplan", testNEIC);
        runUpperAndLower("select * from * where instructor <>_ Kaplan", testNEIC);

        //not null
        ThrowingConsumer<XmlGraphSearchCriteria> testNotNull = (xmlGraphSearchCriteria) -> {
            assertSelectAllFromAll(xmlGraphSearchCriteria);

            WhereAsserter whereAsserter = new WhereAsserter(new WhereConditionAsserter(null,
                    "instructor", XmlSelectConditionMatchType.NOT_NULL, "null"));
            whereAsserter.doAssert(xmlGraphSearchCriteria.getWhere());
        };
        runUpperAndLower("select * from * where instructor not null", testNotNull);

        //is null
        ThrowingConsumer<XmlGraphSearchCriteria> testNull = (xmlGraphSearchCriteria) -> {
            assertSelectAllFromAll(xmlGraphSearchCriteria);

            WhereAsserter whereAsserter = new WhereAsserter(new WhereConditionAsserter(null,
                    "instructor", XmlSelectConditionMatchType.IS_NULL, "null"));
            whereAsserter.doAssert(xmlGraphSearchCriteria.getWhere());
        };
        runUpperAndLower("select * from * where instructor is null", testNull);

        //like
        ThrowingConsumer<XmlGraphSearchCriteria> testLike = (xmlGraphSearchCriteria) -> {
            assertSelectAllFromAll(xmlGraphSearchCriteria);

            WhereAsserter whereAsserter = new WhereAsserter(new WhereConditionAsserter(null,
                    "instructor", XmlSelectConditionMatchType.LIKE, "_k%"));
            whereAsserter.doAssert(xmlGraphSearchCriteria.getWhere());
        };
        runUpperAndLower("select * from * where instructor like _k%", testLike);

        //like ignore case
        ThrowingConsumer<XmlGraphSearchCriteria> testLikeIgnoreCase = (xmlGraphSearchCriteria) -> {
            assertSelectAllFromAll(xmlGraphSearchCriteria);

            WhereAsserter whereAsserter = new WhereAsserter(new WhereConditionAsserter(null,
                    "instructor", XmlSelectConditionMatchType.LIKE_IGNORE_CASE, "_k%"));
            whereAsserter.doAssert(xmlGraphSearchCriteria.getWhere());
        };
        runUpperAndLower("select * from * where instructor like_ _k%", testLikeIgnoreCase);
    }


    @Test
    public void selectAllFromAllComplexWhere() throws Exception {
        ThrowingConsumer<XmlGraphSearchCriteria> test = (xmlGraphSearchCriteria) -> {
            assertSelectAllFromAll(xmlGraphSearchCriteria);

            WhereAsserter whereAsserter = new WhereAsserter(
                    new WhereConditionAsserter(null, "instructor", XmlSelectConditionMatchType.IS, "Kaplan"),
                    new WhereConditionAsserter(null, "day", XmlSelectConditionMatchType.IS_IGNORE_CASE,"T"));
            whereAsserter.doAssert(xmlGraphSearchCriteria.getWhere());
        };
        //only intersections supported at this time
        runUpperAndLower("select * from * where instructor is Kaplan and day is_ T", test);
        runUpperAndLower("select * from * where instructor is Kaplan, day is_ T", test);
        runUpperAndLower("select * from * where instructor is Kaplan , day is_ T", test);
    }

     @Test
    public void selectSpecifiedFromAllNoWhere() throws Exception {
        //single value in the select clause
        ThrowingConsumer<XmlGraphSearchCriteria> test = (criteria) -> {
            SelectAsserter selectAsserter = new SelectAsserter();
            selectAsserter.doAssert(criteria.getSelect(), null, "course");

            FromAsserter fromAsserter = new FromAsserter();
            fromAsserter.doAssert(criteria.getFrom(), null, XmlGraphSearchConstants.ALL);

            WhereAsserter whereAsserter = new WhereAsserter();
            whereAsserter.doAssert(criteria.getWhere());
        };
        runUpperAndLower("select course from *", test);

        //multiple values in the select clause
        ThrowingConsumer<XmlGraphSearchCriteria> testMulti = (criteria) -> {
            SelectAsserter selectAsserter = new SelectAsserter(2);
            selectAsserter.doAssert(criteria.getSelect(),0, null, "course");
            selectAsserter.doAssert(criteria.getSelect(), 1,null, "day");

            FromAsserter fromAsserter = new FromAsserter();
            fromAsserter.doAssert(criteria.getFrom(), null, XmlGraphSearchConstants.ALL);

            WhereAsserter whereAsserter = new WhereAsserter();
            whereAsserter.doAssert(criteria.getWhere());
        };
        runUpperAndLower("select course, day from *", testMulti);
        runUpperAndLower("select course , day from *", testMulti);
    }

    @Test
    public void selectAllFromSpecifiedNoWhere() throws Exception {
        //single value in the select clause
        ThrowingConsumer<XmlGraphSearchCriteria> test = (criteria) -> {
            SelectAsserter selectAsserter = new SelectAsserter();
            selectAsserter.doAssert(criteria.getSelect(), null, XmlGraphSearchConstants.ALL);

            FromAsserter fromAsserter = new FromAsserter();
            fromAsserter.doAssert(criteria.getFrom(), null, "course");

            WhereAsserter whereAsserter = new WhereAsserter();
            whereAsserter.doAssert(criteria.getWhere());
        };
        runUpperAndLower("select * from course", test);
        runUpperAndLower("select * from course;", test);

        //multiple values in the select clause
        ThrowingConsumer<XmlGraphSearchCriteria> testMulti = (criteria) -> {
            SelectAsserter selectAsserter = new SelectAsserter();
            selectAsserter.doAssert(criteria.getSelect(), null, XmlGraphSearchConstants.ALL);

            FromAsserter fromAsserter = new FromAsserter(2);
            fromAsserter.doAssert(criteria.getFrom(),0, null, "course");
            fromAsserter.doAssert(criteria.getFrom(), 1,null, "class");

            WhereAsserter whereAsserter = new WhereAsserter();
            whereAsserter.doAssert(criteria.getWhere());
        };
        runUpperAndLower("select * from course, class", testMulti);
        runUpperAndLower("select * from course , class", testMulti);
    }

    private void assertSelectAllFromAll(XmlGraphSearchCriteria criteria) {
        SelectAsserter selectAsserter = new SelectAsserter();
        selectAsserter.doAssert(criteria.getSelect(), null, XmlGraphSearchConstants.ALL);

        FromAsserter fromAsserter = new FromAsserter();
        fromAsserter.doAssert(criteria.getFrom(), null, XmlGraphSearchConstants.ALL);
    }

    private static void assertSelector(XmlNodeSelector selector, String expectedName, XmlGraph.NodeType expectedNodeType) {
        assertThat(selector.getName()).isEqualToIgnoringCase(expectedName);
        if (expectedNodeType == null) {
            assertThat(selector.getNodeType()).isNull();
        } else {
            assertThat(selector.getNodeType()).isEqualTo(expectedNodeType);
        }
    }


    private static abstract class AbstractSelectorAsserter<T> {
        private final int expectedSize;
        private AbstractSelectorAsserter(int expectedSize) {
            this.expectedSize = expectedSize;
        }

        private AbstractSelectorAsserter() {
            this(1);
        }

        public void doAssert(T clause, XmlGraph.NodeType expectedType, String expectedName) {
            doAssert(clause, 0, expectedType, expectedName);
        }

        public void doAssert(T clause, int index, XmlGraph.NodeType expectedType, String expectedName) {
            assertThat(clause).isNotNull();
            List<XmlNodeSelector> selectors = getSelectors(clause);
            assertThat(selectors).isNotNull();
            assertThat(selectors.size()).isEqualTo(expectedSize);
            assertSelector(selectors.get(index), expectedName, expectedType);
        }

        protected abstract List<XmlNodeSelector> getSelectors(T clause);
    }

    private static class SelectAsserter extends AbstractSelectorAsserter<XmlSelectClause> {
        public SelectAsserter(int expectedSize) {
            super(expectedSize);
        }

        public SelectAsserter() {
        }

        @Override
        protected List<XmlNodeSelector> getSelectors(XmlSelectClause clause) {
            return clause.getSelectElementsNamed();
        }

    }

    private static class FromAsserter extends AbstractSelectorAsserter<XmlFromClause> {

        public FromAsserter(int expectedSize) {
            super(expectedSize);
        }

        public FromAsserter() {
        }

        @Override
        protected List<XmlNodeSelector> getSelectors(XmlFromClause clause) {
            return clause.getXmlNodeSelectors();
        }
    }

    private static class WhereAsserter {
        private final WhereConditionAsserter[] asserters;

        private WhereAsserter(WhereConditionAsserter...asserters) {
            this.asserters = asserters;
        }

        public void doAssert(XmlWhereClause xmlWhereClause) {
            assertThat(xmlWhereClause).isNotNull();
            assertThat(xmlWhereClause.getWhereConditions()).isNotNull();
            int expectedSize = asserters != null ? asserters.length : 0;
            assertThat(xmlWhereClause.getWhereConditions().size()).isEqualTo(expectedSize);
            if (asserters != null) {
                for (int i = 0; i < asserters.length; i++) {
                    asserters[i].doAssert(xmlWhereClause.getWhereConditions().get(i));
                }
            }
        }
    }

    private static class WhereConditionAsserter {
        private final XmlGraph.NodeType expectedType;
        private final String expectedName;
        private final XmlSelectConditionMatchType expectedCondition;
        private final String expectedValue;

        private WhereConditionAsserter(XmlGraph.NodeType expectedType, String expectedName, XmlSelectConditionMatchType expectedCondition, String expectedValue) {
            this.expectedType = expectedType;
            this.expectedName = expectedName;
            this.expectedCondition = expectedCondition;
            this.expectedValue = expectedValue;
        }

        public void doAssert(XmlWhereCondition xmlWhereCondition) {
            assertSelector(xmlWhereCondition.getXmlNodeSelector(), expectedName, expectedType);
            assertThat(xmlWhereCondition.getMatchCondition()).isEqualTo(expectedCondition);
            assertThat(xmlWhereCondition.getValue()).isEqualToIgnoringCase(expectedValue);
        }
    }


}
