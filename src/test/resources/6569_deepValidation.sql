select attribute_name, resource_type, source_entity_type, source_category, source_element_type, target_table, enabled, source_field_id, source_transaction_phase, source_information_provenance from "configuration".cr_attribute_metadata
where source_information_provenance in ('levfin', 'capstr')
and not (source_information_provenance='levfin' and source_category='Loan public data')