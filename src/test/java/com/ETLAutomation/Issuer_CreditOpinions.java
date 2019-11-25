package com.ETLAutomation;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.bson.Document;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.backendutils.Env;
import com.backendutils.MongoUtils;
import com.configuration.api.Configuration;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;

public class Issuer_CreditOpinions extends Configuration {
    MongoUtils mongoUtils = new MongoUtils();


    @Test

    public void IssueRCreditOpinion_validatingSuccessfull_ETL_run() throws ParseException {

        try {
            MongoCollection<Document> Collection1 = mongoUtils.connectToMongoDatabase(Env.Mongo.FEEDS_QA)
                    .getDatabase("ids-dev-2").getCollection("ETLHistory_Meter");

            Document query1 = new Document();
            query1.append("etlName", "issuer_credit_opinions");

            Document projection1 = new Document();
            projection1.append("jobStatus", 1.0);
            projection1.append("rowsProcessed", 1.0);
            projection1.append("startTime", 1.0);
            projection1.append("LOAD_ID", 1.0);

            Document sort = new Document();
            sort.append("_id", -1.0);
            int limit = 1;

            Block<Document> processBlock = new Block<Document>() {
                @Override
                public void apply(final Document document) {


                    String etlrunStatus = (document.getString("jobStatus"));
                    Integer numberofdocumentProcessed = (document.getInteger("rowsProcessed"));
                    Date DateOfProcess = (document.getDate("startTime"));
                    Long LatestLoadId = (document.getLong("LOAD_ID"));
                    System.out.println(DateOfProcess);

                    boolean failure = false;

                    if (etlrunStatus.equals("Success")) {
                        System.out.println("etl Ran successfully for IssueRCreditOpinion Today");
                    } else {
                        failure = true;
                        System.out.println("etl Ran did not run  for IssueRCreditOpinion Today");
                    }
                    Assert.assertFalse(failure);
                }
            };

            Collection1.find(query1).projection(projection1).sort(sort).limit(limit).forEach(processBlock);

        } catch (MongoException e) {
            // handle MongoDB exception
        }

    }

    @Test

    public void IssueRCreditOpinion_validating_rowProcessCountPer_LatestETLRun() throws ParseException {

        try {
            MongoCollection<Document> Collection1 = mongoUtils.connectToMongoDatabase(Env.Mongo.FEEDS_QA)
                    .getDatabase("ids-dev-2").getCollection("ETLHistory_Meter");
            MongoCollection<Document> collection2 = mongoUtils.connectToMongoDatabase(Env.Mongo.FEEDS_QA)
                    .getDatabase("ids-dev-2").getCollection("issuer_credit_opinions");

            MongoClient mongoClient = mongoUtils.connectToMongoDatabase(Env.Mongo.FEEDS_QA);

            DB db = mongoClient.getDB("ids-dev-2");
            DBCollection collection = db.getCollection("issue_credit_opinions");

            Document query1 = new Document();
            query1.append("etlName", "issuer_credit_opinions");

            Document projection1 = new Document();
            projection1.append("LOAD_ID", 1.0);
            projection1.append("rowsProcessed", 1.0);
            Document sort = new Document();
            sort.append("_id", -1.0);
            int limit = 1;

            Block<Document> processBlock1 = new Block<Document>() {
                @Override
                public void apply(final Document document1) {

                    Long LatestLoadId = (document1.getLong("LOAD_ID"));
                    Integer numberofdocumentProcessed = (document1.getInteger("rowsProcessed"));
                    System.out.println("number of row process :"+numberofdocumentProcessed);

                    BasicDBObject query = new BasicDBObject();
                    query.put("LOAD_ID", LatestLoadId);

                    BasicDBObject projection = new BasicDBObject();
                    projection.put("_id", 1.0);

                    Cursor cursor = collection.find(query, projection);
                    int counter = 0;
                    while (cursor.hasNext()) {
                        BasicDBObject document = (BasicDBObject) cursor.next();
                        counter++;
                    }
                    int issueCreditTotalCount = counter;

                    //System.out.println(issueCreditTotalCount);
                    System.out.println(issueCreditTotalCount);
                    System.out.println(numberofdocumentProcessed);
                    Assert.assertTrue(issueCreditTotalCount==numberofdocumentProcessed);

                }
            };
            Collection1.find(query1).projection(projection1).sort(sort).limit(limit).forEach(processBlock1);

        } catch (MongoException e) {
            // handle MongoDB exception
        }

    }


    @Test

    public void IssueRCreditOpinion_ValidateTheLatest_ETL_Rundate() throws ParseException {

        Date date = Calendar.getInstance().getTime();
        String strDate = date.toString();
        String today  = strDate.substring(0,10);
        System.out.println("Todays's date  : "+today);


        try {
            MongoCollection<Document> Collection1 = mongoUtils.connectToMongoDatabase(Env.Mongo.FEEDS_QA)
                    .getDatabase("ids-dev-2").getCollection("ETLHistory_Meter");

            Document query1 = new Document();
            query1.append("etlName", "issue_credit_opinions");

            Document projection1 = new Document();
            projection1.append("jobStatus", 1.0);
            projection1.append("rowsProcessed", 1.0);
            projection1.append("startTime", 1.0);
            projection1.append("LOAD_ID", 1.0);

            Document sort = new Document();
            sort.append("_id", -1.0);
            int limit = 1;

            Block<Document> processBlock = new Block<Document>() {
                @Override
                public void apply(final Document document) {

                    Date startTimeofDate = (document.getDate("startTime"));

                    String strpressDate = startTimeofDate.toString();
                    String ETLprocessDate = strpressDate.substring(0,10);
                    System.out.println("etl run date : "+ETLprocessDate);

                    Assert.assertTrue(today.equals(ETLprocessDate));

                }
            };

            Collection1.find(query1).projection(projection1).sort(sort).limit(limit).forEach(processBlock);

        } catch (MongoException e) {
            // handle MongoDB exception
        }
    }

}