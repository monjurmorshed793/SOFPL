package org.sofpl.repository.search;

import org.sofpl.domain.HolidayCat;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the HolidayCat entity.
 */
public interface HolidayCatSearchRepository extends ElasticsearchRepository<HolidayCat, Long> {
}
