package com.ETLAutomation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.backendutils.Env;
import com.backendutils.MongoUtils;
import com.configuration.LoggerInitialization;
import com.configuration.api.Configuration;
import com.mongodb.Block;
import com.mongodb.MongoException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;

public class ETLAutomation extends Configuration {

    private Logger logger = LoggerInitialization.setupLogger("ETL_Status_");
    MongoUtils mongoUtils = new MongoUtils();

    private Env.Mongo dbServer = Env.Mongo.FEEDS_PROD_PARALLEL;
    //PROD
    private String dbName = "idscp-prod-2";
    //QA
    //private String dbName = "ids-dev-2";

    @DataProvider(name = "incrementalCollections")
    public Object[][] getIncrementalCollections() {
        return new Object[][]{
                {"rds_agnt_rtng", "rds_issuer_incr"},
                {"rds_security_rtng", "rds_issue_incr"}
        };
    }

    @Test(dataProvider = "incrementalCollections")
    public void etl_validateIncrementalCollections(String collectionName, String etlName) throws ParseException {
        //Pass Query
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        Date date = new Date();
        String reqDate = formatter.format(date);
        System.out.println("Validating For Date: " + reqDate);

        //For Tomorrow's Date
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Date yesterday = cal.getTime();
        String reqYesterday = formatter.format(yesterday);
        System.out.println("Yesterday's Date: " + reqYesterday);

        MongoCollection<Document> etlHistoryCollection = mongoUtils
                .connectToMongoDatabase(dbServer)
                .getDatabase(dbName)
                .getCollection("ETLHistory_Meter");


        AggregateIterable<Document> etlHistoryOutput = etlHistoryCollection.aggregate(Arrays.asList(
                new Document()
                        .append("$match", new Document()
                                .append("etlName", etlName)
                                .append("startTime", new Document()
                                        .append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(reqYesterday))
                                        .append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(reqDate))
                                )
                        ),
                new Document()
                        .append("$project", new Document()
                                .append("etlName", 1.0)
                                .append("rowsProcessed", 1.0)
                                .append("startTime", 1.0)
                                .append("LOAD_ID", 1.0)
                                .append("_id", 0.0)
                        ),
                new Document()
                        .append("$group", new Document()
                                .append("_id", "$etlName")
                                .append("count", new Document()
                                        .append("$sum", "$rowsProcessed")
                                )
                                .append("first", new Document()
                                        .append("$min", "$LOAD_ID")
                                )
                                .append("last", new Document()
                                        .append("$max", "$LOAD_ID")
                                )
                        )
                )
        );

        MongoCollection<Document> collection = mongoUtils
                .connectToMongoDatabase(dbServer)
                .getDatabase(dbName)
                .getCollection(collectionName);


        for (Document etlDocument : etlHistoryOutput) {
            System.out.println("ETL_HISTORY_METER COUNT " + collectionName.toUpperCase() + "    " + etlDocument.get("count"));

            AggregateIterable<Document> collectionOutput = collection.aggregate(
                    Arrays.asList(
                            new Document()
                                    .append("$match", new Document()
                                            .append("LOAD_ID", new Document()
                                                    .append("$gte", etlDocument.get("first"))
                                                    .append("$lte", etlDocument.get("last"))
                                            )
                                    ),
                            new Document()
                                    .append("$count", "count")
                    )
            );

            for (Document collectionDocument : collectionOutput) {
                System.out.println("COLLECTION COUNT " + collectionName.toUpperCase() + "   " + collectionDocument.get("count"));
            }
        }
    }

    @DataProvider(name = "refreshCollections")
    public Object[][] getRefreshCollections(){
        return new Object[][]{
                {"rds_agnt_rtng_ref","rds_issuer_ref"},
                {"rds_security_rtng_ref","rds_issue_ref"}
        };
    }

    @Test(dataProvider = "refreshCollections")
    public void etl_validateRefreshCollections(String collectionName, String etlName){
        MongoCollection<Document> etlHistoryCollection = mongoUtils
                .connectToMongoDatabase(dbServer)
                .getDatabase(dbName)
                .getCollection("ETLHistory_Meter");

        AggregateIterable<Document> etlHistoryOutput = etlHistoryCollection.aggregate(
                Arrays.asList(
                        new Document()
                                .append("$match", new Document()
                                        .append("etlName", etlName)
                                ),
                        new Document()
                                .append("$sort", new Document()
                                        .append("_id", -1.0)
                                ),
                        new Document()
                                .append("$limit", 1.0)
                )
        );

        MongoCollection<Document> collection = mongoUtils
                .connectToMongoDatabase(dbServer)
                .getDatabase(dbName)
                .getCollection(collectionName);


        for (Document etlDocument : etlHistoryOutput) {
            System.out.println("ETL_HISTORY_METER COUNT " + collectionName.toUpperCase() + "    " + etlDocument.get("rowsProcessed"));

            AggregateIterable<Document> collectionOutput = collection.aggregate(
                    Arrays.asList(
                            new Document()
                                    .append("$match", new Document()
                                            .append("LOAD_ID", etlDocument.get("LOAD_ID"))
                                    ),
                            new Document()
                                    .append("$count", "count")
                    )
            );

            for (Document collectionDocument : collectionOutput) {
                Assert.assertEquals(etlDocument.get("rowsProcessed"), collectionDocument.get("count"));
                System.out.println("COLLECTION COUNT " + collectionName.toUpperCase() + "   " + collectionDocument.get("count"));
            }
        }

    }


    @DataProvider(name = "etlNames")
    public Object[][] getEtlNames() {
        return new Object[][]{
//                {"agent_ranking","fcf_agent_ranking_etl"},
//                {"agents","agentUpdateETL"},
//                {"agnt_identifiers","agnt_identifiers"},
//                {"bank_financials","Bank_Financials"},
//                {"bank_model","bcm_incr"},
//                {"bmi_countryCurrency","bmi"},
//                {"clients_ftpAccess","client_FTPAccess"},
//                {"directors_executives","directors_executives"},
//                {"insurance_financials","Insurance_Financials"},
//                {"issue_credit_opinions","issue_credit_opinions"},
//                {"issuer_credit_opinions","issuer_credit_opinions"},
//                {"lloyds_financial","Lloyds_Financials"},
//                {"market_sectors","get_market_sector"},
                {"rds_agnt_rtng", "rds_issuer_incr"},
//                {"rds_agnt_rtng_ref","rds_issuer_ref"},
                {"rds_security_rtng", "rds_issue_incr"},
//                {"rds_security_rtng_ref","rds_issue_ref"},
//                {"region_countries","populate_region_countries"},
//                {"security_identifiers","security_identifiers"},
//                {"shareholders","shareholders"},
//                {"sovereign_financials","Sovereign_Financials"},
//                {"st_agnt_attr","AGNT_ATTR_INS"},
//                {"states","populate_state"}
        };
    }

    @Test(dataProvider = "refreshCollections")
    public void ETL_HistoryTest(String collectionName, String etlName) {
        try {
            MongoCollection<Document> etlHistoryCollection = mongoUtils
                    .connectToMongoDatabase(dbServer)
                    .getDatabase(dbName)
                    .getCollection("ETLHistory_Meter");

            /*MongoCollection<Document> collection = mongoUtils
                    .connectToMongoDatabase(dbServer)
                    .getDatabase(dbName)
                    .getCollection(collectionName);

            Integer collectionCount = Integer.parseInt(String.valueOf((int) collection.countDocuments()));
            System.out.println(collectionCount);*/

            //Pass Query
            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String reqDate = formatter.format(date);
            System.out.println("Validating For Date: " + reqDate);

            //For Tomorrow's Date
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_YEAR, 1);
            Date tomorrow = cal.getTime();
            String reqTomorrow = formatter.format(tomorrow);
            System.out.println("Tomorrow's Date: " + reqTomorrow);


            Document query = new Document();
            query.append("$and", Arrays.asList(
                    new Document()
                            .append("etlName", etlName),
                    new Document()
                            .append("endTime", new Document()
                                    .append("$gte", new SimpleDateFormat("yyyy-MM-dd").parse(reqDate))
                            ),
                    new Document()
                            .append("endTime", new Document()
                                    .append("$lt", new SimpleDateFormat("yyyy-MM-dd").parse(reqTomorrow))
                            )
                    )
            );


            Document projection = new Document();
            projection.append("jobStatus", 1.0);
            projection.append("rowsProcessed", 1.0);
            projection.append("startTime", 1.0);
            projection.append("LOAD_ID", 1.0);

            Document sort = new Document();
            sort.append("_id", -1.0);
            int limit = 1;


            Block<Document> processBlock = new Block<Document>() {
                @Override
                public void apply(final Document document) {

                    System.out.println(document.toJson());

                    String etlRunStatus = (document.getString("jobStatus"));
                    Integer numberOfDocumentsProcessed = (document.getInteger("rowsProcessed"));
                    Date dateOfProcess = (document.getDate("startTime"));
                    Long latestLoadId = (document.getLong("LOAD_ID"));
                    System.out.println(etlRunStatus);

                    boolean failure = false;

                    if (etlRunStatus.equals("Success")) {
                        System.out.println("ETL RAN SUCCESSFULLY FOR " + collectionName.toUpperCase() + " TODAY");
                        logger.info("ETL RAN SUCCESSFULLY FOR " + collectionName.toUpperCase() + " TODAY        NUMBER OF DOCS PROCESSED "
                                + numberOfDocumentsProcessed + "        DATE OF PROCESS "
                                + dateOfProcess + "         LATEST LOAD_ID "
                                + latestLoadId);
                    } else {
                        failure = true;
                        System.out.println("ETL FAILED FOR " + collectionName.toUpperCase() + " TODAY");
                        logger.error("ETL FAILED FOR " + collectionName.toUpperCase() + " TODAY");
                    }
                    Assert.assertFalse(failure);
                }
            };

            etlHistoryCollection.find(query).projection(projection).sort(sort).limit(limit).forEach(processBlock);

        } catch (MongoException e) {
            System.err.println("ERROR MONGO EXCEPTION " + e);
            logger.error("ERROR MONGO EXCEPTION " + e);
        } catch (ParseException e) {
            System.err.println("ERROR PARSE EXCEPTION " + e);
            logger.error("ERROR PARSE EXCEPTION " + e);
        }
    }
}



