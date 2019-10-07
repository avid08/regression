package com.fitchconnect.fiscontent;

import com.backendutils.Env;
import com.backendutils.MongoUtils;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.testng.annotations.Test;

public class T1_Sprint_13 {

    MongoUtils mongoUtils = new MongoUtils();

    @Test
    public void Fisc7821(){
        MongoCollection<Document> collection = mongoUtils
                .connectToMongoDatabase(Env.Mongo.CDS)
                .getDatabase("esp-9")
                .getCollection("fitch_entity");

        /*for (Document document : collection){
            System.out.println(document.getString("disclosureVOs"));
        }*/

        System.out.println(collection.countDocuments());
    }

}
