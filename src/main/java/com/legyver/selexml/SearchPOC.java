package com.legyver.selexml;

import com.legyver.selexml.query.XmlGraphSearchCriteria;
import com.legyver.selexml.query.XmlSearchableGraph;
import com.legyver.utils.graphjxml.XmlGraph;
import com.legyver.utils.graphjxml.reader.GraphXmlReader;
import com.legyver.utils.graphjxml.reader.XmlFileReader;
import com.legyver.utils.graphjxml.writer.GraphXmlWriter;
import com.legyver.utils.graphjxml.writer.XmlFileWriter;

import static com.legyver.selexml.POCContext.etcFile;


public class SearchPOC {

    public static void main(String[] args) {
        //easier to verify via output files
        XmlFileWriter xmlFileWriter = new XmlFileWriter(new GraphXmlWriter());

        XmlFileReader xmlFileReader = new XmlFileReader(new GraphXmlReader());
        //original graph
        XmlGraph moreCourses = xmlFileReader.parse(etcFile("reedcollege.xml"));
        //create an indexed version for search
        XmlSearchableGraph searchableGraph = new XmlSearchableGraph(moreCourses);

        XmlGraphSearchCriteria criteria = new XmlGraphSearchCriteria.Builder()
                .selectAll()
                .fromElementNamed("course")
                .whereElementNamed("instructor")
                .is("Kaplan")
                .build();
        XmlGraph result = searchableGraph.search(criteria);
        xmlFileWriter.writeToFile(result, etcFile("search-kaplan.xml"));

    }
}
