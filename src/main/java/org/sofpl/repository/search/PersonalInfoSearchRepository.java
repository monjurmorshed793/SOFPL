package org.sofpl.repository.search;

import org.sofpl.domain.PersonalInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PersonalInfo entity.
 */
public interface PersonalInfoSearchRepository extends ElasticsearchRepository<PersonalInfo, Long> {
}
