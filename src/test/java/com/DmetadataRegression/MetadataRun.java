package com.DmetadataRegression;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.Logger;

import com.DmetadataRegression.Helpers.MetaType;
import com.DmetadataRegression.Helpers.Runnables.DataConsumer;
import com.DmetadataRegression.Helpers.Runnables.DataProducer;
import com.DmetadataRegression.Helpers.TestRecord.TestRecord;
import com.fitchconnect.api.Configuration;
import com.jayway.restassured.response.Header;

import javafx.util.Pair;


public class MetadataRun extends Configuration {
    public final ConcurrentLinkedQueue<Pair<MetaType.FieldMap, TestRecord>> consumerQueue = new ConcurrentLinkedQueue<>();
    public final ConcurrentLinkedQueue<MetaType.FieldMap> producerQueue = new ConcurrentLinkedQueue<>();
    public final List<Header> headerList;
    public String databaseServer;
    public final String dataValueRequestUrl;
    public Thread produceData;
    public Thread consumeData;
    public final Logger logger;
    public AtomicInteger failed = new AtomicInteger(0);
    public final Object producerBlocker = new Object();
    public final Object consumerBlocker = new Object();

    MetadataRun() {
        try {
            executionSetup();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger = Configuration.logger;
        databaseServer = dataBaseServer1;
        dataValueRequestUrl = dataPostUrl;
        headerList = Arrays.asList(
                new Header("Authorization", "Bearer " + refresh_token),
                new Header("X-App-Client-Id", XappClintIDvalue),
                new Header("Accept", acceptValue),
                new Header("Content-Type", contentValue));
    }
    MetadataRun(String database){
        this();
        if (database.equals("new ESP-9")) databaseServer = dataBaseServerNewESP9;
    }

    int testMetadata(String metaTypeString) throws IOException, URISyntaxException, InterruptedException {
        produceData = new Thread(new DataProducer(metaTypeString, this), "Producer");
        consumeData = new Thread(new DataConsumer(metaTypeString, this), "Consumer");
        produceData.setUncaughtExceptionHandler((thread, exception) -> {
            StringWriter errors = new StringWriter();
            exception.printStackTrace(new PrintWriter(errors));
            logger.fatal("Exception in thread " + thread + ":" + errors.toString());
        });
        consumeData.setUncaughtExceptionHandler((thread, exception) -> {
            StringWriter errors = new StringWriter();
            exception.printStackTrace(new PrintWriter(errors));
            logger.fatal("Exception in thread " + thread + ":" + errors.toString());
        });
        produceData.start();
        consumeData.start();
        consumeData.join();
        return failed.get();
    }
}
