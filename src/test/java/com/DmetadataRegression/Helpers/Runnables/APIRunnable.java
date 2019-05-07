package com.DmetadataRegression.Helpers.Runnables;

import com.DmetadataRegression.MetadataRun;
import com.DmetadataRegression.Helpers.MetaType;
import com.DmetadataRegression.Helpers.TestRecord.TestRecord;
import com.jayway.restassured.response.Response;

import javafx.util.Pair;

public class APIRunnable implements Runnable {
    private final Pair<MetaType.FieldMap, TestRecord> recordPair;
    private final MetaType metaType;
    private final MetadataRun metadataRun;
    private final String metaName;
    private final DataConsumer dataConsumer;

    APIRunnable(Pair<MetaType.FieldMap, TestRecord> lastPair, MetaType metaType, MetadataRun metadataRun, String metaName, DataConsumer dataConsumer) {
        recordPair = lastPair;
        this.metaType = metaType;
        this.metadataRun = metadataRun;
        this.metaName = metaName;
        this.dataConsumer = dataConsumer;

    }

    public void run() {
        if (recordPair.getValue().hasData()) {
            Pair<String, Response> requestResponsePair = metaType.getDataFromAPI(recordPair.getValue(), recordPair.getKey(), metadataRun.headerList, metadataRun.dataValueRequestUrl);
            String request = requestResponsePair.getKey();
            String valueString = MetaType.compareData(requestResponsePair.getValue(), recordPair.getValue());
            if (valueString == null) {
                System.out.println("\n[" + metaName + "]\n" +
                        recordPair.getKey().fieldID + " field passed\n" +
                        "Request: " + request + "\n" +
                        "Response: " + requestResponsePair.getValue().asString() + "\n" +
                        "Expected value: " + recordPair.getValue().getTestValue() + "\n");
            } else {
                switch (recordPair.getKey().state) {
                    case Recorded:
                        System.out.println("\n[" + metaName + "]\n" +
                                recordPair.getKey().fieldID + "field not found during cached run\n" +
                                "Request: " + request + "\n" +
                                "Response value: " + requestResponsePair.getValue().asString() + "\n" +
                                "Expected value: " + recordPair.getValue().getTestValue() + "\n");
                        recordPair.getKey().state = MetaType.FieldMap.State.RecordedNotFoundFirstTime;
                        metadataRun.producerQueue.add(recordPair.getKey());
                        dataConsumer.fieldCounter.getAndDecrement();
                        synchronized (metadataRun.producerBlocker) {
                            metadataRun.producerBlocker.notifyAll();
                        }
                        break;
                    case RecordedThenForceUpdated:
                        //System.out.println(result);
                        metadataRun.failed.getAndIncrement();
                        metadataRun.logger.error("\n[" + metaName + "]\n" +
                                recordPair.getKey().fieldID + " not found during forced search run\n" +
                                "Request: " + request + "\n" +
                                "Response value: " + valueString + "\n" +
                                "Expected value: " + recordPair.getValue().getTestValue() + "\n"
                        );
                        break;
                    default:
                        metadataRun.logger.error("\n[" + metaName + "]\n" + "\t\t" + recordPair.getKey().state + "\n" + recordPair.getKey().fieldID + "\n\n");
                        break;
                }
            }
            System.out.println("Fields done: " + dataConsumer.fieldCounter.get() + "\n" +
                    "Current field number left: " + metadataRun.producerQueue.size() + "\n" +
                    "Total Fields Found: " + (dataConsumer.fieldCounter.getAndIncrement() + dataConsumer.pool.getQueue().size() + dataConsumer.pool.getActiveCount() - 1) + "\n\n"
            );

        } else {
            metadataRun.logger.error("\n[" + metaName + "]\n" +
                    "NO DATA FOR FIELD ID num " + dataConsumer.fieldCounter.getAndIncrement() + ": " + recordPair.getKey().fieldID + "\n");
        }

        synchronized (metadataRun.consumerBlocker) {
            if (metadataRun.consumerQueue.size() == 0 &&
                    metadataRun.produceData.getState() == Thread.State.WAITING &&
                    dataConsumer.pool.getQueue().size() == 0 &&
                    dataConsumer.pool.getActiveCount() == 1)
                metadataRun.consumerBlocker.notify();
        }

    }
}
