package com.DmetadataRegression.Helpers.Runnables;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.DmetadataRegression.MetadataRun;
import com.DmetadataRegression.Finanicals.FinancialMeta;
import com.DmetadataRegression.FitchRatings.RatingsMeta;
import com.DmetadataRegression.Helpers.MetaType;
import com.DmetadataRegression.Helpers.MetaType.FieldMap;
import com.DmetadataRegression.Helpers.Mongo.MongoFileBuffer;
import com.DmetadataRegression.Helpers.TestRecord.TestRecord;

import javafx.util.Pair;


public class DataConsumer implements Runnable {
    private MetaType metaType;
    private final MetadataRun metadata;
    private final String metaName;
    final AtomicInteger fieldCounter = new AtomicInteger(1);
    private final int CORE_COUNT = Runtime.getRuntime().availableProcessors();
    final ThreadPoolExecutor pool = new ThreadPoolExecutor(CORE_COUNT * 2, CORE_COUNT * 2,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public DataConsumer(String type, MetadataRun metadataRun) {
        this.metadata = metadataRun;
        this.metaName = type;
        switch (type) {
            case "Financials":
                metaType = new FinancialMeta();
                break;
            case "Ratings":
                metaType = new RatingsMeta();
                break;
        }
        System.out.println("Running on " + CORE_COUNT + " cores and " + CORE_COUNT * 2 + " threads.");
    }

    @Override
    public void run() {
        Pair<FieldMap, TestRecord> lastPair;
        boolean flag = true;
        while (flag) {
            if ((lastPair = metadata.consumerQueue.poll()) == null) {
                if (metadata.produceData.getState() == Thread.State.RUNNABLE ||
                        pool.getQueue().size() > 0 ||
                        pool.getActiveCount() > 0) {
                    try {
                        synchronized (metadata.consumerBlocker) {
//                            System.out.println("consumer waiting");
                            metadata.consumerBlocker.wait();
                        }
//                        System.out.println("consumer stopped waiting");
                        lastPair = metadata.consumerQueue.poll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (lastPair == null) {
                        flag = false;
                        continue;
                    }
                } else {
                    flag = false;
                    continue;
                }
            }
            pool.submit(new APIRunnable(lastPair, metaType, metadata, metaName, this));
        }
        try {
            MongoFileBuffer mongoFileBuffer = MongoFileBuffer.getInstance(metaName);
            mongoFileBuffer.updateCachedRecords();
            pool.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
