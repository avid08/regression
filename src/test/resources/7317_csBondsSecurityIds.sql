select distinct(security_id)
from master.security_attributes sa
  join master.sources ms on sa.source_id = ms.source_id
where ms.source_id=8 order by security_id limit 10