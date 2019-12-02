select id, attribute_name, enabled, last_updated from "configuration".cr_attribute_metadata where attribute_name in (
'FC_DEAL_ID',
'FC_PERMID',
'FC_MARKET_ISSUANCE',
'FC_COMPANY_NAME',
'FC_LAUNCH_DATE',
'FC_CURR',
'FC_ISSUE_AMOUNT',
'FC_TRANSACTION_TYPE',
'FC_EQUITY_OWNERSHIP',
'FC_ISSUE_MARKET_SECTOR_DESC',
'FC_COUNTRY',
'FC_ISSUER_NAME',
'FC_ISSUE_AMOUNT_EURO',
'FC_ISSUE_AMOUNT_USD',
'FC_1L_LEV',
'FC_SCRD_LEV',
'FC_TTL_LEV',
'FC_SPCL_FEATURES',
'FC_MEETING_DT',
'FC_COMMIT_DT',
'FC_CLOSING_DT',
'FC_TERM_LOANB',
'FC_TLB_CCY',
'FC_TLB_ISSUE_AMOUNT',
'FC_TLB_RATE',
'FC_TLB_MARGIN',
'FC_TLB_FLOOR',
'FC_TLB_OID',
'FC_TLB_TERM',
'FC_TLB_PAYMENT',
'FC_TLB_EURO',
'FC_TENOR',
'FC_3MNTH_LIBOR',
'FC_YTM',
'FC_PRICE_TALK',
'FC_2L',
'FC_2L_CCY',
'FC_2L_ISSUE_AMOUNT',
'FC_2L_RATE',
'FC_2L_MARGIN',
'FC_2L_FLOOR',
'FC_2LOID',
'FC_2L_TERM',
'FC_2L_PAYMENT',
'FC_2L_EURO',
'FC_AGENT_ID',
'FC_CALL_PROTECTION',
'FC_CONCURRENT_LOAN_ID',
'FC_INDUSTRY_DESCRIPTION',
'FC_INDUSTRY',
'FC_ISSUER_NAME',
'FC_LFYHYID',
'FC_MARKET_ISSUANCE',
'FC_PURPOSE',
'FC_SPREAD',
'FC_UNDERWRITER',
'FC_1ST_CALL_DT',
'FC_1ST_CALL_PRICE',
'FC_LAUNCH_DATE',
'FC_BOND_TYPE',
'FC_CALL_PERCENTAGE',
'FC_CALL_PRIM',
'FC_CALL_SCHEDULE',
'FC_CASH_PIK',
'FC_CHANGE_OF_CONTROL',
'FC_CHANGE_ON_BREAK',
'FC_DAYS_IN_MARKET',
'FC_DEBUT',
'FC_DESCRIPTION',
'FC_FNL_DOC_SCR',
'FC_EBITDA',
'FC_EQUITY_PERCENTAGE',
'FC_EQUITY_CLAW_PERCENTAGE',
'FC_EQUITY_CLAW_PRICE',
'FC_EQUITY_CLAW_YEARS',
'FC_ISSUE_COUPON_RATE',
'FC_ISSUE_PRICE_FNL',
'FC_MATURITY_DATE',
'FC_ISSUE_AMOUNT',
'FC_FIXED_FLOATING',
'FC_ISSUE_PRICE_PRLM',
'FC_SIZE_PRLM',
'FC_TALK_PRLM',
'FC_SCRD_LEV',
'FC_TTL_LEV',
'FC_NC_YEARS',
'FC_NOTES',
'FC_ISSUE_CUSIP',
'FC_OWNERSHIP',
'FC_PRICING_DT',
'FC_TRANSACTION_TYPE',
'FC_CMNT_PURPOSE',
'FC_ROAD_SHOW_DATE',
'FC_SECONDARY',
'FC_SENIORITY',
'FC_SPECIAL_CALL',
'FC_LIBOR_SPREAD',
'FC_TARGET',
'FC_TENOR',
'FC_TRANSACTION',
'FC_YTM',
'FC_PERMID',
'FC_PERMNAME',
'FC_AGENT_ID',
'FC_3MNTH_LIBOR',
'FC_ADD_ON',
'FC_AGENT_ID',
'FC_BONDID',
'FC_CALL_STRUCTURE',
'FC_CASH_PIK',
'FC_DEAL_TYPE',
'FC_EQUITY_CLAW_PERCENTAGE',
'FC_EQUITY_CLAW_PRICE',
'FC_EQUITY_CLAW_YEARS',
'FC_Expected',
'FC_FLOOR',
'FC_ISSUE_AMOUNT',
'FC_ISSUE_AMOUNT_EURO',
'FC_ISSUE_AMOUNT_USD',
'FC_ISSUE_COUPON_RATE',
'FC_ISSUE_CURRENCY',
'FC_ISSUE_STATUS',
'FC_LAUNCH_DATE',
'FC_MARKET_ISSUANCE',
'FC_MATURITY_DATE',
'FC_OID_ISSUEPRICE',
'FC_PERMID',
'FC_PRICE_TALK',
'FC_PRICING_DT',
'FC_SETTLEMENT_DATE',
'FC_SPCL_FEATURES',
'FC_TENOR',
'FC_TRANSACTION_TYPE',
'FC_TTL_LEV',
'FC_YTM'
) and source_information_provenance in ('levfin', 'capstr')
and not (source_information_provenance='levfin' and source_category='Loan public data')