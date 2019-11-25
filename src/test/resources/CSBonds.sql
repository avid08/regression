SELECT MAX(case when identity_name = 'AgentID' then convert(identity_value, unsigned integer) end) as AgentID
,MAX(case when element_type = 'FC_BONDID' then element_value end) AS FC_BONDID
,MAX(case when element_type = 'FC_PERMID' then element_value end) AS FC_PERMID
,MAX(case when element_type = 'FC_MARKET_ISSUANCE' then element_value end) AS FC_MARKET_ISSUANCE
,MAX(case when element_type = 'FC_LAUNCH_DATE' then element_value end) AS FC_LAUNCH_DATE
,MAX(case when element_type = 'FC_PRICING_DT' then element_value end) AS FC_PRICING_DT
,MAX(case when element_type = 'FC_SETTLEMENT_DATE' then element_value end) AS FC_SETTLEMENT_DATE
,MAX(case when element_type = 'FC_ISSUE_CURRENCY' then element_value end) AS FC_ISSUE_CURRENCY
,MAX(case when element_type = 'FC_ISSUE_AMOUNT' then element_value end) AS FC_ISSUE_AMOUNT
,MAX(case when element_type = 'FC_EURO_EQV' then element_value end) AS FC_EURO_EQV
,MAX(case when element_type = 'FC_USD_EQV' then element_value end) AS FC_USD_EQV
,MAX(case when element_type = 'FC_CASH_PIK' then element_value end) AS FC_CASH_PIK
,MAX(case when element_type = 'FC_ISSUE_COUPON_RATE' then element_value end) AS FC_ISSUE_COUPON_RATE
,MAX(case when element_type = 'FC_YTM' then element_value end) AS FC_YTM
,MAX(case when element_type = 'FC_OID_ISSUEPRICE' then element_value end) AS FC_OID_ISSUEPRICE
,MAX(case when element_type = 'FC_MATURITY_DATE' then element_value end) AS FC_MATURITY_DATE
,MAX(case when element_type = 'FC_TENOR' then element_value end) AS FC_TENOR
,MAX(case when element_type = 'FC_LIBOR_RT' then element_value end) AS FC_LIBOR_RT
,MAX(case when element_type = 'FC_Expected' then element_value end) AS FC_Expected
,MAX(case when element_type = 'FC_FLOOR' then element_value end) AS FC_FLOOR
,MAX(case when element_type = 'FC_SPECIAL' then element_value end) AS FC_SPECIAL
,MAX(case when element_type = 'FC_ADD_ON' then element_value end) AS FC_ADD_ON
,MAX(case when element_type = 'FC_CALL_STRUCTURE' then element_value end) AS FC_CALL_STRUCTURE
,MAX(case when element_type = 'FC_DEAL_TYPE' then element_value end) AS FC_DEAL_TYPE
,MAX(case when element_type = 'FC_ISSUE_STATUS' then element_value end) AS FC_ISSUE_STATUS
,MAX(case when element_type = 'FC_TRANSACTION_TYPE' then element_value end) AS FC_TRANSACTION_TYPE
,MAX(case when element_type = 'FC_PRICE_TALK' then element_value end) AS FC_PRICE_TALK
,MAX(case when element_type = 'FC_EQUITY_CLAW_PERCENTAGE' then element_value end) AS FC_EQUITY_CLAW_PERCENTAGE
,MAX(case when element_type = 'FC_EQUITY_CLAW_PRICE' then element_value end) AS FC_EQUITY_CLAW_PRICE
,MAX(case when element_type = 'FC_EQUITY_CLAW_YEARS' then element_value end) AS FC_EQUITY_CLAW_YEARS
,MAX(case when element_type = 'FC_TOTAL_LEVERAGE' then element_value end) AS FC_TOTAL_LEVERAGE
FROM transaction_element te JOIN tranche t ON t.id = te.entity_id
  INNER JOIN transaction_role tr  ON tr.transaction_id = t.transaction_id AND tr.transaction_role = 'Issuer'
  INNER JOIN entity_cross_reference ecr ON ecr.entity_id = tr.business_entity_id AND ecr.entity_type = 'Business Entity'  AND ecr.identity_authority = 'Fitch Solutions'
WHERE te.information_provenance = 'capstr'
and te.entity_type = 'Tranche'
and category = 'Bond public data'
GROUP BY te.entity_id
ORDER BY AgentID;