package com.DmetadataRegression.Helpers.Runnables;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import com.DmetadataRegression.MetadataRun;
import com.DmetadataRegression.Finanicals.FinancialMeta;
import com.DmetadataRegression.FitchRatings.RatingsMeta;
import com.DmetadataRegression.Helpers.MetaType;
import com.DmetadataRegression.Helpers.MetaType.FieldMap;
import com.DmetadataRegression.Helpers.MetadataConfig;
import com.DmetadataRegression.Helpers.TestRecord.TestRecord;

import javafx.util.Pair;

public class DataProducer implements Runnable {
    private final String stringMetaType;
    private final MetadataRun metadataRun;
    private MetaType metaType;


    public DataProducer(String type, MetadataRun metadataRun) throws IOException, URISyntaxException {
        stringMetaType = type;
        this.metadataRun = metadataRun;
        List<FieldMap> fieldMapList = MetadataConfig.getListOfLookups(stringMetaType);
        switch (type) {
            case "Financials":
                metaType = new FinancialMeta(this.metadataRun.databaseServer);
                break;
            case "Ratings":
                metaType = new RatingsMeta(this.metadataRun.databaseServer);
                break;
        }
        this.metadataRun.producerQueue.addAll(fieldMapList);
    }

    @Override
    public void run() {
        FieldMap field;
        boolean flag = true;
        while (flag) {
            while ((field = metadataRun.producerQueue.poll()) != null) {
                try {
                    Pair<FieldMap, TestRecord> pair = new Pair<>(field, metaType.getDataFromMongo(field, stringMetaType));
                    System.out.println("Found in " + (pair.getKey().state == FieldMap.State.Recorded ? "cache: " : "DB: ") + pair.getKey().fieldID);
                    synchronized (metadataRun.consumerBlocker) {
                        metadataRun.consumerQueue.add(pair);
                        if (metadataRun.consumeData.getState() == Thread.State.WAITING) {
                            metadataRun.consumerBlocker.notifyAll();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (metadataRun.consumeData.getState() != Thread.State.TERMINATED) {
                try {
                    synchronized (metadataRun.producerBlocker) {
                        //System.out.println("producer waiting");
                        metadataRun.producerBlocker.wait();
                    }
                    //System.out.println("producer stopped waiting");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                flag = false;
            }
        }
    }
}

