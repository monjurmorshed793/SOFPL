package org.sofpl.service;

import org.sofpl.domain.Holiday;
import org.sofpl.repository.HolidayRepository;
import org.sofpl.repository.search.HolidaySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Holiday.
 */
@Service
@Transactional
public class HolidayService {

    private final Logger log = LoggerFactory.getLogger(HolidayService.class);

    private final HolidayRepository holidayRepository;

    private final HolidaySearchRepository holidaySearchRepository;

    public HolidayService(HolidayRepository holidayRepository, HolidaySearchRepository holidaySearchRepository) {
        this.holidayRepository = holidayRepository;
        this.holidaySearchRepository = holidaySearchRepository;
    }

    /**
     * Save a holiday.
     *
     * @param holiday the entity to save
     * @return the persisted entity
     */
    public Holiday save(Holiday holiday) {
        log.debug("Request to save Holiday : {}", holiday);
        Holiday result = holidayRepository.save(holiday);
        holidaySearchRepository.save(result);
        return result;
    }

    /**
     * Get all the holidays.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Holiday> findAll(Pageable pageable) {
        log.debug("Request to get all Holidays");
        return holidayRepository.findAll(pageable);
    }


    /**
     * Get one holiday by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Holiday> findOne(Long id) {
        log.debug("Request to get Holiday : {}", id);
        return holidayRepository.findById(id);
    }

    /**
     * Delete the holiday by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Holiday : {}", id);
        holidayRepository.deleteById(id);
        holidaySearchRepository.deleteById(id);
    }

    /**
     * Search for the holiday corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Holiday> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Holidays for query {}", query);
        return holidaySearchRepository.search(queryStringQuery(query), pageable);    }
}
