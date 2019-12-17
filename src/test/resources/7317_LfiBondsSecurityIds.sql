select distinct(security_id), issuer_entity_id
from master.security_attributes sa
  join master.sources ms on sa.source_id = ms.source_id
  join master.securities sec on security_id = sec.fc_sec_id
where ms.source_id=6 order by security_id limit 10