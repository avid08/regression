package com.DmetadataRegression.FitchRatings.LookUps;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.DmetadataRegression.Helpers.MetaType;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.util.JSON;

import javafx.util.Pair;

public class Issuer extends RatingsLookUpType {
    public Issuer(String collection, MongoDatabase database) {
        super(collection, database);
    }

    @Override
    public Pair<Document, String> extractDocument(MetaType.FieldMap fieldMap) {
        MongoCollection<Document> issueCollection = database.getCollection(collection);
        boolean isValidAgentID = false;
        Document result = null;
        List<Bson> aggregateList = convertMetaToMongoQuery(fieldMap);
        List<Long> agentList = new ArrayList<>();
        while (!isValidAgentID) {
            result = issueCollection.aggregate(aggregateList).first();
            Long agentID = result != null ? getAgentIfInvalid(result) : null;
            if (agentID != null) {
                agentList.add(agentID);
                aggregateList.clear();
                agentList.forEach(agent -> aggregateList.add(Aggregates.match(Filters.not(Filters.eq("agentID", agent)))));
                aggregateList.addAll(convertMetaToMongoQuery(fieldMap));
            } else isValidAgentID = true;
        }
        if (result != null) {
            String value;
            if (result.get("result") instanceof Date) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                value = df.format(result.getDate("ratingDate"));
            } else value = result.getString("result");
            return new Pair<>(result, value);
        }
        return new Pair<>(null, null);
    }

    /**
     * Converts a fieldMap's {@code fieldID} to a mongo aggregation
     *
     * @param fieldMap extracts the fieldID from here
     * @return mongo aggregateList
     */
    private List<Bson> convertMetaToMongoQuery(MetaType.FieldMap fieldMap) {
        List<Bson> aggregateList = new ArrayList<>();
        if (fieldMap.fieldID.contains("_SOLST")) {
            makeSolicitationAggregation(fieldMap, aggregateList);
        } else {
            makeGeneralAggregation(fieldMap, aggregateList);
        }
        return aggregateList;
    }

    private void makeGeneralAggregation(MetaType.FieldMap fieldMap, List<Bson> aggregateList) {
        aggregateList.add(Aggregates.match(Filters.exists("issuerRatingVOList")));
        aggregateList.add(Aggregates.unwind("$issuerRatingVOList"));
        aggregateList.add(Aggregates.match(Filters.eq("issuerRatingVOList.ratingTypeID", Integer.parseInt(fieldMap.lookUpSourceList.get(0).value))));
        List<Bson> projectionList = new ArrayList<>();
        projectionList.add(Projections.computed("ID", "$agentID"));
        projectionList.add(Projections.computed("ratingDate", "$issuerRatingVOList.effectiveDate"));
        projectionList.add(Projections.computed("groupType", "$groupType"));
        if (fieldMap.fieldID.contains("_DT")) {
            projectionList.add(Projections.computed("result", "$issuerRatingVOList.effectiveDate"));
        } else if (fieldMap.fieldID.contains("_ALRT")) {
            aggregateList.add(Aggregates.match(Filters.and(
                    Filters.exists("issuerRatingVOList.ratingAlertDescription"),
                    Filters.not(Filters.eq("issuerRatingVOList.ratingAlertDescription", "-")))));
            projectionList.add(Projections.computed("result", "$issuerRatingVOList.ratingAlertDescription"));
        } else if (fieldMap.fieldID.contains("_ACTN")) {
            projectionList.add(Projections.computed("result", "$issuerRatingVOList.ratingActionDescription"));
        } else {
            projectionList.add(Projections.computed("result", "$issuerRatingVOList.ratingCode"));
        }
        aggregateList.add(Aggregates.project(Projections.fields(projectionList)));
    }

    private void makeSolicitationAggregation(MetaType.FieldMap fieldMap, List<Bson> aggregateList) {
        aggregateList.add(Aggregates.match(Filters.exists("issuerRatingVOList")));
        aggregateList.add(Aggregates.match(Filters.exists("issuerSolicitationVO")));
        aggregateList.add(Aggregates.unwind("$issuerSolicitationVO"));
        aggregateList.add(Aggregates.unwind("$issuerRatingVOList"));
        aggregateList.add(Aggregates.match(Filters.eq("issuerSolicitationVO.rtngTypeId", Integer.parseInt(fieldMap.lookUpSourceList.get(0).value))));
        aggregateList.add(Aggregates.match(Filters.eq("issuerRatingVOList.ratingTypeID", Integer.parseInt(fieldMap.lookUpSourceList.get(0).value))));
        aggregateList.add(Aggregates.project(Projections.fields(
                Projections.computed("result", "$issuerSolicitationVO.solicitTypeCode"),
                Projections.computed("ID", "$agentID"),
                Projections.computed("ratingDate", "$issuerRatingVOList.effectiveDate")
        )));
    }

    /**
     * Searches if there are more than two issues with the same {@code agentID}.
     * If there are - only the ones with allowed {@code groupTypeID} could be returned by the API.
     *
     * @param document its {@code agentID} and {@code groupTypeID}
     * @return {@code null} if the {@code agentID} is valid, {@code agentID} itself if it's not valid
     */
    private Long getAgentIfInvalid(Document document) {
        MongoCollection<Document> issueCollection = database.getCollection(collection);
        List<Bson> agentAggregateList = new ArrayList<>();
        agentAggregateList.add(Aggregates.match(Filters.eq("agentID", document.getLong("ID"))));
        agentAggregateList.add(Aggregates.group(JSON.parse("{ _id: null}"), Accumulators.sum("count", JSON.parse("{ $sum: 1 } "))));
        AggregateIterable<Document> countAggregate = issueCollection.aggregate(agentAggregateList);
        Set<Integer> groupTypeSet = new HashSet<>(Arrays.asList(4, 20, 22, 28, 30));
        if (countAggregate.first().getInteger("count") > 1) {
            return groupTypeSet.contains(document.getInteger("groupType")) ? null : document.getLong("ID");
        }
        return null;
    }

}
