package com.legyver.selexml;

import java.io.File;

public class POCContext {
    static File etcFile(String filename) {
        return new File("etc" +  File.separator + filename);
    }

}
