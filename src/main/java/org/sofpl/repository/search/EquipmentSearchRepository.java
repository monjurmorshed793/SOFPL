package org.sofpl.repository.search;

import org.sofpl.domain.Equipment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Equipment entity.
 */
public interface EquipmentSearchRepository extends ElasticsearchRepository<Equipment, Long> {
}
