SELECT cast(issuer_entity_id as integer) as AgentId
,MAX(case when field_id = 'FC_AGENT_ID' then cast(value as integer) end) AS FC_AGENT_ID
,MAX(case when field_id = 'FC_BONDID' then value end) AS FC_BONDID
,MAX(case when field_id = 'FC_3MNTH_LIBOR' then value end) AS FC_3MNTH_LIBOR
,MAX(case when field_id = 'FC_ADD_ON' then value end) AS FC_ADD_ON
,MAX(case when field_id = 'FC_CALL_STRUCTURE' then value end) AS FC_CALL_STRUCTURE
,MAX(case when field_id = 'FC_CASH_PIK' then value end) AS FC_CASH_PIK
,MAX(case when field_id = 'FC_DEAL_TYPE' then value end) AS FC_DEAL_TYPE
,MAX(case when field_id = 'FC_EQUITY_CLAW_PERCENTAGE' then value end) AS FC_EQUITY_CLAW_PERCENTAGE
,MAX(case when field_id = 'FC_EQUITY_CLAW_PRICE' then value end) AS FC_EQUITY_CLAW_PRICE
,MAX(case when field_id = 'FC_EQUITY_CLAW_YEARS' then value end) AS FC_EQUITY_CLAW_YEARS
,MAX(case when field_id = 'FC_EXPECTED' then value end) AS FC_EXPECTED
,MAX(case when field_id = 'FC_FLOOR' then value end) AS FC_FLOOR
,MAX(case when field_id = 'FC_ISSUE_AMOUNT' then value end) AS FC_ISSUE_AMOUNT
,MAX(case when field_id = 'FC_ISSUE_AMOUNT_EURO' then value end) AS FC_ISSUE_AMOUNT_EURO
,MAX(case when field_id = 'FC_ISSUE_AMOUNT_USD' then value end) AS FC_ISSUE_AMOUNT_USD
,MAX(case when field_id = 'FC_ISSUE_COUPON_RATE' then value end) AS FC_ISSUE_COUPON_RATE
,MAX(case when field_id = 'FC_ISSUE_CURRENCY' then value end) AS FC_ISSUE_CURRENCY
,MAX(case when field_id = 'FC_ISSUE_STATUS' then value end) AS FC_ISSUE_STATUS
,MAX(case when field_id = 'FC_MARKET_ISSUANCE' then value end) AS FC_MARKET_ISSUANCE
,MAX(case when field_id = 'FC_MATURITY_DATE' then value end) AS FC_MATURITY_DATE
,MAX(case when field_id = 'FC_OID_ISSUEPRICE' then value end) AS FC_OID_ISSUEPRICE
,MAX(case when field_id = 'FC_PERMID' then value end) AS FC_PERMID
,MAX(case when field_id = 'FC_PRICE_TALK' then value end) AS FC_PRICE_TALK
,MAX(case when field_id = 'FC_PRICING_DT' then value end) AS FC_PRICING_DT
,MAX(case when field_id = 'FC_SETTLEMENT_DATE' then value end) AS FC_SETTLEMENT_DATE
,MAX(case when field_id = 'FC_SPCL_FEATURES' then value end) AS FC_SPCL_FEATURES
,MAX(case when field_id = 'FC_TENOR' then value end) AS FC_TENOR
,MAX(case when field_id = 'FC_TRANSACTION_TYPE' then value end) AS FC_TRANSACTION_TYPE
,MAX(case when field_id = 'FC_TTL_LEV' then value end) AS FC_TTL_LEV
,MAX(case when field_id = 'FC_YTM' then value end) AS FC_YTM
FROM master.security_attributes m
  join master.securities sec on security_id = sec.fc_sec_id
where field_id in ('FC_AGENT_ID', 'FC_3MNTH_LIBOR', 'FC_ADD_ON', 'FC_AGENT_ID', 'FC_BONDID', 'FC_CALL_STRUCTURE', 'FC_CASH_PIK', 'FC_DEAL_TYPE', 'FC_EQUITY_CLAW_PERCENTAGE', 'FC_EQUITY_CLAW_PRICE', 'FC_EQUITY_CLAW_YEARS', 'FC_EXPECTED', 'FC_FLOOR', 'FC_ISSUE_AMOUNT', 'FC_ISSUE_AMOUNT_EURO', 'FC_ISSUE_AMOUNT_USD', 'FC_ISSUE_COUPON_RATE', 'FC_ISSUE_CURRENCY', 'FC_ISSUE_STATUS', 'FC_LAUNCH_DATE', 'FC_MARKET_ISSUANCE', 'FC_MATURITY_DATE', 'FC_OID_ISSUEPRICE', 'FC_PERMID', 'FC_PRICE_TALK', 'FC_PRICING_DT', 'FC_SETTLEMENT_DATE', 'FC_SPCL_FEATURES', 'FC_TENOR', 'FC_TRANSACTION_TYPE', 'FC_TTL_LEV', 'FC_YTM')
and m.source_id = 8
group by security_id, issuer_entity_id order by FC_AGENT_ID, FC_BONDID;