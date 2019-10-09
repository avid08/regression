package com.configuration;

import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;
import groovy.json.internal.Charsets;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.jayway.restassured.RestAssured.given;

public class LoggerInitialization {

    private static void initLogger(String testSuiteName) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
            Date date = new Date();
            String filePath = System.getProperty("user.home") + "\\IdeaProjects\\apiregresssion\\regression\\src\\test\\java\\com\\logs\\" + testSuiteName + "__" + dateFormat.format(date) + "_Test_Execution_Log.log";
            // String filePath = System.getProperty("user.home") + "\\IdeaProjects\\APIRegression\\src\\test\\java\\com\\logs\\log.log";
            org.apache.log4j.PatternLayout layout = new PatternLayout("%-5p %d %m%n");
            org.apache.log4j.RollingFileAppender appender = new RollingFileAppender(layout, filePath);
            appender.setName("Test_Execution_Log");
            appender.setMaxFileSize("25MB");
            appender.activateOptions();
            org.apache.log4j.Logger.getRootLogger().addAppender(appender);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Logger setupLogger(String sprintName) {
        initLogger(sprintName + "_");
        Logger logger = Logger.getLogger("Test_Execution_Log_Sprint_10");
        return logger;
    }
}
