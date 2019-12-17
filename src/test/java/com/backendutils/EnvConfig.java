package com.backendutils;

public class EnvConfig {
    public static class Postgres {
        public static class QA {
            public static final String USERNAME = "qa";
            public static final String PASSWORD = "XrLqTU4LfX6x9ExV";
            public static final String HOSTNAME = "jdbc:postgresql://pos-pro-que-rds.c3qwnbqmztfh.us-east-1.rds.amazonaws.com/prov_qa";
            public static final String PORT = "5432";
            public static final String DBNAME = "prov_qa";
        }

        public static class STG {
            public static final String USERNAME = "qa";
            public static final String PASSWORD = "B3gJWC9ccG4qd2KB";
            public static final String HOSTNAME = "jdbc:postgresql://pos-smr-gue-rds.c3qwnbqmztfh.us-east-1.rds.amazonaws.com/smr_stg";
            public static final String PORT = "5432";
            public static final String DBNAME = "smr_stg";
        }

        public static class PROD {
            public static final String USERNAME = "qa";
            public static final String PASSWORD = "R2VMn8ZCe7x51Me8U";
            public static final String HOSTNAME = "jdbc:postgresql://pos-smr-pue-rds.cskmoyo8brj0.us-east-1.rds.amazonaws.com/master";
            public static final String PORT = "5432";
            public static final String DBNAME = "master";
        }

        public static class CR_DEV {
            public static final String USERNAME = "prov_admin";
            public static final String PASSWORD = "q9=w7UMR";
            public static final String HOSTNAME = "jdbc:postgresql://pos-pro-due-rds.cvhy6th3vpsn.us-east-1.rds.amazonaws.com/prov_dev";
            public static final String PORT = "5432";
            public static final String DBNAME = "prov_dev";
        }

        public static class CR_QA {
            public static final String USERNAME = "product";
            public static final String PASSWORD = "5HNJbSJUYkDD";
            public static final String HOSTNAME = "jdbc:postgresql://pos-pro-que-rds.c3qwnbqmztfh.us-east-1.rds.amazonaws.com/prov_qa";
            public static final String PORT = "5432";
            public static final String DBNAME = "prov_qa";
        }

        public static class CR_STG {
            public static final String USERNAME = "product";
            public static final String PASSWORD = "j94HdB09TrYxxNlWg";
            public static final String HOSTNAME = "jdbc:postgresql://pos-pro-que-rds.c3qwnbqmztfh.us-east-1.rds.amazonaws.com/server";
            public static final String PORT = "5432";
            public static final String DBNAME = "server";
        }

        public static class CR_PROD {
            public static final String USERNAME = "product";
            public static final String PASSWORD = "j94HdB09TrYxxNlWg";
            public static final String HOSTNAME = "jdbc:postgresql://pos-smr-pue-rds.cskmoyo8brj0.us-east-1.rds.amazonaws.com/server";
            public static final String PORT = "5432";
            public static final String DBNAME = "server";
        }
    }

    public static class Mongo {
            public static final String FitchMongo = "mongodb://reporter:the_call@mgo-que1a-cr001:27017/?authSource=admin&authMechanism=None";
            public static final String NEWCAL_PROD = "mongodb://reporter:the_call@mgo-pue1a-sn002.fitchratings.com:27017/?authSource=admin&authMechanism=SCRAM-SHA-1";
            public static final String CAL_PROD = "mongodb://reporter:the_call@mgo-pue1c-cr002.fitchratings.com:27017/?authSource=admin&authMechanism=SCRAM-SHA-1";
            public static final String FEEDS_PROD = "mongodb://reporter:the_call@mgo-pue1c-fd002.fitchratings.com:27017/?authSource=admin&authMechanism=SCRAM-SHA-1";
            public static final String META_PROD = "mongodb://reporter:the_call@mgo-pue1c-ur002.fitchratings.com:27017/?authSource=admin&authMechanism=SCRAM-SHA-1";
            public static final String CAL_PRODLIKE = "mongodb://reporter:the_call@mgo-uue1a-sn001:27017/?authSource=admin&authMechanism=SCRAM-SHA-1";
            public static final String CAL_STG = "mongodb://reporter:the_call@mgo-uue1a-cr001:27017/?authSource=admin&authMechanism=SCRAM-SHA-1";
            public static final String FEEDS_PRODLIKE = "mongodb://reporter:the_call@mgo-uue1c-fd001.fitchratings.com:27017/?authSource=admin&authMechanism=SCRAM-SHA-1e";
            public static final String META_STG = "mongodb://reporter:the_call@mgo-uue1a-ur001.fitchratings.com:27017/?authSource=admin&authMechanism=SCRAM-SHA-1";
            public static final String CAL_QA2 = "mongodb://reporter:the_call@mgo-que1a-sn001.fitchratings.com:27017/?authSource=admin&authMechanism=SCRAM-SHA-1";
            public static final String CAL_QA = "mongodb://reporter:the_call@mgo-que1a-cr001.fitchratings.com:27017/?authSource=admin&authMechanism=SCRAM-SHA-1";
            public static final String FEEDS_QA = "mongodb://reporter:the_call@mgo-que1a-sn001:27017/?authSource=admin&authMechanism=SCRAM-SHA-1";
            public static final String META_QA = "mongodb://reporter:the_call@mgo-que1a-ur001.fitchratings.com:27017/?authSource=admin&authMechanism=SCRAM-SHA-1";
            public static final String EDIT_CAL_DEV = "mongodb://mongo-esp-usr:the_call@mgo-due1c-cr001:27017/?authSource=esp-dev-9&authMechanism=SCRAM-SHA-1";
            public static final String CAL_DEV = "mongodb://reporter:the_call@mgo-due1c-cr001:27017/?authSource=admin&authMechanism=SCRAM-SHA-1";
            public static final String FEEDS_DEV = "mongodb://reporter:the_call@mgo-due1c-fd001:27017/?authSource=admin&authMechanism=SCRAM-SHA-1";
            public static final String META_DEV = "mongodb://reporter:the_call@mgo-due1c-ur001:27017/?authSource=admin&authMechanism=SCRAM-SHA-1";
            public static final String FEEDS_PROD_PARALLEL = "mongodb://reporter:the_call@mgo-pue1a-sn002:27017/?authSource=admin&authMechanism=SCRAM-SHA-256";
            public static final String CDS = "mongodb://reporter:the_call@mgo-que1a-cr002:27017/?authSource=admin&authMechanism=SCRAM-SHA-1";
    }

    public static class MySQL {
        public static class QA {
            public static final String USERNAME = "qa";
            public static final String PASSWORD = "h73hfdn2$";
            public static final String HOSTNAME = "jdbc:mysql://mys-cov-que-rds.c3qwnbqmztfh.us-east-1.rds.amazonaws.com/prodstage?zeroDateTimeBehavior=convertToNull";
            public static final String PORT = "3306";
            public static final String DBNAME = "";
        }

        public static class PROD {
            public static final String USERNAME = "fsda-prod";
            public static final String PASSWORD = "dfdb96edgh32R";
            public static final String HOSTNAME = "jdbc:mysql://mys-cov-pue-rds.cskmoyo8brj0.us-east-1.rds.amazonaws.com/prodstage?zeroDateTimeBehavior=convertToNull";
            public static final String PORT = "3306";
            public static final String DBNAME = "";
        }

        public static class CR_QA {
            public static final String USERNAME = "fs_readonly";
            public static final String PASSWORD = "M9d3Gp^g!LPXp";
            public static final String HOSTNAME = "jdbc:mysql://covrevtest.ccwpcizbgfg6.us-east-1.rds.amazonaws.com/prodstage?zeroDateTimeBehavior=convertToNull";
            public static final String PORT = "3306";
            public static final String DBNAME = "";
        }
    }
}
