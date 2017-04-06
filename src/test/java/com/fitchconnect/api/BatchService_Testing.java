
package com.fitchconnect.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class BatchService_Testing extends Configuration {

	
	@Test
	public void comparingData_535() throws IOException {
		
		System.out.println("Batch Service Test Casses");

		boolean failure = false;

		try {
			MongoCredential credential = MongoCredential.createCredential("reporter", "admin",
					"the_call".toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataBaseServer1, 27017),
					Arrays.asList(credential));

			DB db = mongoClient.getDB("core-1");

			DBCollection collection = db.getCollection("market_sector");

			String id = "01010103";

			DBObject match = new BasicDBObject("$match", new BasicDBObject("MRKT_SCTR_ID", id));

			DBObject project = new BasicDBObject("$project",
					new BasicDBObject("MRKT_SCTR_ID", 1).append("MRKT_SCTR_DESC", 1));

			AggregationOutput output = collection.aggregate(match, project);

			String MRKT_SCTR_DESC = null;

			for (DBObject result : output.results()) {
				MRKT_SCTR_DESC = (String) result.get("MRKT_SCTR_DESC");
				// System.out.println("**" + result);
			}
			
		
		
		
		
			   MongoCredential credential1 = MongoCredential.createCredential("reporter", "admin",
						"the_call".toCharArray());
					MongoClient mongoClient1 = new MongoClient(new ServerAddress(dataBaseServer2, 27017),
							Arrays.asList(credential1));	
					

			db = mongoClient1.getDB("fcapidb");
			collection = db.getCollection("marketSector");

			match = new BasicDBObject("$match", new BasicDBObject("_id", id));

			project = new BasicDBObject("$project", new BasicDBObject("_id", 1).append("name", 1));

			output = collection.aggregate(match, project);

			String name = null;

			for (DBObject result : output.results()) {
				name = (String) result.get("name");
				// System.out.println(result);
			}

			// System.out.println(MRKT_SCTR_DESC);
			// System.out.println(name);

			if (!MRKT_SCTR_DESC.equals(name)) {
				failure = true;
			}
			Assert.assertFalse(failure);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	@Test
	public void comparingFitchentity_fromXref_with_Esp9() throws IOException {

		System.out.println("RUNNING THE LONGEST TEST CASE");
		boolean failure = false;

		ArrayList<String> CompanyId = new ArrayList();

		try {
			MongoCredential credential = MongoCredential.createCredential("reporter", "admin",
					"the_call".toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataBaseServer2, 27017),
					Arrays.asList(credential));
		
			DB db = mongoClient.getDB("xrefdb");

			DBCollection collection = db.getCollection("fitchEntity");

			DBObject project = new BasicDBObject("$project", new BasicDBObject("fitchCompanyId", 1));

			AggregationOutput output = collection.aggregate(project);

			for (DBObject result : output.results()) {
				CompanyId.add((String) result.get("fitchCompanyId"));
			}
			System.out.println("Number of company ID " + CompanyId.size());
			// System.out.println("Company Id :" + CompanyId);

		} catch (Exception e) {
			System.err.println("try catch error " + e.getClass().getName() + ": " + e.getMessage());
		}

		ArrayList<Long> AgentID = new ArrayList();

		try {
			MongoCredential credential = MongoCredential.createCredential("reporter", "admin",
					"the_call".toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataBaseServer1, 27017),
					Arrays.asList(credential));		

			 
			DB db = mongoClient.getDB(databaseFitchEnty);

			DBCollection collection = db.getCollection("fitch_entity");

			DBObject project = new BasicDBObject("$project", new BasicDBObject("agentID", 1));

			AggregationOutput output = collection.aggregate(project);

			for (DBObject result : output.results()) {
				AgentID.add((Long) result.get("agentID"));
			}
			System.out.println("Agent Id :" + AgentID);
			System.out.println("Number of Agent Id " + AgentID.size());

			for (String ID : CompanyId) {

				if (ID != null && !ID.trim().isEmpty()) {
					if (AgentID.contains(Long.valueOf(ID))) {
						// System.out.println("Found Company Id " + ID);
					} else {
						System.err.println("Not Found CompanyId :" + ID);
						failure = true;
					}
				}
			}

		} catch (Exception e) {
			System.err.println("try catch error " + e.getClass().getName() + " : " + e.getMessage());
		}

	}

	@Test
	public void compareCore_1marketSector_To_fcapidb_marketSector_Data() throws IOException {
	boolean failure = false;

		ArrayList<String> MRKT_SCTR_ID = new ArrayList<String>();
		getList(MRKT_SCTR_ID);
		
	System.out.println(MRKT_SCTR_ID);

	try {
			
		for (int i = 0; i < MRKT_SCTR_ID.size(); i++) {
				MongoCredential credential = MongoCredential.createCredential("reporter", "admin",
						"the_call".toCharArray());
				MongoClient mongoClient = new MongoClient(new ServerAddress(dataBaseServer1, 27017),
						Arrays.asList(credential));

				DB db = mongoClient.getDB("core-1");

				DBCollection collection = db.getCollection("market_sector");
				
			

				DBObject match = new BasicDBObject("$match", new BasicDBObject("MRKT_SCTR_ID", MRKT_SCTR_ID.get(i)));

				DBObject project = new BasicDBObject("$project",
						new BasicDBObject("MRKT_SCTR_ID", 1).append("MRKT_SCTR_DESC", 1));

				AggregationOutput output = collection.aggregate(match, project);

				String MRKT_SCTR_DESC = null;

				for (DBObject result : output.results()) {
					MRKT_SCTR_DESC = (String) result.get("MRKT_SCTR_DESC");
					// System.out.println("First Database:" + result);
				}
				
				//System.out.println(MRKT_SCTR_DESC);
				
				
				
		
		        MongoCredential credential1 = MongoCredential.createCredential("reporter", "admin",
					"the_call".toCharArray());
				MongoClient mongoClient1 = new MongoClient(new ServerAddress(dataBaseServer2, 27017),
						Arrays.asList(credential1));	
				

				db = mongoClient1.getDB("fcapidb");
				collection = db.getCollection("marketSector");

				match = new BasicDBObject("$match", new BasicDBObject("_id", MRKT_SCTR_ID.get(i)));

				project = new BasicDBObject("$project", new BasicDBObject("_id", 1).append("name", 1));

				output = collection.aggregate(match, project);

				String name = null;

				for (DBObject result : output.results()) {
					name = (String) result.get("name");
					// System.out.println("Second Database" + result);
				}

				// System.out.println(MRKT_SCTR_DESC);
				// System.out.println(name);

				if (!MRKT_SCTR_DESC.equals(name)) {
					failure = true;
				}

			
			}
			
			} catch (Exception e) {
				System.err.println("try catch error " + e.getClass().getName() + ": " + e.getMessage());	
				}	
			}
			   	


	
	
	public void getList(ArrayList<String> MRKT_SCTR_ID) throws IOException {

		try {
			MongoCredential credential = MongoCredential.createCredential("reporter", "admin",
					"the_call".toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataBaseServer1, 27017),
					Arrays.asList(credential));

		
			DB db = mongoClient.getDB("core-1");

			DBCollection collection = db.getCollection("market_sector");

			DBObject project = new BasicDBObject("$project", new BasicDBObject("MRKT_SCTR_ID", 1));

			AggregationOutput output = collection.aggregate(project);

			for (DBObject result : output.results()) {
				MRKT_SCTR_ID.add((String) result.get("MRKT_SCTR_ID"));
			}

			System.out.println("all markersector Id :" + MRKT_SCTR_ID);

		} catch (Exception e) {
			System.err.println("try catch error " + e.getClass().getName() + ": " + e.getMessage());
		}

	}


	
	


	// ++++++++++++++++++++++==============================================================================================================
	
	@Test
	public void Comparing_identifierVOList() {
		boolean failure = false;
		ArrayList<Long> ModyisId = new ArrayList();

		try {
			MongoCredential credential = MongoCredential.createCredential("reporter", "admin",
					"the_call".toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataBaseServer1, 27017),
					Arrays.asList(credential));
			// MongoClient mongoClient = new MongoClient(dataBaseServer, 27017);

			// DB db = mongoClient.getDB("admin");
			// boolean auth = db.authenticate("reporter",
			// "the_call".toCharArray());

			DB db = mongoClient.getDB(databaseFitchEnty);

			DBCollection collection = db.getCollection("fitch_entity");

			DBObject project = new BasicDBObject("$project", new BasicDBObject("agentID", 1));

			DBObject match = new BasicDBObject("$match", new BasicDBObject("identifierVOList.name", "LEI")
					.append("identifierVOList.name", "CUSIP").append("identifierVOList.name", "Moodys ID")
			// .append("identifierVOList.name", "S&P ID")
			// .append("identifierVOList.name", "CIK Code")
			// .append("identifierVOList.name", "Dow Jones Ticker")

			// .append("identifierVOList.name", "FDIC Cert Number")

			// .append("identifierVOList.name", "agentId")
			// .append("identifierVOList.name", "groupId")
			);

			AggregationOutput output = collection.aggregate(match, project);

			for (DBObject result : output.results()) {
				ModyisId.add((Long) result.get("agentID"));

			}
			System.out.println("Number of Mody's Entity " + ModyisId.size());
			// System.out.println(ModyisId);

		} catch (Exception e) {
			System.err.println("try catch error " + e.getClass().getName() + ": " + e.getMessage());
		}

		ArrayList<String> CompanyIds = new ArrayList();

		try {

			MongoCredential credential = MongoCredential.createCredential("reporter", "admin",
					"the_call".toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataBaseServer2, 27017),
					Arrays.asList(credential));
			// MongoClient mongoClient = new MongoClient(dataBaseServer, 27017);

			// DB db = mongoClient.getDB("admin");
			// boolean auth = db.authenticate("reporter",
			// "the_call".toCharArray());

			DB db = mongoClient.getDB("xrefdb");

			DBCollection collection = db.getCollection("fitchEntity");

			DBObject project = new BasicDBObject("$project", new BasicDBObject("fitchCompanyId", 1));

			DBObject match = new BasicDBObject("$match",
					new BasicDBObject("entities.idType", "LEI").append("entities.idType", "CUSIP6")

							.append("entities.idType", "moodys"));
			/*
			 * .append("entities.idType","sandp")
			 * .append("entities.idType","CIK")
			 * .append("entities.idType","companyTicker")
			 * .append("entities.idType","LEI")
			 * .append("entities.idType","FDIC")
			 * 
			 * .append("entities.idType","FitchID")
			 * .append("entities.idType","fitchGroupID")
			 */

			AggregationOutput output = collection.aggregate(match, project);

			for (DBObject result : output.results()) {
				CompanyIds.add((String) result.get("fitchCompanyId"));
			}

			System.out.println("Number of Company id " + CompanyIds.size());
			// System.out.println(CompanyIds);

			for (String compare : CompanyIds) {

				if (compare != null && !compare.trim().isEmpty()) {
					if (ModyisId.contains(Long.valueOf(compare))) {
						//System.out.println("Found Company Id " + compare);
					} else {
						System.err.println("Not Found CompanyId :" + compare);
						failure = true;
					}
				}
			}

		} catch (Exception e) {
			System.err.println("try catch error " + e.getClass().getName() + ": " + e.getMessage());
		}

	}

}
