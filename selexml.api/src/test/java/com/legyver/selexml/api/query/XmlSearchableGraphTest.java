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
            String query = "SELECT * FROM COURSE WHERE INSTRUCTOR IS_ KAPLAN";

            XmlGraphSearchCriteria criteria = sqlSyntaxInterpreter.parse(query);
            XmlGraph graph = xmlSearchableGraph.search(criteria);
            assertThat(graph.getChildren().size()).isEqualTo(4);
        }
    }

}
