package com.backendutils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoUtils {

    public MongoClient connectToMongoDatabase(Env.Mongo env){
        switch(env) {
            case FitchMongo:
            MongoClientURI fitchMongoUri = new MongoClientURI(EnvConfig.Mongo.FitchMongo);
            MongoClient fitchMongoClient = new MongoClient(fitchMongoUri);
            System.out.println("Connected to FitchMongo database");
            return fitchMongoClient;

            case NEWCAL_PROD:
            MongoClientURI newCalProdUri = new MongoClientURI(EnvConfig.Mongo.NEWCAL_PROD);
            MongoClient newCalProdClient = new MongoClient(newCalProdUri);
            System.out.println("Connected to NEWCAL_PROD database");
            return newCalProdClient;

            case CAL_PROD:
            MongoClientURI calProdUri = new MongoClientURI(EnvConfig.Mongo.CAL_PROD);
            MongoClient calProdClient = new MongoClient(calProdUri);
            System.out.println("Connected to CAL_PROD database");
            return calProdClient;

            case FEEDS_PROD:
            MongoClientURI feedsProdUri = new MongoClientURI(EnvConfig.Mongo.FEEDS_PROD);
            MongoClient feedsProdClient = new MongoClient(feedsProdUri);
            System.out.println("Connected to FEEDS_PROD database");
            return feedsProdClient;

            case META_PROD:
            MongoClientURI metaProdUri = new MongoClientURI(EnvConfig.Mongo.META_PROD);
            MongoClient metaProdClient = new MongoClient(metaProdUri);
            System.out.println("Connected to META_PROD database");
            return metaProdClient;

            case CAL_PRODLIKE:
            MongoClientURI calProdlikeUri = new MongoClientURI(EnvConfig.Mongo.CAL_PRODLIKE);
            MongoClient calProdlikeClient = new MongoClient(calProdlikeUri);
            System.out.println("Connected to CAL_PRODLIKE database");
            return calProdlikeClient;

            case CAL_STG:
            MongoClientURI calStgUri = new MongoClientURI(EnvConfig.Mongo.CAL_STG);
            MongoClient calStgClient = new MongoClient(calStgUri);
            System.out.println("Connected to CAL_STG database");
            return calStgClient;

            case FEEDS_PRODLIKE:
            MongoClientURI feedsProdlikeUri = new MongoClientURI(EnvConfig.Mongo.FEEDS_PRODLIKE);
            MongoClient feedsProdlikeClient = new MongoClient(feedsProdlikeUri);
            System.out.println("Connected to FEEDS_PRODLIKE database");
            return feedsProdlikeClient;

            case META_STG:
            MongoClientURI metaStgUri = new MongoClientURI(EnvConfig.Mongo.META_STG);
            MongoClient metaStgClient = new MongoClient(metaStgUri);
            System.out.println("Connected to META_STG database");
            return metaStgClient;

            case CAL_QA2:
            MongoClientURI calQa2Uri = new MongoClientURI(EnvConfig.Mongo.CAL_QA2);
            MongoClient calQa2Client = new MongoClient(calQa2Uri);
            System.out.println("Connected to CAL_QA2 database");
            return calQa2Client;

            case CAL_QA:
            MongoClientURI calQaUri = new MongoClientURI(EnvConfig.Mongo.CAL_QA);
            MongoClient calQaClient = new MongoClient(calQaUri);
            System.out.println("Connected to CAL_QA database");
            return calQaClient;

            case FEEDS_QA:
            MongoClientURI feedsQaUri = new MongoClientURI(EnvConfig.Mongo.FEEDS_QA);
            MongoClient feedsQaClient = new MongoClient(feedsQaUri);
            System.out.println("Connected to FEEDS_QA database");
            return feedsQaClient;

            case META_QA:
            MongoClientURI metaQaUri = new MongoClientURI(EnvConfig.Mongo.META_QA);
            MongoClient metaQaClient = new MongoClient(metaQaUri);
            System.out.println("Connected to META_QA database");
            return metaQaClient;

            case EDIT_CAL_DEV:
            MongoClientURI editCalDevUri = new MongoClientURI(EnvConfig.Mongo.EDIT_CAL_DEV);
            MongoClient editCalDevClient = new MongoClient(editCalDevUri);
            System.out.println("Connected to EDIT_CAL_DEV database");
            return editCalDevClient;

            case CAL_DEV:
            MongoClientURI calDevUri = new MongoClientURI(EnvConfig.Mongo.CAL_DEV);
            MongoClient calDevClient = new MongoClient(calDevUri);
            System.out.println("Connected to CAL_DEV database");
            return calDevClient;

            case FEEDS_DEV:
            MongoClientURI feedsDevUri = new MongoClientURI(EnvConfig.Mongo.FEEDS_DEV);
            MongoClient feedsDevClient = new MongoClient(feedsDevUri);
            System.out.println("Connected to FEEDS_DEV database");
            return feedsDevClient;

            case META_DEV:
            MongoClientURI metaDevUri = new MongoClientURI(EnvConfig.Mongo.META_DEV);
            MongoClient metaDevClient = new MongoClient(metaDevUri);
            System.out.println("Connected to META_DEV database");
            return metaDevClient;

            case FEEDS_PROD_PARALLEL:
            MongoClientURI feedsProdParallelUri = new MongoClientURI(EnvConfig.Mongo.FEEDS_PROD_PARALLEL);
            MongoClient feedsProdParallelClient = new MongoClient(feedsProdParallelUri);
            System.out.println("Connected to FEEDS_PROD_PARALLEL database");
            return feedsProdParallelClient;

            case CDS:
                MongoClientURI cdsUri = new MongoClientURI(EnvConfig.Mongo.CDS);
                MongoClient cdsClient = new MongoClient(cdsUri);
                System.out.println("Connected to CDS database");
                return cdsClient;

            default:
                MongoClientURI defaultUri = new MongoClientURI(EnvConfig.Mongo.META_QA);
                MongoClient defaultMongoClient = new MongoClient(defaultUri);
                System.out.println("Connected to META_QA database as default");
                return defaultMongoClient;
        }
    }
}
