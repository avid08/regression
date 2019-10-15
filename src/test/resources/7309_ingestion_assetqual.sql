select resource
from ingestion.fitch_ratings_navigator_instance as ing
where operation='INSERT' and resource_type = 'RATINGS_NAVIGATOR_INSTANCE'
and ing.resource->>'Type'='BANK'
and ing.resource->'RNI'->'MetaData'->'EntityInfo'->>'AgentID'='111961'
and ing.resource->'RNI'->'ThisEntity'->'ThisRatingAction'->0->>'IDX'='VR'
and ing.resource->'RNI'->'ThisEntity'->'ThisRatingAction'->0->'Factors'->4->>'IDX'='FinProfile'
and ing.resource->'RNI'->'ThisEntity'->'ThisRatingAction'->0->'Factors'->4->'SubFactors'->0->>'IDX'='AssetQual';