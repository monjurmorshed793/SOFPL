package org.sofpl.repository.search;

import org.sofpl.domain.Designation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Designation entity.
 */
public interface DesignationSearchRepository extends ElasticsearchRepository<Designation, Long> {
}
