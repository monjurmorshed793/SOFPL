package org.sofpl.service;

import org.sofpl.domain.HolidayCat;
import org.sofpl.repository.HolidayCatRepository;
import org.sofpl.repository.search.HolidayCatSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing HolidayCat.
 */
@Service
@Transactional
public class HolidayCatService {

    private final Logger log = LoggerFactory.getLogger(HolidayCatService.class);

    private final HolidayCatRepository holidayCatRepository;

    private final HolidayCatSearchRepository holidayCatSearchRepository;

    public HolidayCatService(HolidayCatRepository holidayCatRepository, HolidayCatSearchRepository holidayCatSearchRepository) {
        this.holidayCatRepository = holidayCatRepository;
        this.holidayCatSearchRepository = holidayCatSearchRepository;
    }

    /**
     * Save a holidayCat.
     *
     * @param holidayCat the entity to save
     * @return the persisted entity
     */
    public HolidayCat save(HolidayCat holidayCat) {
        log.debug("Request to save HolidayCat : {}", holidayCat);
        HolidayCat result = holidayCatRepository.save(holidayCat);
        holidayCatSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the holidayCats.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HolidayCat> findAll(Pageable pageable) {
        log.debug("Request to get all HolidayCats");
        return holidayCatRepository.findAll(pageable);
    }


    /**
     * Get one holidayCat by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<HolidayCat> findOne(Long id) {
        log.debug("Request to get HolidayCat : {}", id);
        return holidayCatRepository.findById(id);
    }

    /**
     * Delete the holidayCat by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete HolidayCat : {}", id);
        holidayCatRepository.deleteById(id);
        holidayCatSearchRepository.deleteById(id);
    }

    /**
     * Search for the holidayCat corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HolidayCat> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HolidayCats for query {}", query);
        return holidayCatSearchRepository.search(queryStringQuery(query), pageable);    }
}
