package com.Cds.Api;

import com.apiutils.APIUtils;
import com.backendutils.Env;
import com.backendutils.MongoUtils;
import com.backendutils.PostgresUtils;
import com.configuration.LoggerInitialization;
import com.configuration.api.Configuration;
import com.jayway.restassured.response.Response;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

public class T1_Sprint_14 extends Configuration {

    Logger logger = LoggerInitialization.setupLogger("T1_Sprint_14");
    MongoUtils mongoUtils = new MongoUtils();
    APIUtils apiUtils = new APIUtils();
    PostgresUtils postgresUtils = new PostgresUtils();

    private Integer timeoutBetweenTests = 2000;

    @DataProvider(name="Fisc6354")
    public Object[][] getDataFor6354(){
        MongoCollection<Document> collection = mongoUtils
                .connectToMongoDatabase(Env.Mongo.META_QA)
                .getDatabase("xrefdb")
                .getCollection("metadataFields");

        BasicDBObject query = new BasicDBObject();
        query.put("dataSourceId", "cds");
        String mongoResponse = null;
        MongoCursor<Document> cursor = collection.find(query).iterator();
        while (cursor.hasNext()) {
            mongoResponse = cursor.next().toJson();
        }

        String uri = baseURI + "/v1/metadata/fields?filter[source]=cds";
        String apiResponse = apiUtils.getResponse(uri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue).asString();


        return new Object[][] {
                {"FC_2Y_BPS_CDS","2Y CDS (bps)","2Y CDS (bps)","2Y CDS in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_2Y_BPS_CDS_PD","2Y CDS (bps) PD","2Y CDS (bps) PD","2Y CDS Probability of Default in basis points","base", apiResponse, mongoResponse},
                {"FC_2Y_DD_BPS_CDS","2Y CDS d/d (bps)","2Y CDS d/d (bps)","2Y CDS Day on Day change in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_2Y_DD_BPS_CDS_PD","2Y CDS d/d (bps) PD","2Y CDS d/d (bps) PD","2Y CDS Probability of Default Day on Day change in basis points","base", apiResponse, mongoResponse},
                {"FC_2Y_DD_PERCENT_CDS","2Y CDS d/d (%)","2Y CDS d/d (%)","2Y CDS Day on Day percentage change","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_2Y_DD_PERCENT_CDS_PD","2Y CDS d/d (%) PD","2Y CDS d/d (%) PD","2Y CDS Probability of Default Day on Day change percent change","base", apiResponse, mongoResponse},
                {"FC_2Y_MM_BPS_CDS","2Y CDS m/m (bps)","2Y CDS m/m (bps)","2Y CDS Month on Month change in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_2Y_MM_BPS_CDS_PD","2Y CDS m/m (bps) PD","2Y CDS m/m (bps) PD","2Y CDS Probability of Default Month on Month change in basis points","base", apiResponse, mongoResponse},
                {"FC_2Y_MM_PERCENT_CDS","2Y CDS m/m (%)","2Y CDS m/m (%)","2Y CDS Month on Month percentage change","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_2Y_MM_PERCENT_CDS_PD","2Y CDS m/m (%) PD","2Y CDS m/m (%) PD","2Y CDS Probability of Default Month on Month change percent change","base", apiResponse, mongoResponse},
                {"FC_2Y_QQ_BPS_CDS","2Y CDS q/q (bps)","2Y CDS q/q (bps)","2Y CDS Quarter on Quarter change in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_2Y_QQ_BPS_CDS_PD","2Y CDS q/q (bps) PD","2Y CDS q/q (bps) PD","2Y CDS Probability of Default Quarter on Quarter change in basis points","base", apiResponse, mongoResponse},
                {"FC_2Y_QQ_PERCENT_CDS","2Y CDS q/q (%)","2Y CDS q/q (%)","2Y CDS Quarter on Quarter percentage change","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_2Y_QQ_PERCENT_CDS_PD","2Y CDS q/q (%) PD","2Y CDS q/q (%) PD","2Y CDS Probability of Default Quarter on Quarter change percent change","base", apiResponse, mongoResponse},
                {"FC_2Y_SS_BPS_CDS","2Y CDS hy/hy (bps)","2Y CDS hy/hy (bps)","2Y CDS Quarter on Half-Year change in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_2Y_SS_BPS_CDS_PD","2Y CDS hy/hy (bps) PD","2Y CDS hy/hy (bps) PD","2Y CDS Probability of Default Half-Year on Half-Year change in basis points","base", apiResponse, mongoResponse},
                {"FC_2Y_SS_PERCENT_CDS","2Y CDS hy/hy (%)","2Y CDS hy/hy (%)","2Y CDS Quarter on Half-Year percentage change","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_2Y_SS_PERCENT_CDS_PD","2Y CDS hy/hy (%) PD","2Y CDS hy/hy (%) PD","2Y CDS Probability of Default Half-Year on Half-Year change percent change","base", apiResponse, mongoResponse},
                {"FC_2Y_WW_BPS_CDS","2Y CDS w/w (bps)","2Y CDS w/w (bps)","2Y CDS Week on Week change in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_2Y_WW_BPS_CDS_PD","2Y CDS w/w (bps) PD","2Y CDS w/w (bps) PD","2Y CDS Probability of Default Week on Week change in basis points","base", apiResponse, mongoResponse},
                {"FC_2Y_WW_PERCENT_CDS","2Y CDS w/w (%)","2Y CDS w/w (%)","2Y CDS Week on Week percentage change","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_2Y_WW_PERCENT_CDS_PD","2Y CDS w/w (%) PD","2Y CDS w/w (%) PD","2Y CDS Probability of Default Week on Week change percent change","base", apiResponse, mongoResponse},
                {"FC_2Y_YY_BPS_CDS","2Y CDS y/y (bps)","2Y CDS y/y (bps)","2Y CDS Year on Year change in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_2Y_YY_BPS_CDS_PD","2Y CDS y/y (bps) PD","2Y CDS y/y (bps) PD","2Y CDS Probability of Default Year on Year change in basis points","base", apiResponse, mongoResponse},
                {"FC_2Y_YY_PERCENT_CDS","2Y CDS y/y (%)","2Y CDS y/y (%)","2Y CDS Year on Year percentage change","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_2Y_YY_PERCENT_CDS_PD","2Y CDS y/y (%) PD","2Y CDS y/y (%) PD","2Y CDS Probability of Default Year on Year change percent change","base", apiResponse, mongoResponse},
                {"FC_4Y_BPS_CDS","4Y CDS (bps)","4Y CDS (bps)","4Y CDS in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_4Y_BPS_CDS_PD","4Y CDS (bps) PD","4Y CDS (bps) PD","4Y CDS Probability of Default in basis points","base", apiResponse, mongoResponse},
                {"FC_4Y_DD_BPS_CDS","4Y CDS d/d (bps)","4Y CDS d/d (bps)","4Y CDS Day on Day change in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_4Y_DD_BPS_CDS_PD","4Y CDS d/d (bps) PD","4Y CDS d/d (bps) PD","4Y CDS Probability of Default Day on Day change in basis points","base", apiResponse, mongoResponse},
                {"FC_4Y_DD_PERCENT_CDS","4Y CDS d/d (%)","4Y CDS d/d (%)","4Y CDS Day on Day percentage change","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_4Y_DD_PERCENT_CDS_PD","4Y CDS d/d (%) PD","4Y CDS d/d (%) PD","4Y CDS Probability of Default Day on Day change percent change","base", apiResponse, mongoResponse},
                {"FC_4Y_MM_BPS_CDS","4Y CDS m/m (bps)","4Y CDS m/m (bps)","4Y CDS Month on Month change in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_4Y_MM_BPS_CDS_PD","4Y CDS m/m (bps) PD","4Y CDS m/m (bps) PD","4Y CDS Probability of Default Month on Month change in basis points","base", apiResponse, mongoResponse},
                {"FC_4Y_MM_PERCENT_CDS","4Y CDS m/m (%)","4Y CDS m/m (%)","4Y CDS Month on Month percentage change","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_4Y_MM_PERCENT_CDS_PD","4Y CDS m/m (%) PD","4Y CDS m/m (%) PD","4Y CDS Probability of Default Month on Month change percent change","base", apiResponse, mongoResponse},
                {"FC_4Y_QQ_BPS_CDS","4Y CDS q/q (bps)","4Y CDS q/q (bps)","4Y CDS Quarter on Quarter change in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_4Y_QQ_BPS_CDS_PD","4Y CDS q/q (bps) PD","4Y CDS q/q (bps) PD","4Y CDS Probability of Default Quarter on Quarter change in basis points","base", apiResponse, mongoResponse},
                {"FC_4Y_QQ_PERCENT_CDS","4Y CDS q/q (%)","4Y CDS q/q (%)","4Y CDS Quarter on Quarter percentage change","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_4Y_QQ_PERCENT_CDS_PD","4Y CDS q/q (%) PD","4Y CDS q/q (%) PD","4Y CDS Probability of Default Quarter on Quarter change percent change","base", apiResponse, mongoResponse},
                {"FC_4Y_SS_BPS_CDS","4Y CDS hy/hy (bps)","4Y CDS hy/hy (bps)","4Y CDS Quarter on Half-Year change in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_4Y_SS_BPS_CDS_PD","4Y CDS hy/hy (bps) PD","4Y CDS hy/hy (bps) PD","4Y CDS Probability of Default Half-Year on Half-Year change in basis points","base", apiResponse, mongoResponse},
                {"FC_4Y_SS_PERCENT_CDS","4Y CDS hy/hy (%)","4Y CDS hy/hy (%)","4Y CDS Quarter on Half-Year percentage change","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_4Y_SS_PERCENT_CDS_PD","4Y CDS hy/hy (%) PD","4Y CDS hy/hy (%) PD","4Y CDS Probability of Default Half-Year on Half-Year change percent change","base", apiResponse, mongoResponse},
                {"FC_4Y_WW_BPS_CDS","4Y CDS w/w (bps)","4Y CDS w/w (bps)","4Y CDS Week on Week change in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_4Y_WW_BPS_CDS_PD","4Y CDS w/w (bps) PD","4Y CDS w/w (bps) PD","4Y CDS Probability of Default Week on Week change in basis points","base", apiResponse, mongoResponse},
                {"FC_4Y_WW_PERCENT_CDS","4Y CDS w/w (%)","4Y CDS w/w (%)","4Y CDS Week on Week percentage change","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_4Y_WW_PERCENT_CDS_PD","4Y CDS w/w (%) PD","4Y CDS w/w (%) PD","4Y CDS Probability of Default Week on Week change percent change","base", apiResponse, mongoResponse},
                {"FC_4Y_YY_BPS_CDS","4Y CDS y/y (bps)","4Y CDS y/y (bps)","4Y CDS Year on Year change in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_4Y_YY_BPS_CDS_PD","4Y CDS y/y (bps) PD","4Y CDS y/y (bps) PD","4Y CDS Probability of Default Year on Year change in basis points","base", apiResponse, mongoResponse},
                {"FC_4Y_YY_PERCENT_CDS","4Y CDS y/y (%)","4Y CDS y/y (%)","4Y CDS Year on Year percentage change","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_4Y_YY_PERCENT_CDS_PD","4Y CDS y/y (%) PD","4Y CDS y/y (%) PD","4Y CDS Probability of Default Year on Year change percent change","base", apiResponse, mongoResponse},
                {"FC_6M_BPS_CDS","6M CDS (bps) ","6M CDS (bps)","6M CDS in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_6M_DD_BPS_CDS","6M CDS d/d (bps)","6M CDS d/d (bps)","6M CDS Day on Day change in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_6M_DD_PERCENT_CDS","6M CDS d/d (%)","6M CDS d/d (%)","6M CDS Day on Day percentage change","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_6M_MM_BPS_CDS","6M CDS m/m (bps)","6M CDS m/m (bps)","6M CDS Month on Month change in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_6M_MM_PERCENT_CDS","6M CDS m/m (%)","6M CDS m/m (%)","6M CDS Month on Month percentage change","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_6M_QQ_BPS_CDS","6M CDS q/q (bps)","6M CDS q/q (bps)","6M CDS Quarter on Quarter change in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_6M_QQ_PERCENT_CDS","6M CDS q/q (%)","6M CDS q/q (%)","6M CDS Quarter on Quarter percentage change","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_6M_SS_BPS_CDS","6M CDS hy/hy (bps)","6M CDS hy/hy (bps)","6M CDS Quarter on Half-Year change in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_6M_SS_PERCENT_CDS","6M CDS hy/hy (%)","6M CDS hy/hy (%)","6M CDS Quarter on Half-Year percentage change","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_6M_WW_BPS_CDS","6M CDS w/w (bps)","6M CDS w/w (bps)","6M CDS Week on Week change in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_6M_WW_PERCENT_CDS","6M CDS w/w (%)","6M CDS w/w (%)","6M CDS Week on Week percentage change","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_6M_YY_BPS_CDS","6M CDS y/y (bps)","6M CDS y/y (bps)","6M CDS Year on Year change in basis points","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_6M_YY_PERCENT_CDS","6M CDS y/y (%)","6M CDS y/y (%)","6M CDS Year on Year percentage change","creditDefaultSwaps", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_2Y_BPS_CDS","2Y CDS Risk Benchmark (bps)","2Y CDS Risk Benchmark (bps)","2Y CDS Risk Benchmark in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_4Y_BPS_CDS","4Y CDS Risk Benchmark (bps)","4Y CDS Risk Benchmark (bps)","4Y CDS Risk Benchmark in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_6M_BPS_CDS","6M CDS Risk Benchmark (bps)","6M CDS Risk Benchmark (bps)","6M CDS Risk Benchmark in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_BPS_CDS","2Y CDS Risk Benchmark (bps) PD","2Y CDS Risk Benchmark (bps) PD","2Y CDS Risk Benchmark Probability of Default in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_DD_BPS_CDS","2Y CDS Risk Benchmark d/d (bps) PD","2Y CDS Risk Benchmark d/d (bps) PD","2Y CDS Risk Benchmark Probability of Default Day on Day change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_DD_PERCENT_CDS","2Y CDS Risk Benchmark d/d (%) PD","2Y CDS Risk Benchmark d/d (%) PD","2Y CDS Risk Benchmark Probability of Default Day on Day percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_MM_BPS_CDS","2Y CDS Risk Benchmark m/m (bps) PD","2Y CDS Risk Benchmark m/m (bps) PD","2Y CDS Risk Benchmark Probability of Default Month on Month change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_MM_PERCENT_CDS","2Y CDS Risk Benchmark m/m (%) PD","2Y CDS Risk Benchmark m/m (%) PD","2Y CDS Risk Benchmark Probability of Default Month on Month percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_QQ_BPS_CDS","2Y CDS Risk Benchmark q/q (bps) PD","2Y CDS Risk Benchmark q/q (bps) PD","2Y CDS Risk Benchmark Probability of Default Quater on Quater change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_QQ_PERCENT_CDS","2Y CDS Risk Benchmark q/q (%) PD","2Y CDS Risk Benchmark q/q (%) PD","2Y CDS Risk Benchmark Probability of Default Quater on Quater percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_SS_BPS_CDS","2Y CDS Risk Benchmark hy/hy (bps) PD","2Y CDS Risk Benchmark hy/hy (bps) PD","2Y CDS Risk Benchmark Probability of Default Half-Year on Half-Year change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_SS_PERCENT_CDS","2Y CDS Risk Benchmark hy/hy (%) PD","2Y CDS Risk Benchmark hy/hy (%) PD","2Y CDS Risk Benchmark Probability of Default Half-Year on Healf-Year percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_WW_BPS_CDS","2Y CDS Risk Benchmark w/w (bps) PD","2Y CDS Risk Benchmark w/w (bps) PD","2Y CDS Risk Benchmark Probability of Default Week on Week change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_WW_PERCENT_CDS","2Y CDS Risk Benchmark w/w (%) PD","2Y CDS Risk Benchmark w/w (%) PD","2Y CDS Risk Benchmark Probability of Default Week on Week percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_YY_BPS_CDS","2Y CDS Risk Benchmark y/y (bps) PD","2Y CDS Risk Benchmark y/y (bps) PD","2Y CDS Risk Benchmark Probability of Default Year on Year change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_YY_PERCENT_CDS","2Y CDS Risk Benchmark y/y (%) PD","2Y CDS Risk Benchmark y/y (%) PD","2Y CDS Risk Benchmark Probability of Default Year on Year percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_BPS_CDS","4Y CDS Risk Benchmark (bps) PD","4Y CDS Risk Benchmark (bps)","4Y CDS Risk Benchmark Probability of Default in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_DD_BPS_CDS","4Y CDS Risk Benchmark d/d (bps) PD","4Y CDS Risk Benchmark d/d (bps)","4Y CDS Risk Benchmark Probability of Default Day on Day change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_DD_PERCENT_CDS","4Y CDS Risk Benchmark d/d (%) PD","4Y CDS Risk Benchmark d/d (%)","4Y CDS Risk Benchmark Probability of Default Day on Day percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_MM_BPS_CDS","4Y CDS Risk Benchmark m/m (bps) PD","4Y CDS Risk Benchmark m/m (bps)","4Y CDS Risk Benchmark Probability of Default Month on Month change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_MM_PERCENT_CDS","4Y CDS Risk Benchmark m/m (%) PD","4Y CDS Risk Benchmark m/m (%)","4Y CDS Risk Benchmark Probability of Default Month on Month percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_QQ_BPS_CDS","4Y CDS Risk Benchmark q/q (bps) PD","4Y CDS Risk Benchmark q/q (bps)","4Y CDS Risk Benchmark Probability of Default Quater on Quater change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_QQ_PERCENT_CDS","4Y CDS Risk Benchmark q/q (%) PD","4Y CDS Risk Benchmark q/q (%)","4Y CDS Risk Benchmark Probability of Default Quater on Quater percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_SS_BPS_CDS","4Y CDS Risk Benchmark hy/hy (bps) PD","4Y CDS Risk Benchmark hy/hy (bps)","4Y CDS Risk Benchmark Probability of Default Half-Year on Half-Year change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_SS_PERCENT_CDS","4Y CDS Risk Benchmark hy/hy (%) PD","4Y CDS Risk Benchmark hy/hy (%)","4Y CDS Risk Benchmark Probability of Default Half-Year on Healf-Year percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_WW_BPS_CDS","4Y CDS Risk Benchmark w/w (bps) PD","4Y CDS Risk Benchmark w/w (bps)","4Y CDS Risk Benchmark Probability of Default Week on Week change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_WW_PERCENT_CDS","4Y CDS Risk Benchmark w/w (%) PD","4Y CDS Risk Benchmark w/w (%)","4Y CDS Risk Benchmark Probability of Default Week on Week percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_YY_BPS_CDS","4Y CDS Risk Benchmark y/y (bps) PD","4Y CDS Risk Benchmark y/y (bps)","4Y CDS Risk Benchmark Probability of Default Year on Year change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_YY_PERCENT_CDS","4Y CDS Risk Benchmark y/y (%) PD","4Y CDS Risk Benchmark y/y (%)","4Y CDS Risk Benchmark Probability of Default Year on Year percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_6M_DD_BPS_CDS","6M CDS Risk Benchmark d/d (bps) PD","6M CDS Risk Benchmark d/d (bps) PD","6M CDS Risk Benchmark Probability of Default Day on Day change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_6M_DD_PERCENT_CDS","6M CDS Risk Benchmark d/d (%) PD","6M CDS Risk Benchmark d/d (%) PD","6M CDS Risk Benchmark Probability of Default Day on Day percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_6M_MM_BPS_CDS","6M CDS Risk Benchmark m/m (bps) PD","6M CDS Risk Benchmark m/m (bps) PD","6M CDS Risk Benchmark Probability of Default Month on Month change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_6M_MM_PERCENT_CDS","6M CDS Risk Benchmark m/m (%) PD","6M CDS Risk Benchmark m/m (%) PD","6M CDS Risk Benchmark Probability of Default Month on Month percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_6M_QQ_BPS_CDS","6M CDS Risk Benchmark q/q (bps) PD","6M CDS Risk Benchmark q/q (bps) PD","6M CDS Risk Benchmark Probability of Default Quater on Quater change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_6M_QQ_PERCENT_CDS","6M CDS Risk Benchmark q/q (%) PD","6M CDS Risk Benchmark q/q (%) PD","6M CDS Risk Benchmark Probability of Default Quater on Quater percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_6M_SS_BPS_CDS","6M CDS Risk Benchmark hy/hy (bps) PD","6M CDS Risk Benchmark hy/hy (bps) PD","6M CDS Risk Benchmark Probability of Default Half-Year on Half-Year change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_6M_SS_PERCENT_CDS","6M CDS Risk Benchmark hy/hy (%) PD","6M CDS Risk Benchmark hy/hy (%) PD","6M CDS Risk Benchmark Probability of Default Half-Year on Healf-Year percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_6M_WW_BPS_CDS","6M CDS Risk Benchmark w/w (bps) PD","6M CDS Risk Benchmark w/w (bps) PD","6M CDS Risk Benchmark Probability of Default Week on Week change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_6M_WW_PERCENT_CDS","6M CDS Risk Benchmark w/w (%) PD","6M CDS Risk Benchmark w/w (%) PD","6M CDS Risk Benchmark Probability of Default Week on Week percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_6M_YY_BPS_CDS","6M CDS Risk Benchmark y/y (bps) PD","6M CDS Risk Benchmark y/y (bps) PD","6M CDS Risk Benchmark Probability of Default Year on Year change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_PD_6M_YY_PERCENT_CDS","6M CDS Risk Benchmark y/y (%) PD","6M CDS Risk Benchmark y/y (%) PD","6M CDS Risk Benchmark Probability of Default Year on Year percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_DD_BPS_CDS","2Y CDS Risk Benchmark d/d (bps)","2Y CDS Risk Benchmark d/d (bps)","2Y CDS Risk Benchmar Spread Day on Day change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_DD_PERCENT_CDS","2Y CDS Risk Benchmark d/d (%)","2Y CDS Risk Benchmark d/d (%)","2Y CDS Risk Benchmark Spread Day on Day percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_MM_BPS_CDS","2Y CDS Risk Benchmark m/m (bps)","2Y CDS Risk Benchmark m/m (bps)","2Y CDS Risk Benchmark Spread Month on Month change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_MM_PERCENT_CDS","2Y CDS Risk Benchmark m/m (%)","2Y CDS Risk Benchmark m/m (%)","2Y CDS Risk Benchmark Spread Month on Month percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_QQ_BPS_CDS","2Y CDS Risk Benchmark q/q (bps)","2Y CDS Risk Benchmark q/q (bps)","2Y CDS Risk Benchmark Spread Quater on Quater change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_QQ_PERCENT_CDS","2Y CDS Risk Benchmark q/q (%)","2Y CDS Risk Benchmark q/q (%)","2Y CDS Risk Benchmark Spread Quater on Quater percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_SS_BPS_CDS","2Y CDS Risk Benchmark hy/hy (bps)","2Y CDS Risk Benchmark hy/hy (bps)","2Y CDS Risk Benchmark Spread Half-Year on Half-Year change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_SS_PERCENT_CDS","2Y CDS Risk Benchmark hy/hy (%)","2Y CDS Risk Benchmark hy/hy (%)","2Y CDS Risk Benchmark Spread Half-Year on Healf-Year percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_WW_BPS_CDS","2Y CDS Risk Benchmark w/w (bps)","2Y CDS Risk Benchmark w/w (bps)","2Y CDS Risk Benchmark Spread Week on Week change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_WW_PERCENT_CDS","2Y CDS Risk Benchmark w/w (%)","2Y CDS Risk Benchmark w/w (%)","2Y CDS Risk Benchmark Spread Week on Week percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_YY_BPS_CDS","2Y CDS Risk Benchmark y/y (bps)","2Y CDS Risk Benchmark y/y (bps)","2Y CDS Risk Benchmark Spread Year on Year change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_YY_PERCENT_CDS","2Y CDS Risk Benchmark y/y (%)","2Y CDS Risk Benchmark y/y (%)","2Y CDS Risk Benchmark Spread Year on Year percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_DD_BPS_CDS","4Y CDS Risk Benchmark d/d (bps)","4Y CDS Risk Benchmark d/d (bps)","4Y CDS Risk Benchmar Spread Day on Day change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_DD_PERCENT_CDS","4Y CDS Risk Benchmark d/d (%)","4Y CDS Risk Benchmark d/d (%)","4Y CDS Risk Benchmark Spread Day on Day percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_MM_BPS_CDS","4Y CDS Risk Benchmark m/m (bps)","4Y CDS Risk Benchmark m/m (bps)","4Y CDS Risk Benchmark Spread Month on Month change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_MM_PERCENT_CDS","4Y CDS Risk Benchmark m/m (%)","4Y CDS Risk Benchmark m/m (%)","4Y CDS Risk Benchmark Spread Month on Month percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_QQ_BPS_CDS","4Y CDS Risk Benchmark q/q (bps)","4Y CDS Risk Benchmark q/q (bps)","4Y CDS Risk Benchmark Spread Quater on Quater change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_QQ_PERCENT_CDS","4Y CDS Risk Benchmark q/q (%)","4Y CDS Risk Benchmark q/q (%)","4Y CDS Risk Benchmark Spread Quater on Quater percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_SS_BPS_CDS","4Y CDS Risk Benchmark hy/hy (bps)","4Y CDS Risk Benchmark hy/hy (bps)","4Y CDS Risk Benchmark Spread Half-Year on Half-Year change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_SS_PERCENT_CDS","4Y CDS Risk Benchmark hy/hy (%)","4Y CDS Risk Benchmark hy/hy (%)","4Y CDS Risk Benchmark Spread Half-Year on Healf-Year percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_WW_BPS_CDS","4Y CDS Risk Benchmark w/w (bps)","4Y CDS Risk Benchmark w/w (bps)","4Y CDS Risk Benchmark Spread Week on Week change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_WW_PERCENT_CDS","4Y CDS Risk Benchmark w/w (%)","4Y CDS Risk Benchmark w/w (%)","4Y CDS Risk Benchmark Spread Week on Week percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_YY_BPS_CDS","4Y CDS Risk Benchmark y/y (bps)","4Y CDS Risk Benchmark y/y (bps)","4Y CDS Risk Benchmark Spread Year on Year change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_YY_PERCENT_CDS","4Y CDS Risk Benchmark y/y (%)","4Y CDS Risk Benchmark y/y (%)","4Y CDS Risk Benchmark Spread Year on Year percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_DD_BPS_CDS","6M CDS Risk Benchmark d/d (bps)","6M CDS Risk Benchmark d/d (bps)","6M CDS Risk Benchmar Spread Day on Day change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_DD_PERCENT_CDS","6M CDS Risk Benchmark d/d (%)","6M CDS Risk Benchmark d/d (%)","6M CDS Risk Benchmark Spread Day on Day percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_MM_BPS_CDS","6M CDS Risk Benchmark m/m (bps)","6M CDS Risk Benchmark m/m (bps)","6M CDS Risk Benchmark Spread Month on Month change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_MM_PERCENT_CDS","6M CDS Risk Benchmark m/m (%)","6M CDS Risk Benchmark m/m (%)","6M CDS Risk Benchmark Spread Month on Month percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_QQ_BPS_CDS","6M CDS Risk Benchmark q/q (bps)","6M CDS Risk Benchmark q/q (bps)","6M CDS Risk Benchmark Spread Quater on Quater change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_QQ_PERCENT_CDS","6M CDS Risk Benchmark q/q (%)","6M CDS Risk Benchmark q/q (%)","6M CDS Risk Benchmark Spread Quater on Quater percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_SS_BPS_CDS","6M CDS Risk Benchmark hy/hy (bps)","6M CDS Risk Benchmark hy/hy (bps)","6M CDS Risk Benchmark Spread Half-Year on Half-Year change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_SS_PERCENT_CDS","6M CDS Risk Benchmark hy/hy (%)","6M CDS Risk Benchmark hy/hy (%)","6M CDS Risk Benchmark Spread Half-Year on Healf-Year percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_WW_BPS_CDS","6M CDS Risk Benchmark w/w (bps)","6M CDS Risk Benchmark w/w (bps)","6M CDS Risk Benchmark Spread Week on Week change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_WW_PERCENT_CDS","6M CDS Risk Benchmark w/w (%)","6M CDS Risk Benchmark w/w (%)","6M CDS Risk Benchmark Spread Week on Week percentage change","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_YY_BPS_CDS","6M CDS Risk Benchmark y/y (bps)","6M CDS Risk Benchmark y/y (bps)","6M CDS Risk Benchmark Spread Year on Year change in basis points","base", apiResponse, mongoResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_YY_PERCENT_CDS","6M CDS Risk Benchmark y/y (%)","6M CDS Risk Benchmark y/y (%)","6M CDS Risk Benchmark Spread Year on Year percentage change","base", apiResponse, mongoResponse},
        };
    }

    @Test(dataProvider = "Fisc6354")
    public void Fisc6354_MONGO_cdsMetadataEnhacements(String fitchFieldId, String displayName, String fitchFieldDesc, String fieldDefinition, String permissionsRequired, String apiResponse, String mongoResponse){
        try {
            Assert.assertTrue(mongoResponse.contains("\"fitchFieldId\": \"" + fitchFieldId + "\""));
            Assert.assertTrue(mongoResponse.contains("\"displayName\": \"" + displayName + "\""));
            Assert.assertTrue(mongoResponse.contains("\"fitchFieldDesc\": \"" + fitchFieldDesc + "\""));
            Assert.assertTrue(mongoResponse.contains("\"fieldDefinition\": \"" + fieldDefinition + "\""));
            logger.info("FISC 6354 MONGO DATA PASSED! Tested FITCHFIELDID: " + fitchFieldId + " DISPLAYNAME: " + displayName + " FITCHFIELDDESC " + fitchFieldDesc + " FIELDDEFINITION " + fieldDefinition + " PERMISSION " + permissionsRequired);
        } catch (AssertionError err){
            logger.error("FISC 6354 MONGO DATA FAILED! Tested FITCHFIELDID: "  + fitchFieldId + " ERROR: " + err);
            Assert.fail();
        }
    }

    @Test(dataProvider = "Fisc6354")
    public void Fisc6354_API_cdsMetadataEnhacements(String fitchFieldId, String displayName, String fitchFieldDesc, String fieldDefinition, String permissionsRequired, String apiResponse, String mongoResponse){
        try {
            Assert.assertTrue(apiResponse.contains("\"id\":\"" + fitchFieldId + "\""));
            Assert.assertTrue(apiResponse.contains("\"displayName\":\"" + displayName + "\""));
            Assert.assertTrue(apiResponse.contains("\"fitchFieldDesc\":\"" + fitchFieldDesc + "\""));
            Assert.assertTrue(apiResponse.contains("\"fieldDefinition\":\"" + fieldDefinition + "\""));
            logger.info("FISC 6354 API RESOURCEFUL ENDPOINT DATA PASSED! Tested FITCHFIELDID: " + fitchFieldId + " DISPLAYNAME: " + displayName + " FITCHFIELDDESC " + fitchFieldDesc + " FIELDDEFINITION " + fieldDefinition + " PERMISSION " + permissionsRequired);
        } catch (AssertionError err){
            logger.error("FISC 6354 API RESOURCEFUL ENDPOINT DATA FAILED! Tested FITCHFIELDID: "  + fitchFieldId + " ERROR: " + err);
            Assert.fail();
        }
    }

    @DataProvider(name="Fisc6373")
    public Object[][] getDataFor6373() throws IOException {

        String apiResponse = apiUtils.postToDataAggregator("6373.json", AuthrztionValue, XappClintIDvalue, dataPostUrl).asString();
        return new Object[][] {
                {"FC_2Y_BPS_CDS","2Y CDS (bps)","2Y CDS (bps)","2Y CDS in basis points","creditDefaultSwaps", apiResponse},
                {"FC_2Y_BPS_CDS_PD","2Y CDS (bps) PD","2Y CDS (bps) PD","2Y CDS Probability of Default in basis points","base", apiResponse},
                {"FC_2Y_DD_BPS_CDS","2Y CDS d/d (bps)","2Y CDS d/d (bps)","2Y CDS Day on Day change in basis points","creditDefaultSwaps", apiResponse},
                {"FC_2Y_DD_BPS_CDS_PD","2Y CDS d/d (bps) PD","2Y CDS d/d (bps) PD","2Y CDS Probability of Default Day on Day change in basis points","base", apiResponse},
                {"FC_2Y_DD_PERCENT_CDS","2Y CDS d/d (%)","2Y CDS d/d (%)","2Y CDS Day on Day percentage change","creditDefaultSwaps", apiResponse},
                {"FC_2Y_DD_PERCENT_CDS_PD","2Y CDS d/d (%) PD","2Y CDS d/d (%) PD","2Y CDS Probability of Default Day on Day change percent change","base", apiResponse},
                {"FC_2Y_MM_BPS_CDS","2Y CDS m/m (bps)","2Y CDS m/m (bps)","2Y CDS Month on Month change in basis points","creditDefaultSwaps", apiResponse},
                {"FC_2Y_MM_BPS_CDS_PD","2Y CDS m/m (bps) PD","2Y CDS m/m (bps) PD","2Y CDS Probability of Default Month on Month change in basis points","base", apiResponse},
                {"FC_2Y_MM_PERCENT_CDS","2Y CDS m/m (%)","2Y CDS m/m (%)","2Y CDS Month on Month percentage change","creditDefaultSwaps", apiResponse},
                {"FC_2Y_MM_PERCENT_CDS_PD","2Y CDS m/m (%) PD","2Y CDS m/m (%) PD","2Y CDS Probability of Default Month on Month change percent change","base", apiResponse},
                {"FC_2Y_QQ_BPS_CDS","2Y CDS q/q (bps)","2Y CDS q/q (bps)","2Y CDS Quarter on Quarter change in basis points","creditDefaultSwaps", apiResponse},
                {"FC_2Y_QQ_BPS_CDS_PD","2Y CDS q/q (bps) PD","2Y CDS q/q (bps) PD","2Y CDS Probability of Default Quarter on Quarter change in basis points","base", apiResponse},
                {"FC_2Y_QQ_PERCENT_CDS","2Y CDS q/q (%)","2Y CDS q/q (%)","2Y CDS Quarter on Quarter percentage change","creditDefaultSwaps", apiResponse},
                {"FC_2Y_QQ_PERCENT_CDS_PD","2Y CDS q/q (%) PD","2Y CDS q/q (%) PD","2Y CDS Probability of Default Quarter on Quarter change percent change","base", apiResponse},
                {"FC_2Y_SS_BPS_CDS","2Y CDS hy/hy (bps)","2Y CDS hy/hy (bps)","2Y CDS Quarter on Half-Year change in basis points","creditDefaultSwaps", apiResponse},
                {"FC_2Y_SS_BPS_CDS_PD","2Y CDS hy/hy (bps) PD","2Y CDS hy/hy (bps) PD","2Y CDS Probability of Default Half-Year on Half-Year change in basis points","base", apiResponse},
                {"FC_2Y_SS_PERCENT_CDS","2Y CDS hy/hy (%)","2Y CDS hy/hy (%)","2Y CDS Quarter on Half-Year percentage change","creditDefaultSwaps", apiResponse},
                {"FC_2Y_SS_PERCENT_CDS_PD","2Y CDS hy/hy (%) PD","2Y CDS hy/hy (%) PD","2Y CDS Probability of Default Half-Year on Half-Year change percent change","base", apiResponse},
                {"FC_2Y_WW_BPS_CDS","2Y CDS w/w (bps)","2Y CDS w/w (bps)","2Y CDS Week on Week change in basis points","creditDefaultSwaps", apiResponse},
                {"FC_2Y_WW_BPS_CDS_PD","2Y CDS w/w (bps) PD","2Y CDS w/w (bps) PD","2Y CDS Probability of Default Week on Week change in basis points","base", apiResponse},
                {"FC_2Y_WW_PERCENT_CDS","2Y CDS w/w (%)","2Y CDS w/w (%)","2Y CDS Week on Week percentage change","creditDefaultSwaps", apiResponse},
                {"FC_2Y_WW_PERCENT_CDS_PD","2Y CDS w/w (%) PD","2Y CDS w/w (%) PD","2Y CDS Probability of Default Week on Week change percent change","base", apiResponse},
                {"FC_2Y_YY_BPS_CDS","2Y CDS y/y (bps)","2Y CDS y/y (bps)","2Y CDS Year on Year change in basis points","creditDefaultSwaps", apiResponse},
                {"FC_2Y_YY_BPS_CDS_PD","2Y CDS y/y (bps) PD","2Y CDS y/y (bps) PD","2Y CDS Probability of Default Year on Year change in basis points","base", apiResponse},
                {"FC_2Y_YY_PERCENT_CDS","2Y CDS y/y (%)","2Y CDS y/y (%)","2Y CDS Year on Year percentage change","creditDefaultSwaps", apiResponse},
                {"FC_2Y_YY_PERCENT_CDS_PD","2Y CDS y/y (%) PD","2Y CDS y/y (%) PD","2Y CDS Probability of Default Year on Year change percent change","base", apiResponse},
                {"FC_4Y_BPS_CDS","4Y CDS (bps)","4Y CDS (bps)","4Y CDS in basis points","creditDefaultSwaps", apiResponse},
                {"FC_4Y_BPS_CDS_PD","4Y CDS (bps) PD","4Y CDS (bps) PD","4Y CDS Probability of Default in basis points","base", apiResponse},
                {"FC_4Y_DD_BPS_CDS","4Y CDS d/d (bps)","4Y CDS d/d (bps)","4Y CDS Day on Day change in basis points","creditDefaultSwaps", apiResponse},
                {"FC_4Y_DD_BPS_CDS_PD","4Y CDS d/d (bps) PD","4Y CDS d/d (bps) PD","4Y CDS Probability of Default Day on Day change in basis points","base", apiResponse},
                {"FC_4Y_DD_PERCENT_CDS","4Y CDS d/d (%)","4Y CDS d/d (%)","4Y CDS Day on Day percentage change","creditDefaultSwaps", apiResponse},
                {"FC_4Y_DD_PERCENT_CDS_PD","4Y CDS d/d (%) PD","4Y CDS d/d (%) PD","4Y CDS Probability of Default Day on Day change percent change","base", apiResponse},
                {"FC_4Y_MM_BPS_CDS","4Y CDS m/m (bps)","4Y CDS m/m (bps)","4Y CDS Month on Month change in basis points","creditDefaultSwaps", apiResponse},
                {"FC_4Y_MM_BPS_CDS_PD","4Y CDS m/m (bps) PD","4Y CDS m/m (bps) PD","4Y CDS Probability of Default Month on Month change in basis points","base", apiResponse},
                {"FC_4Y_MM_PERCENT_CDS","4Y CDS m/m (%)","4Y CDS m/m (%)","4Y CDS Month on Month percentage change","creditDefaultSwaps", apiResponse},
                {"FC_4Y_MM_PERCENT_CDS_PD","4Y CDS m/m (%) PD","4Y CDS m/m (%) PD","4Y CDS Probability of Default Month on Month change percent change","base", apiResponse},
                {"FC_4Y_QQ_BPS_CDS","4Y CDS q/q (bps)","4Y CDS q/q (bps)","4Y CDS Quarter on Quarter change in basis points","creditDefaultSwaps", apiResponse},
                {"FC_4Y_QQ_BPS_CDS_PD","4Y CDS q/q (bps) PD","4Y CDS q/q (bps) PD","4Y CDS Probability of Default Quarter on Quarter change in basis points","base", apiResponse},
                {"FC_4Y_QQ_PERCENT_CDS","4Y CDS q/q (%)","4Y CDS q/q (%)","4Y CDS Quarter on Quarter percentage change","creditDefaultSwaps", apiResponse},
                {"FC_4Y_QQ_PERCENT_CDS_PD","4Y CDS q/q (%) PD","4Y CDS q/q (%) PD","4Y CDS Probability of Default Quarter on Quarter change percent change","base", apiResponse},
                {"FC_4Y_SS_BPS_CDS","4Y CDS hy/hy (bps)","4Y CDS hy/hy (bps)","4Y CDS Quarter on Half-Year change in basis points","creditDefaultSwaps", apiResponse},
                {"FC_4Y_SS_BPS_CDS_PD","4Y CDS hy/hy (bps) PD","4Y CDS hy/hy (bps) PD","4Y CDS Probability of Default Half-Year on Half-Year change in basis points","base", apiResponse},
                {"FC_4Y_SS_PERCENT_CDS","4Y CDS hy/hy (%)","4Y CDS hy/hy (%)","4Y CDS Quarter on Half-Year percentage change","creditDefaultSwaps", apiResponse},
                {"FC_4Y_SS_PERCENT_CDS_PD","4Y CDS hy/hy (%) PD","4Y CDS hy/hy (%) PD","4Y CDS Probability of Default Half-Year on Half-Year change percent change","base", apiResponse},
                {"FC_4Y_WW_BPS_CDS","4Y CDS w/w (bps)","4Y CDS w/w (bps)","4Y CDS Week on Week change in basis points","creditDefaultSwaps", apiResponse},
                {"FC_4Y_WW_BPS_CDS_PD","4Y CDS w/w (bps) PD","4Y CDS w/w (bps) PD","4Y CDS Probability of Default Week on Week change in basis points","base", apiResponse},
                {"FC_4Y_WW_PERCENT_CDS","4Y CDS w/w (%)","4Y CDS w/w (%)","4Y CDS Week on Week percentage change","creditDefaultSwaps", apiResponse},
                {"FC_4Y_WW_PERCENT_CDS_PD","4Y CDS w/w (%) PD","4Y CDS w/w (%) PD","4Y CDS Probability of Default Week on Week change percent change","base", apiResponse},
                {"FC_4Y_YY_BPS_CDS","4Y CDS y/y (bps)","4Y CDS y/y (bps)","4Y CDS Year on Year change in basis points","creditDefaultSwaps", apiResponse},
                {"FC_4Y_YY_BPS_CDS_PD","4Y CDS y/y (bps) PD","4Y CDS y/y (bps) PD","4Y CDS Probability of Default Year on Year change in basis points","base", apiResponse},
                {"FC_4Y_YY_PERCENT_CDS","4Y CDS y/y (%)","4Y CDS y/y (%)","4Y CDS Year on Year percentage change","creditDefaultSwaps", apiResponse},
                {"FC_4Y_YY_PERCENT_CDS_PD","4Y CDS y/y (%) PD","4Y CDS y/y (%) PD","4Y CDS Probability of Default Year on Year change percent change","base", apiResponse},
                {"FC_6M_BPS_CDS","6M CDS (bps) ","6M CDS (bps)","6M CDS in basis points","creditDefaultSwaps", apiResponse},
                {"FC_6M_DD_BPS_CDS","6M CDS d/d (bps)","6M CDS d/d (bps)","6M CDS Day on Day change in basis points","creditDefaultSwaps", apiResponse},
                {"FC_6M_DD_PERCENT_CDS","6M CDS d/d (%)","6M CDS d/d (%)","6M CDS Day on Day percentage change","creditDefaultSwaps", apiResponse},
                {"FC_6M_MM_BPS_CDS","6M CDS m/m (bps)","6M CDS m/m (bps)","6M CDS Month on Month change in basis points","creditDefaultSwaps", apiResponse},
                {"FC_6M_MM_PERCENT_CDS","6M CDS m/m (%)","6M CDS m/m (%)","6M CDS Month on Month percentage change","creditDefaultSwaps", apiResponse},
                {"FC_6M_QQ_BPS_CDS","6M CDS q/q (bps)","6M CDS q/q (bps)","6M CDS Quarter on Quarter change in basis points","creditDefaultSwaps", apiResponse},
                {"FC_6M_QQ_PERCENT_CDS","6M CDS q/q (%)","6M CDS q/q (%)","6M CDS Quarter on Quarter percentage change","creditDefaultSwaps", apiResponse},
                {"FC_6M_SS_BPS_CDS","6M CDS hy/hy (bps)","6M CDS hy/hy (bps)","6M CDS Quarter on Half-Year change in basis points","creditDefaultSwaps", apiResponse},
                {"FC_6M_SS_PERCENT_CDS","6M CDS hy/hy (%)","6M CDS hy/hy (%)","6M CDS Quarter on Half-Year percentage change","creditDefaultSwaps", apiResponse},
                {"FC_6M_WW_BPS_CDS","6M CDS w/w (bps)","6M CDS w/w (bps)","6M CDS Week on Week change in basis points","creditDefaultSwaps", apiResponse},
                {"FC_6M_WW_PERCENT_CDS","6M CDS w/w (%)","6M CDS w/w (%)","6M CDS Week on Week percentage change","creditDefaultSwaps", apiResponse},
                {"FC_6M_YY_BPS_CDS","6M CDS y/y (bps)","6M CDS y/y (bps)","6M CDS Year on Year change in basis points","creditDefaultSwaps", apiResponse},
                {"FC_6M_YY_PERCENT_CDS","6M CDS y/y (%)","6M CDS y/y (%)","6M CDS Year on Year percentage change","creditDefaultSwaps", apiResponse},
                {"FC_RISK_BENCHMARK_2Y_BPS_CDS","2Y CDS Risk Benchmark (bps)","2Y CDS Risk Benchmark (bps)","2Y CDS Risk Benchmark in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_4Y_BPS_CDS","4Y CDS Risk Benchmark (bps)","4Y CDS Risk Benchmark (bps)","4Y CDS Risk Benchmark in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_6M_BPS_CDS","6M CDS Risk Benchmark (bps)","6M CDS Risk Benchmark (bps)","6M CDS Risk Benchmark in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_BPS_CDS","2Y CDS Risk Benchmark (bps) PD","2Y CDS Risk Benchmark (bps) PD","2Y CDS Risk Benchmark Probability of Default in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_DD_BPS_CDS","2Y CDS Risk Benchmark d/d (bps) PD","2Y CDS Risk Benchmark d/d (bps) PD","2Y CDS Risk Benchmark Probability of Default Day on Day change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_DD_PERCENT_CDS","2Y CDS Risk Benchmark d/d (%) PD","2Y CDS Risk Benchmark d/d (%) PD","2Y CDS Risk Benchmark Probability of Default Day on Day percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_MM_BPS_CDS","2Y CDS Risk Benchmark m/m (bps) PD","2Y CDS Risk Benchmark m/m (bps) PD","2Y CDS Risk Benchmark Probability of Default Month on Month change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_MM_PERCENT_CDS","2Y CDS Risk Benchmark m/m (%) PD","2Y CDS Risk Benchmark m/m (%) PD","2Y CDS Risk Benchmark Probability of Default Month on Month percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_QQ_BPS_CDS","2Y CDS Risk Benchmark q/q (bps) PD","2Y CDS Risk Benchmark q/q (bps) PD","2Y CDS Risk Benchmark Probability of Default Quater on Quater change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_QQ_PERCENT_CDS","2Y CDS Risk Benchmark q/q (%) PD","2Y CDS Risk Benchmark q/q (%) PD","2Y CDS Risk Benchmark Probability of Default Quater on Quater percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_SS_BPS_CDS","2Y CDS Risk Benchmark hy/hy (bps) PD","2Y CDS Risk Benchmark hy/hy (bps) PD","2Y CDS Risk Benchmark Probability of Default Half-Year on Half-Year change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_SS_PERCENT_CDS","2Y CDS Risk Benchmark hy/hy (%) PD","2Y CDS Risk Benchmark hy/hy (%) PD","2Y CDS Risk Benchmark Probability of Default Half-Year on Healf-Year percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_WW_BPS_CDS","2Y CDS Risk Benchmark w/w (bps) PD","2Y CDS Risk Benchmark w/w (bps) PD","2Y CDS Risk Benchmark Probability of Default Week on Week change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_WW_PERCENT_CDS","2Y CDS Risk Benchmark w/w (%) PD","2Y CDS Risk Benchmark w/w (%) PD","2Y CDS Risk Benchmark Probability of Default Week on Week percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_YY_BPS_CDS","2Y CDS Risk Benchmark y/y (bps) PD","2Y CDS Risk Benchmark y/y (bps) PD","2Y CDS Risk Benchmark Probability of Default Year on Year change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_2Y_YY_PERCENT_CDS","2Y CDS Risk Benchmark y/y (%) PD","2Y CDS Risk Benchmark y/y (%) PD","2Y CDS Risk Benchmark Probability of Default Year on Year percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_BPS_CDS","4Y CDS Risk Benchmark (bps) PD","4Y CDS Risk Benchmark (bps)","4Y CDS Risk Benchmark Probability of Default in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_DD_BPS_CDS","4Y CDS Risk Benchmark d/d (bps) PD","4Y CDS Risk Benchmark d/d (bps)","4Y CDS Risk Benchmark Probability of Default Day on Day change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_DD_PERCENT_CDS","4Y CDS Risk Benchmark d/d (%) PD","4Y CDS Risk Benchmark d/d (%)","4Y CDS Risk Benchmark Probability of Default Day on Day percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_MM_BPS_CDS","4Y CDS Risk Benchmark m/m (bps) PD","4Y CDS Risk Benchmark m/m (bps)","4Y CDS Risk Benchmark Probability of Default Month on Month change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_MM_PERCENT_CDS","4Y CDS Risk Benchmark m/m (%) PD","4Y CDS Risk Benchmark m/m (%)","4Y CDS Risk Benchmark Probability of Default Month on Month percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_QQ_BPS_CDS","4Y CDS Risk Benchmark q/q (bps) PD","4Y CDS Risk Benchmark q/q (bps)","4Y CDS Risk Benchmark Probability of Default Quater on Quater change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_QQ_PERCENT_CDS","4Y CDS Risk Benchmark q/q (%) PD","4Y CDS Risk Benchmark q/q (%)","4Y CDS Risk Benchmark Probability of Default Quater on Quater percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_SS_BPS_CDS","4Y CDS Risk Benchmark hy/hy (bps) PD","4Y CDS Risk Benchmark hy/hy (bps)","4Y CDS Risk Benchmark Probability of Default Half-Year on Half-Year change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_SS_PERCENT_CDS","4Y CDS Risk Benchmark hy/hy (%) PD","4Y CDS Risk Benchmark hy/hy (%)","4Y CDS Risk Benchmark Probability of Default Half-Year on Healf-Year percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_WW_BPS_CDS","4Y CDS Risk Benchmark w/w (bps) PD","4Y CDS Risk Benchmark w/w (bps)","4Y CDS Risk Benchmark Probability of Default Week on Week change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_WW_PERCENT_CDS","4Y CDS Risk Benchmark w/w (%) PD","4Y CDS Risk Benchmark w/w (%)","4Y CDS Risk Benchmark Probability of Default Week on Week percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_YY_BPS_CDS","4Y CDS Risk Benchmark y/y (bps) PD","4Y CDS Risk Benchmark y/y (bps)","4Y CDS Risk Benchmark Probability of Default Year on Year change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_4Y_YY_PERCENT_CDS","4Y CDS Risk Benchmark y/y (%) PD","4Y CDS Risk Benchmark y/y (%)","4Y CDS Risk Benchmark Probability of Default Year on Year percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_6M_DD_BPS_CDS","6M CDS Risk Benchmark d/d (bps) PD","6M CDS Risk Benchmark d/d (bps) PD","6M CDS Risk Benchmark Probability of Default Day on Day change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_6M_DD_PERCENT_CDS","6M CDS Risk Benchmark d/d (%) PD","6M CDS Risk Benchmark d/d (%) PD","6M CDS Risk Benchmark Probability of Default Day on Day percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_6M_MM_BPS_CDS","6M CDS Risk Benchmark m/m (bps) PD","6M CDS Risk Benchmark m/m (bps) PD","6M CDS Risk Benchmark Probability of Default Month on Month change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_6M_MM_PERCENT_CDS","6M CDS Risk Benchmark m/m (%) PD","6M CDS Risk Benchmark m/m (%) PD","6M CDS Risk Benchmark Probability of Default Month on Month percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_6M_QQ_BPS_CDS","6M CDS Risk Benchmark q/q (bps) PD","6M CDS Risk Benchmark q/q (bps) PD","6M CDS Risk Benchmark Probability of Default Quater on Quater change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_6M_QQ_PERCENT_CDS","6M CDS Risk Benchmark q/q (%) PD","6M CDS Risk Benchmark q/q (%) PD","6M CDS Risk Benchmark Probability of Default Quater on Quater percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_6M_SS_BPS_CDS","6M CDS Risk Benchmark hy/hy (bps) PD","6M CDS Risk Benchmark hy/hy (bps) PD","6M CDS Risk Benchmark Probability of Default Half-Year on Half-Year change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_6M_SS_PERCENT_CDS","6M CDS Risk Benchmark hy/hy (%) PD","6M CDS Risk Benchmark hy/hy (%) PD","6M CDS Risk Benchmark Probability of Default Half-Year on Healf-Year percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_6M_WW_BPS_CDS","6M CDS Risk Benchmark w/w (bps) PD","6M CDS Risk Benchmark w/w (bps) PD","6M CDS Risk Benchmark Probability of Default Week on Week change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_6M_WW_PERCENT_CDS","6M CDS Risk Benchmark w/w (%) PD","6M CDS Risk Benchmark w/w (%) PD","6M CDS Risk Benchmark Probability of Default Week on Week percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_6M_YY_BPS_CDS","6M CDS Risk Benchmark y/y (bps) PD","6M CDS Risk Benchmark y/y (bps) PD","6M CDS Risk Benchmark Probability of Default Year on Year change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_PD_6M_YY_PERCENT_CDS","6M CDS Risk Benchmark y/y (%) PD","6M CDS Risk Benchmark y/y (%) PD","6M CDS Risk Benchmark Probability of Default Year on Year percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_DD_BPS_CDS","2Y CDS Risk Benchmark d/d (bps)","2Y CDS Risk Benchmark d/d (bps)","2Y CDS Risk Benchmar Spread Day on Day change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_DD_PERCENT_CDS","2Y CDS Risk Benchmark d/d (%)","2Y CDS Risk Benchmark d/d (%)","2Y CDS Risk Benchmark Spread Day on Day percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_MM_BPS_CDS","2Y CDS Risk Benchmark m/m (bps)","2Y CDS Risk Benchmark m/m (bps)","2Y CDS Risk Benchmark Spread Month on Month change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_MM_PERCENT_CDS","2Y CDS Risk Benchmark m/m (%)","2Y CDS Risk Benchmark m/m (%)","2Y CDS Risk Benchmark Spread Month on Month percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_QQ_BPS_CDS","2Y CDS Risk Benchmark q/q (bps)","2Y CDS Risk Benchmark q/q (bps)","2Y CDS Risk Benchmark Spread Quater on Quater change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_QQ_PERCENT_CDS","2Y CDS Risk Benchmark q/q (%)","2Y CDS Risk Benchmark q/q (%)","2Y CDS Risk Benchmark Spread Quater on Quater percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_SS_BPS_CDS","2Y CDS Risk Benchmark hy/hy (bps)","2Y CDS Risk Benchmark hy/hy (bps)","2Y CDS Risk Benchmark Spread Half-Year on Half-Year change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_SS_PERCENT_CDS","2Y CDS Risk Benchmark hy/hy (%)","2Y CDS Risk Benchmark hy/hy (%)","2Y CDS Risk Benchmark Spread Half-Year on Healf-Year percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_WW_BPS_CDS","2Y CDS Risk Benchmark w/w (bps)","2Y CDS Risk Benchmark w/w (bps)","2Y CDS Risk Benchmark Spread Week on Week change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_WW_PERCENT_CDS","2Y CDS Risk Benchmark w/w (%)","2Y CDS Risk Benchmark w/w (%)","2Y CDS Risk Benchmark Spread Week on Week percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_YY_BPS_CDS","2Y CDS Risk Benchmark y/y (bps)","2Y CDS Risk Benchmark y/y (bps)","2Y CDS Risk Benchmark Spread Year on Year change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_2Y_YY_PERCENT_CDS","2Y CDS Risk Benchmark y/y (%)","2Y CDS Risk Benchmark y/y (%)","2Y CDS Risk Benchmark Spread Year on Year percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_DD_BPS_CDS","4Y CDS Risk Benchmark d/d (bps)","4Y CDS Risk Benchmark d/d (bps)","4Y CDS Risk Benchmar Spread Day on Day change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_DD_PERCENT_CDS","4Y CDS Risk Benchmark d/d (%)","4Y CDS Risk Benchmark d/d (%)","4Y CDS Risk Benchmark Spread Day on Day percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_MM_BPS_CDS","4Y CDS Risk Benchmark m/m (bps)","4Y CDS Risk Benchmark m/m (bps)","4Y CDS Risk Benchmark Spread Month on Month change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_MM_PERCENT_CDS","4Y CDS Risk Benchmark m/m (%)","4Y CDS Risk Benchmark m/m (%)","4Y CDS Risk Benchmark Spread Month on Month percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_QQ_BPS_CDS","4Y CDS Risk Benchmark q/q (bps)","4Y CDS Risk Benchmark q/q (bps)","4Y CDS Risk Benchmark Spread Quater on Quater change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_QQ_PERCENT_CDS","4Y CDS Risk Benchmark q/q (%)","4Y CDS Risk Benchmark q/q (%)","4Y CDS Risk Benchmark Spread Quater on Quater percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_SS_BPS_CDS","4Y CDS Risk Benchmark hy/hy (bps)","4Y CDS Risk Benchmark hy/hy (bps)","4Y CDS Risk Benchmark Spread Half-Year on Half-Year change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_SS_PERCENT_CDS","4Y CDS Risk Benchmark hy/hy (%)","4Y CDS Risk Benchmark hy/hy (%)","4Y CDS Risk Benchmark Spread Half-Year on Healf-Year percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_WW_BPS_CDS","4Y CDS Risk Benchmark w/w (bps)","4Y CDS Risk Benchmark w/w (bps)","4Y CDS Risk Benchmark Spread Week on Week change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_WW_PERCENT_CDS","4Y CDS Risk Benchmark w/w (%)","4Y CDS Risk Benchmark w/w (%)","4Y CDS Risk Benchmark Spread Week on Week percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_YY_BPS_CDS","4Y CDS Risk Benchmark y/y (bps)","4Y CDS Risk Benchmark y/y (bps)","4Y CDS Risk Benchmark Spread Year on Year change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_4Y_YY_PERCENT_CDS","4Y CDS Risk Benchmark y/y (%)","4Y CDS Risk Benchmark y/y (%)","4Y CDS Risk Benchmark Spread Year on Year percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_DD_BPS_CDS","6M CDS Risk Benchmark d/d (bps)","6M CDS Risk Benchmark d/d (bps)","6M CDS Risk Benchmar Spread Day on Day change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_DD_PERCENT_CDS","6M CDS Risk Benchmark d/d (%)","6M CDS Risk Benchmark d/d (%)","6M CDS Risk Benchmark Spread Day on Day percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_MM_BPS_CDS","6M CDS Risk Benchmark m/m (bps)","6M CDS Risk Benchmark m/m (bps)","6M CDS Risk Benchmark Spread Month on Month change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_MM_PERCENT_CDS","6M CDS Risk Benchmark m/m (%)","6M CDS Risk Benchmark m/m (%)","6M CDS Risk Benchmark Spread Month on Month percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_QQ_BPS_CDS","6M CDS Risk Benchmark q/q (bps)","6M CDS Risk Benchmark q/q (bps)","6M CDS Risk Benchmark Spread Quater on Quater change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_QQ_PERCENT_CDS","6M CDS Risk Benchmark q/q (%)","6M CDS Risk Benchmark q/q (%)","6M CDS Risk Benchmark Spread Quater on Quater percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_SS_BPS_CDS","6M CDS Risk Benchmark hy/hy (bps)","6M CDS Risk Benchmark hy/hy (bps)","6M CDS Risk Benchmark Spread Half-Year on Half-Year change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_SS_PERCENT_CDS","6M CDS Risk Benchmark hy/hy (%)","6M CDS Risk Benchmark hy/hy (%)","6M CDS Risk Benchmark Spread Half-Year on Healf-Year percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_WW_BPS_CDS","6M CDS Risk Benchmark w/w (bps)","6M CDS Risk Benchmark w/w (bps)","6M CDS Risk Benchmark Spread Week on Week change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_WW_PERCENT_CDS","6M CDS Risk Benchmark w/w (%)","6M CDS Risk Benchmark w/w (%)","6M CDS Risk Benchmark Spread Week on Week percentage change","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_YY_BPS_CDS","6M CDS Risk Benchmark y/y (bps)","6M CDS Risk Benchmark y/y (bps)","6M CDS Risk Benchmark Spread Year on Year change in basis points","base", apiResponse},
                {"FC_RISK_BENCHMARK_SPREADS_6M_YY_PERCENT_CDS","6M CDS Risk Benchmark y/y (%)","6M CDS Risk Benchmark y/y (%)","6M CDS Risk Benchmark Spread Year on Year percentage change","base", apiResponse},
        };
    }

    @Test(dataProvider = "Fisc6373")
    public void Fisc6373_cdsEnhacementsInDataAggregator(String fitchFieldId, String displayName, String fitchFieldDesc, String fieldDefinition, String permissionsRequired, String apiResponse){
        try {
            Assert.assertTrue(apiResponse.contains("\"id\":\"" + fitchFieldId + "\""));
            Assert.assertTrue(apiResponse.contains("\"displayName\":\"" + displayName + "\""));
            Assert.assertTrue(apiResponse.contains("\"fitchFieldDesc\":\"" + fitchFieldDesc + "\""));
            Assert.assertTrue(apiResponse.contains("\"fieldDefinition\":\"" + fieldDefinition + "\""));
            logger.info("FISC 6373 API DATA AGGREGATOR PASSED! Tested FITCHFIELDID: " + fitchFieldId + " DISPLAYNAME: " + displayName + " FITCHFIELDDESC " + fitchFieldDesc + " FIELDDEFINITION " + fieldDefinition + " PERMISSION " + permissionsRequired);
        } catch (AssertionError err){
            logger.error("FISC 6373 API DATA AGGREGATOR FAILED! Tested FITCHFIELDID: "  + fitchFieldId + " ERROR: " + err);
            Assert.fail();
        }
    }

    @Test
    public void Fisc7288_HistoryFilesFor6MCDSDataToUpstreamAndMongo(){

    }

    @Test
    public void Fisc7891_IssueDataInPostgresMasterDatabase(){

    }
}
