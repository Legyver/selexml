package com.legyver.selexml.api.poc;

import com.legyver.selexml.api.query.XmlGraphSearchConstants;
import com.legyver.selexml.api.query.XmlGraphSearchCriteria;
import com.legyver.selexml.api.XmlSearchableGraph;
import com.legyver.utils.graphjxml.XmlGraph;
import com.legyver.utils.graphjxml.reader.GraphXmlReader;
import com.legyver.utils.graphjxml.reader.XmlFileReader;
import com.legyver.utils.graphjxml.writer.GraphXmlWriter;
import com.legyver.utils.graphjxml.writer.XmlFileWriter;

import static com.legyver.selexml.api.poc.POCContext.etcFile;

/**
 * POC to demonstrate outputting graphs without any filters in a performant way
 */
public class ListAllPOC {

    /**
     * The runnable demo
     * @param args ignored command line args
     */
    public static void main(String[] args) {
        boolean reduced = false;
        //easier to verify via output files
        XmlFileWriter xmlFileWriter = new XmlFileWriter(new GraphXmlWriter());

        XmlFileReader xmlFileReader = new XmlFileReader(new GraphXmlReader());
        //original graph
        XmlGraph moreCourses = xmlFileReader.parse(etcFile(reduced ? "reedreduced.xml" : "reedcollege.xml"));
        //create an indexed version for search
        XmlSearchableGraph searchableGraph = new XmlSearchableGraph(moreCourses);

        XmlGraphSearchCriteria criteria = new XmlGraphSearchCriteria.Builder()
                .selectAll()
                .fromAnyNamed(XmlGraphSearchConstants.ALL)
                .buildNoWhere();
        XmlGraph result = searchableGraph.search(criteria);
        xmlFileWriter.writeToFile(result, etcFile(reduced ? "search-nofilter.xml" : "search-nofilter-full.xml"));

    }
}
