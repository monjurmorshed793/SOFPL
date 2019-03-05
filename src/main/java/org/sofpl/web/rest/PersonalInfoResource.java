package org.sofpl.web.rest;
import org.sofpl.domain.PersonalInfo;
import org.sofpl.service.PersonalInfoService;
import org.sofpl.web.rest.errors.BadRequestAlertException;
import org.sofpl.web.rest.util.HeaderUtil;
import org.sofpl.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing PersonalInfo.
 */
@RestController
@RequestMapping("/api")
public class PersonalInfoResource {

    private final Logger log = LoggerFactory.getLogger(PersonalInfoResource.class);

    private static final String ENTITY_NAME = "personalInfo";

    private final PersonalInfoService personalInfoService;

    public PersonalInfoResource(PersonalInfoService personalInfoService) {
        this.personalInfoService = personalInfoService;
    }

    /**
     * POST  /personal-infos : Create a new personalInfo.
     *
     * @param personalInfo the personalInfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new personalInfo, or with status 400 (Bad Request) if the personalInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/personal-infos")
    public ResponseEntity<PersonalInfo> createPersonalInfo(@RequestBody PersonalInfo personalInfo) throws URISyntaxException {
        log.debug("REST request to save PersonalInfo : {}", personalInfo);
        if (personalInfo.getId() != null) {
            throw new BadRequestAlertException("A new personalInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonalInfo result = personalInfoService.save(personalInfo);
        return ResponseEntity.created(new URI("/api/personal-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /personal-infos : Updates an existing personalInfo.
     *
     * @param personalInfo the personalInfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated personalInfo,
     * or with status 400 (Bad Request) if the personalInfo is not valid,
     * or with status 500 (Internal Server Error) if the personalInfo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/personal-infos")
    public ResponseEntity<PersonalInfo> updatePersonalInfo(@RequestBody PersonalInfo personalInfo) throws URISyntaxException {
        log.debug("REST request to update PersonalInfo : {}", personalInfo);
        if (personalInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PersonalInfo result = personalInfoService.save(personalInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, personalInfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /personal-infos : get all the personalInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of personalInfos in body
     */
    @GetMapping("/personal-infos")
    public ResponseEntity<List<PersonalInfo>> getAllPersonalInfos(Pageable pageable) {
        log.debug("REST request to get a page of PersonalInfos");
        Page<PersonalInfo> page = personalInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/personal-infos");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /personal-infos/:id : get the "id" personalInfo.
     *
     * @param id the id of the personalInfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the personalInfo, or with status 404 (Not Found)
     */
    @GetMapping("/personal-infos/{id}")
    public ResponseEntity<PersonalInfo> getPersonalInfo(@PathVariable Long id) {
        log.debug("REST request to get PersonalInfo : {}", id);
        Optional<PersonalInfo> personalInfo = personalInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personalInfo);
    }

    /**
     * DELETE  /personal-infos/:id : delete the "id" personalInfo.
     *
     * @param id the id of the personalInfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/personal-infos/{id}")
    public ResponseEntity<Void> deletePersonalInfo(@PathVariable Long id) {
        log.debug("REST request to delete PersonalInfo : {}", id);
        personalInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/personal-infos?query=:query : search for the personalInfo corresponding
     * to the query.
     *
     * @param query the query of the personalInfo search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/personal-infos")
    public ResponseEntity<List<PersonalInfo>> searchPersonalInfos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PersonalInfos for query {}", query);
        Page<PersonalInfo> page = personalInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/personal-infos");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
