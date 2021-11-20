package com.legyver.selexml.api.poc;

import java.io.File;

/**
 * Context for POC demos dealing with importing/exporting xml files
 */
public class POCContext {
    /**
     * get the file reference from the project etc folder
     * @param filename the name of the file
     * @return the file
     */
    static File etcFile(String filename) {
        return new File("etc" +  File.separator + filename);
    }

}
