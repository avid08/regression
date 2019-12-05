package com.fulcrum.api;

import com.backendutils.ArrayUtils;
import com.backendutils.EnvConfig;
import com.backendutils.MySqlUtils;
import com.configuration.api.Configuration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.backendutils.Env.MySQL.CR_QA;
import static com.backendutils.Env.MySQL.QA;

public class CovenantReview extends Configuration {

    MySqlUtils mySqlUtils = new MySqlUtils();
    FulcrumUtils fulcrumUtils = new FulcrumUtils();
    ArrayUtils arrayUtils = new ArrayUtils();

    @Test
    public void validateCovenantReviewDataBetweenDatabases() {
        try {
            HashMap<CovRevKey, ArrayList<Object>> covRevDataMap_QA_DB = fulcrumUtils.getCovenantReviewData(QA);
            HashMap<CovRevKey, ArrayList<Object>> covRevDataMap_QA_CR_DB = fulcrumUtils.getCovenantReviewData(CR_QA);

            Set<CovRevKey> covRevDataKeySet = covRevDataMap_QA_DB.keySet();
            List<AssertionError> errorsList = new ArrayList<AssertionError>();

            int i = 0;

            for (CovRevKey covRevKey : covRevDataKeySet) {
                ArrayList<Object> covRevQAList = covRevDataMap_QA_DB.get(covRevKey);
                ArrayList<Object> covRevCRQAList = covRevDataMap_QA_CR_DB.get(covRevKey);
                boolean isRowPassed = arrayUtils.areListsOfObjectsEqual(covRevQAList, covRevCRQAList);
                try {
                    Assert.assertTrue(isRowPassed);
                    System.out.println(i + "        COVREV PASSED       ID" + covRevKey.getId() + "         ENTITY ID" + covRevKey.getEntityId());
                    i++;
                    //  logger.info("COVREV PASSED " + covRevKey.getId() + "       " + covRevKey.getEntityId());
                }
                catch (AssertionError err) {
                    errorsList.add(err);
                    logger.error("COVREV FAILED " + covRevKey.getId() + "       " + covRevKey.getEntityId() + "      " + err);
                    System.out.println("COVREV FAILED " + covRevKey.getId() + "       " + covRevKey.getEntityId() + "      " + err);
                    continue;
                }
            }
            Assert.assertEquals(errorsList.size(), 0);
        } catch (SQLException ex){
            System.out.println("SQL EXCEPTION " + ex);
        }
    }
}
