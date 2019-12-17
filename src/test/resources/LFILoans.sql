SELECT convert(ecr.identity_value, unsigned integer) AS "AGENT ID"
,MAX(case when te.element_type = 'FC_LAUNCH_DATE' then te.element_value end) AS FC_LAUNCH_DATE
,MAX(case when te.element_type = 'FC_MEETING_DT' then te.element_value end) AS FC_MEETING_DT
,MAX(case when te.element_type = 'FC_COMMIT_DT' then te.element_value end) AS FC_COMMIT_DT
,MAX(case when te.element_type = 'FC_OWNERSHIP' then te.element_value end) AS FC_OWNERSHIP
,MAX(case when te.element_type = 'FC_TRANSACTION_TYPE' then te.element_value end) AS FC_TRANSACTION_TYPE
,MAX(case when te.element_type = 'FC_TRANCHE_NM' then te.element_value end) AS FC_TRANCHE_NM
,MAX(case when te.element_type = 'FC_ISSUE_AMOUNT' and te.transaction_phase = "Final" then te.element_value end) AS FC_ISSUE_AMOUNT_FNL
,MAX(case when te.element_type = 'FC_FLOOR' and te.transaction_phase = "Final" then te.element_value end) AS FC_FLOOR_FNL
,MAX(case when te.element_type = 'FC_OID' and te.transaction_phase = "Final" then te.element_value end) AS FC_OID_FNL
,MAX(case when te.element_type = 'FC_TERM' and te.transaction_phase = "Final" then te.element_value end) AS FC_TERM_FNL
,MAX(case when te.element_type = 'FC_MATURITY_DATE' and te.transaction_phase = "Final" then te.element_value end) AS FC_MATURITY_DATE_FNL
,MAX(case when te.element_type = 'FC_YTM' and te.transaction_phase = "Final" then te.element_value end) AS FC_YTM_FNL
,MAX(case when te.element_type = 'FC_YT3' and te.transaction_phase = "Final" then te.element_value end) AS FC_YT3_FNL
,MAX(case when te.element_type = 'FC_CALLS' and te.transaction_phase = "Final" then te.element_value end) AS FC_CALLS_FNL
,MAX(case when te.element_type = 'FC_FINANCIAL_COVENANT' and te.transaction_phase = "Final" then te.element_value end) AS FC_FINANCIAL_COVENANT_FNL
,MAX(case when te.element_type = 'FC_INCREMENTAL_TERMS' and te.transaction_phase = "Final" then te.element_value end) AS FC_INCREMENTAL_TERMS_FNL
,MAX(case when te.element_type = 'FC_COVENANT_COMMENT' then te.element_value end) AS FC_COVENANT_COMMENT
,MAX(case when te.element_type = 'FC_EBITDA' then te.element_value end) AS FC_EBITDA
,MAX(case when te.element_type = 'FC_TTL_LEV' then te.element_value end) AS FC_TTL_LEV
,MAX(case when te.element_type = 'FC_EQTY_CONT_LBO' then te.element_value end) AS FC_EQTY_CONT_LBO
,MAX(case when te.element_type = 'FC_REVLVR_SIZE' then te.element_value end) AS FC_REVLVR_SIZE
,MAX(case when te.element_type = 'FC_TLA' then te.element_value end) AS FC_TLA
,MAX(case when te.element_type = 'FC_BOND_AMT_CONCURT' then te.element_value end) AS FC_BOND_AMT_CONCURT
,MAX(case when te.element_type = 'FC_CMNT_PURPOSE' then te.element_value end) AS FC_CMNT_PURPOSE
,MAX(case when te.element_type = 'FC_LEAD_ARRANGERS' then te.element_value end) AS FC_LEAD_ARRANGERS
,MAX(case when te.element_type = 'FC_ISSUE_AMOUNT' and te.transaction_phase = "Preliminary" then te.element_value end) AS FC_ISSUE_AMOUNT_PRLM
,MAX(case when te.element_type = 'FC_INITIAL_SPRD' then te.element_value end) AS FC_INITIAL_SPRD
,MAX(case when te.element_type = 'FC_FLOOR' and te.transaction_phase = "Preliminary" then te.element_value end) AS FC_FLOOR_PRLM
,MAX(case when te.element_type = 'FC_OID' and te.transaction_phase = "Preliminary" then te.element_value end) AS FC_OID_PRLM
,MAX(case when te.element_type = 'FC_TERM' and te.transaction_phase = "Preliminary" then te.element_value end) AS FC_TERM_PRLM
,MAX(case when te.element_type = 'FC_MATURITY_DATE' and te.transaction_phase = "Preliminary" then te.element_value end) AS FC_MATURITY_DATE_PRLM
,MAX(case when te.element_type = 'FC_YTM' and te.transaction_phase = "Preliminary" then te.element_value end) AS FC_YTM_PRLM
,MAX(case when te.element_type = 'FC_YT3' and te.transaction_phase = "Preliminary" then te.element_value end) AS FC_YT3_PRLM
,MAX(case when te.element_type = 'FC_CALLS' and te.transaction_phase = "Preliminary" then te.element_value end) AS FC_CALLS_PRLM
,MAX(case when te.element_type = 'FC_FINANCIAL_COVENANT' and te.transaction_phase = "Preliminary" then te.element_value end) AS FC_FINANCIAL_COVENANT_PRLM
,MAX(case when te.element_type = 'FC_INCREMENTAL_TERMS' and te.transaction_phase = "Preliminary" then te.element_value end) AS FC_INCREMENTAL_TERMS_PRLM
,MAX(case when te.element_type = 'FC_INITIAL_COV' then te.element_value end) AS FC_INITIAL_COV
,MAX(case when te.element_type = 'FC_INITIAL_OTHR' then te.element_value end) AS FC_INITIAL_OTHR
,MAX(case when te.element_type = 'FC_FLX_DT_LN1' then te.element_value end) AS FC_FLX_DT_LN1
,MAX(case when te.element_type = 'FC_FLX_TYPE_LN1' then te.element_value end) AS FC_FLX_TYPE_LN1
,MAX(case when te.element_type = 'FC_BID_PRICE' then te.element_value end) AS FC_BID_PRICE
,MAX(case when te.element_type = 'FC_OFFER_PRC' then te.element_value end) AS FC_OFFER_PRC
,MAX(case when te.element_type = 'FC_BREAK_DT' then te.element_value end) AS FC_BREAK_DT
,MAX(case when te.element_type = 'FC_DL_CAT' then te.element_value end) AS FC_DL_CAT
,MAX(case when te.element_type = 'FC_PRVT_PLCD_SLTL' then te.element_value end) AS FC_PRVT_PLCD_SLTL
,MAX(case when te.element_type = 'FC_PERMID' then te.element_value end) AS FC_PERMID
,MAX(case when te.element_type = 'FC_PERMNAME' then te.element_value end) AS FC_PERMNAME
,MAX(case when te.element_type = 'FC_MFN' then te.element_value end) AS FC_MFN
,MAX(case when te.element_type = 'FC_REPAY_AMT' then te.element_value end) AS FC_REPAY_AMT
,MAX(case when te.element_type = 'FC_SPRD_TIGHT_C' then te.element_value end) AS FC_SPRD_TIGHT_C
,MAX(case when te.element_type = 'FC_SPRD_WIDE_C' then te.element_value end) AS FC_SPRD_WIDE_C
,MAX(case when te.element_type = 'FC_ORG_SPRD1_C' then te.element_value end) AS FC_ORG_SPRD1_C
,MAX(case when te.element_type = 'FC_ORG_SPRD2_C' then te.element_value end) AS FC_ORG_SPRD2_C
,MAX(case when te.element_type = 'FC_SPREAD_CAT_C' then te.element_value end) AS FC_SPREAD_CAT_C
,MAX(case when te.element_type = 'FC_ORG_SPRD_CAT_C' then te.element_value end) AS FC_ORG_SPRD_CAT_C
,MAX(case when te.element_type = 'FC_OID_TIGHT_C' then te.element_value end) AS FC_OID_TIGHT_C
,MAX(case when te.element_type = 'FC_OID_WIDE_C' then te.element_value end) AS FC_OID_WIDE_C
,MAX(case when te.element_type = 'FC_ORG_OID1_C' then te.element_value end) AS FC_ORG_OID1_C
,MAX(case when te.element_type = 'FC_ORG_OID2_C' then te.element_value end) AS FC_ORG_OID2_C
,MAX(case when te.element_type = 'FC_OID_CAT_C' then te.element_value end) AS FC_OID_CAT_C
,MAX(case when te.element_type = 'FC_ORG_OID_CAT_C' then te.element_value end) AS FC_ORG_OID_CAT_C
,MAX(case when te.element_type = 'FC_PRC_DT_C' then te.element_value end) AS FC_PRC_DT_C
,MAX(case when te.element_type = 'FC_SPRD_AVG_LN1_C' then te.element_value end) AS FC_SPRD_AVG_LN1_C
,MAX(case when te.element_type = 'FC_FLOOR_LN1_C' then te.element_value end) AS FC_FLOOR_LN1_C
,MAX(case when te.element_type = 'FC_OID_AVG1_C' then te.element_value end) AS FC_OID_AVG1_C
,MAX(case when te.element_type = 'FC_AVG_YTM_LN1_C' then te.element_value end) AS FC_AVG_YTM_LN1_C
,MAX(case when te.element_type = 'FC_RATCAT_LN1_C' then te.element_value end) AS FC_RATCAT_LN1_C
,MAX(case when te.element_type = 'FC_SPRD_AVG_LN2_C' then te.element_value end) AS FC_SPRD_AVG_LN2_C
,MAX(case when te.element_type = 'FC_FLOOR_LN2_C' then te.element_value end) AS FC_FLOOR_LN2_C
,MAX(case when te.element_type = 'FC_OID_AVG2_C' then te.element_value end) AS FC_OID_AVG2_C
,MAX(case when te.element_type = 'FC_AVG_YTM_LN2_C' then te.element_value end) AS FC_AVG_YTM_LN2_C
,MAX(case when te.element_type = 'FC_RATCAT_LN2_C' then te.element_value end) AS FC_RATCAT_LN2_C
,MAX(case when te.element_type = 'FC_B3_C' then te.element_value end) AS FC_B3_C
,MAX(case when te.element_type = 'FC_RP_CHNG' then te.element_value end) AS FC_RP_CHNG
,MAX(case when te.element_type = 'FC_SUNSET_CMT' then te.element_value end) AS FC_SUNSET_CMT
,MAX(case when te.element_type = 'FC_ASSET_SL_CNG' then te.element_value end) AS FC_ASSET_SL_CNG
,MAX(case when te.element_type = 'FC_ECF_CHNG' then te.element_value end) AS FC_ECF_CHNG
,MAX(case when te.element_type = 'FC_INCR_FCLTY_CHNG' then te.element_value end) AS FC_INCR_FCLTY_CHNG
,MAX(case when te.element_type = 'FC_FIN_COV_CHNG' then te.element_value end) AS FC_FIN_COV_CHNG
,MAX(case when te.element_type = 'FC_GRD_CHNG' then te.element_value end) AS FC_GRD_CHNG
,MAX(case when te.element_type = 'FC_STORY' then te.element_value end) AS FC_STORY
,MAX(case when te.element_type = 'FC_DOC_CHNG_DT' then te.element_value end) AS FC_DOC_CHNG_DT
,MAX(case when te.element_type = 'FC_ISSUE1_C' then te.element_value end) AS FC_ISSUE1_C
,MAX(case when te.element_type = 'FC_TENOR1_C' then te.element_value end) AS FC_TENOR1_C
,MAX(case when te.element_type = 'FC_SPRD1_C' then te.element_value end) AS FC_SPRD1_C
,MAX(case when te.element_type = 'FC_FLOOR1_C' then te.element_value end) AS FC_FLOOR1_C
,MAX(case when te.element_type = 'FC_OID1_C' then te.element_value end) AS FC_OID1_C
,MAX(case when te.element_type = 'FC_YTM1_C' then te.element_value end) AS FC_YTM1_C
,MAX(case when te.element_type = 'FC_YT3_YR1_C' then te.element_value end) AS FC_YT3_YR1_C
,MAX(case when te.element_type = 'FC_LEV_TRSN_LN1_C' then te.element_value end) AS FC_LEV_TRSN_LN1_C
,MAX(case when te.element_type = 'FC_INCRMT_FCTLY1_C' then te.element_value end) AS FC_INCRMT_FCTLY1_C
,MAX(case when te.element_type = 'FC_OTHER1_C' then te.element_value end) AS FC_OTHER1_C
,MAX(case when te.element_type = 'FC_INCRMT_FCTLY2_C' then te.element_value end) AS FC_INCRMT_FCTLY2_C
,MAX(case when te.element_type = 'FC_TRAN_CNT' then te.element_value end) AS FC_TRAN_CNT
,MAX(case when te.element_type = 'FC_CALL_MONTHS_C' then te.element_value end) AS FC_CALL_MONTHS_C
,MAX(case when te.element_type = 'FC_PURPOSE_C' then te.element_value end) AS FC_PURPOSE_C
,MAX(case when te.element_type = 'FC_SPONSORED_C' then te.element_value end) AS FC_SPONSORED_C
,MAX(case when te.element_type = 'FC_REPRC' then te.element_value end) AS FC_REPRC
,MAX(case when te.element_type = 'FC_SPRD_REPRC_DL' then te.element_value end) AS FC_SPRD_REPRC_DL
,MAX(case when te.element_type = 'FC_FL_REPRCD_DEAL' then te.element_value end) AS FC_FL_REPRCD_DEAL
,MAX(case when te.element_type = 'FC_AMT_REPRICD' then te.element_value end) AS FC_AMT_REPRICD
,MAX(case when te.element_type = 'FC_SAVNG_C' then te.element_value end) AS FC_SAVNG_C
,MAX(case when te.element_type = 'FC_LIBOR_RT' then te.element_value end) AS FC_LIBOR_RT
,MAX(case when te.element_type = 'FC_REPAY_101' then te.element_value end) AS FC_REPAY_101
,MAX(case when te.element_type = 'FC_DELAY_CLS' then te.element_value end) AS FC_DELAY_CLS
,MAX(case when te.element_type = 'FC_CURR' then te.element_value end) AS FC_CURR
,MAX(case when te.element_type = 'FC_PIK' then te.element_value end) AS FC_PIK
,MAX(case when te.element_type = 'FC_FXD_FLTING' then te.element_value end) AS FC_FXD_FLTING
,MAX(case when te.element_type = 'FC_DDTL_AMT' then te.element_value end) AS FC_DDTL_AMT
,MAX(case when te.element_type = 'FC_DDTL_AVL_MTH' then te.element_value end) AS FC_DDTL_AVL_MTH
,MAX(case when te.element_type = 'FC_50_MARGIN' then te.element_value end) AS FC_50_MARGIN
,MAX(case when te.element_type = 'FC_100_MARGIN' then te.element_value end) AS FC_100_MARGIN
,MAX(case when te.element_type = 'FC_INITIAL_FLT_FEE' then te.element_value end) AS FC_INITIAL_FLT_FEE
,MAX(case when te.element_type = 'FC_DESC_TERMS' then te.element_value end) AS FC_DESC_TERMS
,MAX(case when te.element_type = 'FC_FNL_DOC_SCR' then te.element_value end) AS FC_FNL_DOC_SCR
,MAX(case when te.element_type = 'FC_CALL_PROT_LN1_C' then te.element_value end) AS FC_CALL_PROT_LN1_C
,MAX(case when te.element_type = 'FC_CALL_PROT_LN2_C' then te.element_value end) AS FC_CALL_PROT_LN2_C
,MAX(case when te.element_type = 'FC_COUNT_LN1_C' then te.element_value end) AS FC_COUNT_LN1_C
,MAX(case when te.element_type = 'FC_COUNT_LN2_C' then te.element_value end) AS FC_COUNT_LN2_C
,MAX(case when te.element_type = 'FC_COVENANT_COMMENT' and te.transaction_phase='Final' then te.element_value end) AS FC_COVENANT_COMMENT_FNL
,MAX(case when te.element_type = 'FC_COVENANT_LN1_C' then te.element_value end) AS FC_COVENANT_LN1_C
,MAX(case when te.element_type = 'FC_COVENANT_LN2_C' then te.element_value end) AS FC_COVENANT_LN2_C
,MAX(case when te.element_type = 'FC_CURRENT_SPREAD' then te.element_value end) AS FC_CURRENT_SPREAD
,MAX(case when te.element_type = 'FC_FLOOR2_C' then te.element_value end) AS FC_FLOOR2_C
,MAX(case when te.element_type = 'FC_FNCL_COV_LN1_C' then te.element_value end) AS FC_FNCL_COV_LN1_C
,MAX(case when te.element_type = 'FC_FNCL_COV_LN2_C' then te.element_value end) AS FC_FNCL_COV_LN2_C
,MAX(case when te.element_type = 'FC_ISSUE2_C' then te.element_value end) AS FC_ISSUE2_C
,MAX(case when te.element_type = 'FC_LEV_TRSN_LN2_C' then te.element_value end) AS FC_LEV_TRSN_LN2_C
,MAX(case when te.element_type = 'FC_MARKET_ISSUANCE' then te.element_value end) AS FC_MARKET_ISSUANCE
,MAX(case when te.element_type = 'FC_OID2_C' then te.element_value end) AS FC_OID2_C
,MAX(case when te.element_type = 'FC_OTHER2_C' then te.element_value end) AS FC_OTHER2_C
,MAX(case when te.element_type = 'FC_OTHR_COMMENT' then te.element_value end) AS FC_OTHR_COMMENT
,MAX(case when te.element_type = 'FC_PRELM_DOC' then te.element_value end) AS FC_PRELM_DOC
,MAX(case when te.element_type = 'FC_SCRD_LEV' then te.element_value end) AS FC_SCRD_LEV
,MAX(case when te.element_type = 'FC_SPRD2_C' then te.element_value end) AS FC_SPRD2_C
,MAX(case when te.element_type = 'FC_STRETCH_C' then te.element_value end) AS FC_STRETCH_C
,MAX(case when te.element_type = 'FC_SUNSET' then te.element_value end) AS FC_SUNSET
,MAX(case when te.element_type = 'FC_TENOR2_C' then te.element_value end) AS FC_TENOR2_C
FROM transaction_element te
  JOIN tranche t ON t.id = te.entity_id
  JOIN entity_cross_reference ecr ON ecr.entity_id = t.transaction_id
  JOIN transaction_role tr ON t.transaction_id = tr.transaction_id WHERE ecr.identity_authority = 'Fitch Solutions' AND ecr.identity_name = 'AgentID' and tr.transaction_role in ('Issuer', 'Borrower')and te.entity_type = 'Tranche' and te.category = 'Loan public data' GROUP BY identity_value ORDER BY convert(identity_value, unsigned integer);