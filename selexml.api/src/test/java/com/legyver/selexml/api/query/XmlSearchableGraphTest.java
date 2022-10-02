package com.legyver.selexml.api.query;

import com.legyver.selexml.api.XmlSearchableGraph;
import com.legyver.selexml.api.query.text.SqlSyntaxInterpreter;
import com.legyver.utils.graphjxml.XmlGraph;
import com.legyver.utils.graphjxml.reader.GraphXmlReader;
import com.legyver.utils.graphjxml.reader.XmlInputStreamReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class XmlSearchableGraphTest {
    private static XmlSearchableGraph xmlSearchableGraph;
    private final SqlSyntaxInterpreter sqlSyntaxInterpreter = new SqlSyntaxInterpreter();


    @BeforeAll
    public static void readGraph() {
        XmlInputStreamReader xmlInputStreamReader = new XmlInputStreamReader(new GraphXmlReader());
        InputStream inputStream = XmlSearchableGraphTest.class.getResourceAsStream("reed-reduced.xml");
        XmlGraph xmlGraph = xmlInputStreamReader.parse(inputStream);
        xmlSearchableGraph = new XmlSearchableGraph(xmlGraph);
    }

    @Test
    public void selectAll() throws Exception {
        String query = "select * from *";

        XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
        XmlGraph graph = xmlSearchableGraph.search(criteria);
        assertThat(graph.getChildren().size()).isEqualTo(6);
    }

    @Test
    public void selectAllCourses() throws Exception {
        String query = "select * from course";

        XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
        XmlGraph graph = xmlSearchableGraph.search(criteria);
        assertThat(graph.getChildren().size()).isEqualTo(6);
    }

    @Test
    public void selectAllCoursesWhereInstructorIs() throws Exception {
        {
            String query = "select * from course where instructor is Kaplan";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(4);
        }
        {
            String query = "select * from course where instructor is kaplan";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(0);
        }
        {
            String query = "select * from course where instructor is_ kaplan";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(4);
        }
        {
            String query = "SELECT * FROM COURSE WHERE INSTRUCTOR IS KAPLAN";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(0);
        }
        {
            String query = "SELECT * FROM COURSE WHERE INSTRUCTOR IS_ KAPLAN";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(4);
        }
    }

    @Test
    public void selectAllCoursesWhereInstructorNull() throws Exception {
        {
            String query = "select * from course where instructor is null";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(1);
        }
        {
            String query = "SELECT * FROM COURSE WHERE INSTRUCTOR IS NULL";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(1);
        }
    }

    @Test
    public void selectAllCoursesWhereInstructorNotNull() throws Exception {
        {
            String query = "select * from course where instructor not null";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(5);
        }
        {
            String query = "SELECT * FROM COURSE WHERE INSTRUCTOR NOT NULL";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(5);
        }
    }

    @Test
    public void selectAllCoursesWhereInstructorEquals() throws Exception {
        {
            String query = "select * from course where instructor = Kaplan";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(4);
        }
        {
            String query = "select * from course where instructor = kaplan";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(0);
        }
        {
            String query = "select * from course where instructor =_ kaplan";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(4);
        }
        {
            String query = "SELECT * FROM COURSE WHERE INSTRUCTOR = KAPLAN";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(0);
        }
        {
            String query = "SELECT * FROM COURSE WHERE INSTRUCTOR =_ KAPLAN";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(4);
        }
    }

    @Test
    public void selectAllCoursesWhereInstructorNot() throws Exception {
        {
            String query = "select * from course where instructor not Kaplan";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(2);
        }
        {
            String query = "select * from course where instructor not kaplan";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(6);
        }
        {
            String query = "select * from course where instructor not_ kaplan";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(2);
        }
        {
            String query = "SELECT * FROM COURSE WHERE INSTRUCTOR NOT KAPLAN";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(6);
        }
        {
            String query = "SELECT * FROM COURSE WHERE INSTRUCTOR NOT_ KAPLAN";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(2);
        }
    }

    @Test
    public void selectAllCoursesWhereInstructorNotEqual() throws Exception {
        {
            String query = "select * from course where instructor != Kaplan";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(2);
        }
        {
            String query = "select * from course where instructor != kaplan";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(6);
        }
        {
            String query = "select * from course where instructor !=_ kaplan";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(2);
        }
        {
            String query = "SELECT * FROM COURSE WHERE INSTRUCTOR != KAPLAN";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(6);
        }
        {
            String query = "SELECT * FROM COURSE WHERE INSTRUCTOR !=_ KAPLAN";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(2);
        }
        //same as above but with <> instead of !=
        {
            String query = "select * from course where instructor <> Kaplan";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(2);
        }
        {
            String query = "select * from course where instructor <> kaplan";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(6);
        }
        {
            String query = "select * from course where instructor <>_ kaplan";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(2);
        }
        {
            String query = "SELECT * FROM COURSE WHERE INSTRUCTOR <> KAPLAN";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(6);
        }
        {
            String query = "SELECT * FROM COURSE WHERE INSTRUCTOR <>_ KAPLAN";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(2);
        }
    }

}
