package com.DmetadataRegression;

import java.io.IOException;
import java.net.URISyntaxException;

import org.testng.Assert;
import org.testng.annotations.Test;


public class Metadata {

    @Test(enabled = true)
    public void Financials() {
        MetadataRun financialRun = new MetadataRun();
        try {
            Assert.assertEquals(financialRun.testMetadata("Financials"),0, "Number of failed fields:");
        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test()
    public void Ratings() {
        MetadataRun ratingsRun = new MetadataRun("new ESP-9");
        try {
            Assert.assertEquals(ratingsRun.testMetadata("Ratings"),0, "Number of failed fields:");
        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
