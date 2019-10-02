package com.backendutils;

public class Env {
    public enum Postgres {
        DEV, DEV_INT, STG, PROD, QA
    }
    public enum Mongo {
        FitchMongo, NEWCAL_PROD, CAL_PROD, FEEDS_PROD, META_PROD, CAL_PRODLIKE, CAL_STG, FEEDS_PRODLIKE, META_STG, CAL_QA2, CAL_QA, FEEDS_QA, META_QA, EDIT_CAL_DEV, CAL_DEV, FEEDS_DEV, META_DEV, FEEDS_PROD_PARALLEL, CDS
    }
}
