package org.sofpl.web.rest;
import org.sofpl.domain.Designation;
import org.sofpl.service.DesignationService;
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
 * REST controller for managing Designation.
 */
@RestController
@RequestMapping("/api")
public class DesignationResource {

    private final Logger log = LoggerFactory.getLogger(DesignationResource.class);

    private static final String ENTITY_NAME = "designation";

    private final DesignationService designationService;

    public DesignationResource(DesignationService designationService) {
        this.designationService = designationService;
    }

    /**
     * POST  /designations : Create a new designation.
     *
     * @param designation the designation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new designation, or with status 400 (Bad Request) if the designation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/designations")
    public ResponseEntity<Designation> createDesignation(@RequestBody Designation designation) throws URISyntaxException {
        log.debug("REST request to save Designation : {}", designation);
        if (designation.getId() != null) {
            throw new BadRequestAlertException("A new designation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Designation result = designationService.save(designation);
        return ResponseEntity.created(new URI("/api/designations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /designations : Updates an existing designation.
     *
     * @param designation the designation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated designation,
     * or with status 400 (Bad Request) if the designation is not valid,
     * or with status 500 (Internal Server Error) if the designation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/designations")
    public ResponseEntity<Designation> updateDesignation(@RequestBody Designation designation) throws URISyntaxException {
        log.debug("REST request to update Designation : {}", designation);
        if (designation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Designation result = designationService.save(designation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, designation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /designations : get all the designations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of designations in body
     */
    @GetMapping("/designations")
    public ResponseEntity<List<Designation>> getAllDesignations(Pageable pageable) {
        log.debug("REST request to get a page of Designations");
        Page<Designation> page = designationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/designations");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /designations/:id : get the "id" designation.
     *
     * @param id the id of the designation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the designation, or with status 404 (Not Found)
     */
    @GetMapping("/designations/{id}")
    public ResponseEntity<Designation> getDesignation(@PathVariable Long id) {
        log.debug("REST request to get Designation : {}", id);
        Optional<Designation> designation = designationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(designation);
    }

    /**
     * DELETE  /designations/:id : delete the "id" designation.
     *
     * @param id the id of the designation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/designations/{id}")
    public ResponseEntity<Void> deleteDesignation(@PathVariable Long id) {
        log.debug("REST request to delete Designation : {}", id);
        designationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/designations?query=:query : search for the designation corresponding
     * to the query.
     *
     * @param query the query of the designation search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/designations")
    public ResponseEntity<List<Designation>> searchDesignations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Designations for query {}", query);
        Page<Designation> page = designationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/designations");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
