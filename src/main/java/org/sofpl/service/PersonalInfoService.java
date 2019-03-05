package org.sofpl.service;

import org.sofpl.domain.PersonalInfo;
import org.sofpl.repository.PersonalInfoRepository;
import org.sofpl.repository.search.PersonalInfoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing PersonalInfo.
 */
@Service
@Transactional
public class PersonalInfoService {

    private final Logger log = LoggerFactory.getLogger(PersonalInfoService.class);

    private final PersonalInfoRepository personalInfoRepository;

    private final PersonalInfoSearchRepository personalInfoSearchRepository;

    public PersonalInfoService(PersonalInfoRepository personalInfoRepository, PersonalInfoSearchRepository personalInfoSearchRepository) {
        this.personalInfoRepository = personalInfoRepository;
        this.personalInfoSearchRepository = personalInfoSearchRepository;
    }

    /**
     * Save a personalInfo.
     *
     * @param personalInfo the entity to save
     * @return the persisted entity
     */
    public PersonalInfo save(PersonalInfo personalInfo) {
        log.debug("Request to save PersonalInfo : {}", personalInfo);
        PersonalInfo result = personalInfoRepository.save(personalInfo);
        personalInfoSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the personalInfos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PersonalInfo> findAll(Pageable pageable) {
        log.debug("Request to get all PersonalInfos");
        return personalInfoRepository.findAll(pageable);
    }


    /**
     * Get one personalInfo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PersonalInfo> findOne(Long id) {
        log.debug("Request to get PersonalInfo : {}", id);
        return personalInfoRepository.findById(id);
    }

    /**
     * Delete the personalInfo by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PersonalInfo : {}", id);
        personalInfoRepository.deleteById(id);
        personalInfoSearchRepository.deleteById(id);
    }

    /**
     * Search for the personalInfo corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PersonalInfo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PersonalInfos for query {}", query);
        return personalInfoSearchRepository.search(queryStringQuery(query), pageable);    }
}
