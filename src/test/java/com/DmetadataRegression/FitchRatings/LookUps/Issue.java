package com.DmetadataRegression.FitchRatings.LookUps;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.DmetadataRegression.Helpers.MetaType;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.util.JSON;

import javafx.util.Pair;

public class Issue extends RatingsLookUpType {
    public Issue(String collection, MongoDatabase database) {
        super(collection, database);
    }

    @Override
    public Pair<Document, String> extractDocument(MetaType.FieldMap fieldMap) {
        MongoCollection<Document> issueCollection = database.getCollection(collection);
        List<Bson> aggregateList = convertMetaToMongoQuery(fieldMap);
        Document result = issueCollection.aggregate(aggregateList).first();
        String value;
        if (result != null) {
            if (result.get("result") instanceof Date) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                value = df.format(result.getDate("ratingDate"));
            } else value = result.getString("result");
            return new Pair<>(result, value);
        }
        return new Pair<>(null, null);
    }

    private List<Bson> convertMetaToMongoQuery(MetaType.FieldMap fieldMap) {
        List<Bson> aggregateList = new ArrayList<>();
        switch (fieldMap.fieldID) {
            case "FC_IDRR":
                makeFC_IDRRAggregation(fieldMap, aggregateList); //TODO - DOES NOTHING RN
            case "FC_IRR":
                makeFC_IRRAggregation(fieldMap, aggregateList);
                break;
            default:
                if (fieldMap.fieldID.contains("_SOLST")) {
                    makeSolicitationAggregation(fieldMap, aggregateList);
                } else {
                    makeGeneralAggregation(fieldMap, aggregateList);
                }
                break;
        }

        return aggregateList;
    }


    private void makeGeneralAggregation(MetaType.FieldMap fieldMap, List<Bson> aggregateList) {
        aggregateList.add(Aggregates.project(Projections.computed("Ratings", JSON.parse("{$concatArrays:[\"$currentInternationalRatings\",\"$currentNationalRatings\"]}"))));
        aggregateList.add(Aggregates.match(Filters.not(Filters.eq("Ratings", null))));
        aggregateList.add(Aggregates.unwind("$Ratings"));
        aggregateList.add(Aggregates.match(Filters.eq("Ratings.ratingTypeID", Integer.parseInt(fieldMap.lookUpSourceList.get(0).value))));
        List<Bson> projectionList = new ArrayList<>();
        projectionList.add(Projections.computed("ID", "$Ratings.ratableID"));
        projectionList.add(Projections.computed("ratingDate", "$Ratings.ratingEffectiveDate"));
        if (fieldMap.fieldID.contains("_DT")) {
            projectionList.add(Projections.computed("result", "$Ratings.ratingEffectiveDate"));
        } else if (fieldMap.fieldID.contains("_ALRT")) {
            aggregateList.add(Aggregates.match(Filters.and(
                    Filters.exists("Ratings.ratingAlertDescription"),
                    Filters.not(Filters.eq("Ratings.ratingAlertDescription", "-")))));
            projectionList.add(Projections.computed("result", "$Ratings.ratingAlertDescription"));
        } else if (fieldMap.fieldID.contains("_ACTN")) {
            projectionList.add(Projections.computed("result", "$Ratings.ratingActionDescription"));
        } else {
            projectionList.add(Projections.computed("result", "$Ratings.ratingCode"));
        }
        aggregateList.add(Aggregates.project(Projections.fields(projectionList)));
    }

    private void makeFC_IRRAggregation(MetaType.FieldMap fieldMap, List<Bson> aggregateList) {
        aggregateList.add(Aggregates.project(Projections.computed("Ratings", JSON.parse("{$concatArrays:[\"$currentInternationalRatings\",\"$currentNationalRatings\"]}"))));
        aggregateList.add(Aggregates.match(Filters.not(Filters.eq("Ratings", null))));
        aggregateList.add(Aggregates.unwind("$Ratings"));
        aggregateList.add(Aggregates.match(Filters.eq("Ratings.ratingTypeID", Integer.parseInt(fieldMap.lookUpSourceList.get(0).value))));
        aggregateList.add(Aggregates.match(Filters.exists("Ratings.ratingRecovery")));
        aggregateList.add(Aggregates.project(Projections.fields(
                Projections.computed("result", "$Ratings.ratingRecovery"),
                Projections.computed("ID", "$Ratings.ratableID"),
                Projections.computed("ratingDate", "$Ratings.ratingEffectiveDate")
        )));
    }

    private void makeFC_IDRRAggregation(MetaType.FieldMap fieldMap, List<Bson> aggregateList) {

    }

    private void makeSolicitationAggregation(MetaType.FieldMap fieldMap, List<Bson> aggregateList) {
        aggregateList.add(Aggregates.project(Projections.fields(
                Projections.computed("Ratings",
                        JSON.parse("{$concatArrays:[\"$currentInternationalRatings\",\"$currentNationalRatings\"]}")),
                Projections.computed("issueSolicitationVO", "$issueSolicitationVO"))));
        aggregateList.add(Aggregates.match(Filters.not(Filters.eq("Ratings", null))));
        aggregateList.add(Aggregates.match(Filters.exists("issueSolicitationVO")));
        aggregateList.add(Aggregates.unwind("$issueSolicitationVO"));
        aggregateList.add(Aggregates.unwind("$Ratings"));
        aggregateList.add(Aggregates.match(Filters.eq("Ratings.ratingTypeID", Integer.parseInt(fieldMap.lookUpSourceList.get(0).value))));
        aggregateList.add(Aggregates.match(Filters.eq("issueSolicitationVO.rtngTypeId", Integer.parseInt(fieldMap.lookUpSourceList.get(0).value))));
        aggregateList.add(Aggregates.project(Projections.fields(
                Projections.computed("result", "$issueSolicitationVO.solicitTypeCode"),
                Projections.computed("ID", "$issueSolicitationVO.rtbleID"),
                Projections.computed("ratingDate", "$Ratings.ratingEffectiveDate")
        )));
    }
}
