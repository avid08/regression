package com.etlautomation;

import org.bson.Document;

import java.util.HashMap;

public class ETLUtils {
    public static HashMap<String, Document> runQueryAndProjection(String etlName){
        Document query1 = new Document();
        query1.append("etlName", etlName);

        Document projection1 = new Document();
        projection1.append("jobStatus", 1.0);
        projection1.append("rowsProcessed", 1.0);
        projection1.append("startTime", 1.0);
        projection1.append("LOAD_ID", 1.0);

        Document sort = new Document();
        sort.append("_id", -1.0);
        int limit = 1;

        HashMap<String, Document> out = new HashMap<>();
        out.put("query", query1);
        out.put("projection", projection1);
        out.put("sort", sort);

        return out;
    }

    public static void validate(){

    }
}
