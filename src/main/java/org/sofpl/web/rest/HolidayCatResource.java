package org.sofpl.web.rest;
import org.sofpl.domain.HolidayCat;
import org.sofpl.service.HolidayCatService;
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
 * REST controller for managing HolidayCat.
 */
@RestController
@RequestMapping("/api")
public class HolidayCatResource {

    private final Logger log = LoggerFactory.getLogger(HolidayCatResource.class);

    private static final String ENTITY_NAME = "holidayCat";

    private final HolidayCatService holidayCatService;

    public HolidayCatResource(HolidayCatService holidayCatService) {
        this.holidayCatService = holidayCatService;
    }

    /**
     * POST  /holiday-cats : Create a new holidayCat.
     *
     * @param holidayCat the holidayCat to create
     * @return the ResponseEntity with status 201 (Created) and with body the new holidayCat, or with status 400 (Bad Request) if the holidayCat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/holiday-cats")
    public ResponseEntity<HolidayCat> createHolidayCat(@RequestBody HolidayCat holidayCat) throws URISyntaxException {
        log.debug("REST request to save HolidayCat : {}", holidayCat);
        if (holidayCat.getId() != null) {
            throw new BadRequestAlertException("A new holidayCat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HolidayCat result = holidayCatService.save(holidayCat);
        return ResponseEntity.created(new URI("/api/holiday-cats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /holiday-cats : Updates an existing holidayCat.
     *
     * @param holidayCat the holidayCat to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated holidayCat,
     * or with status 400 (Bad Request) if the holidayCat is not valid,
     * or with status 500 (Internal Server Error) if the holidayCat couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/holiday-cats")
    public ResponseEntity<HolidayCat> updateHolidayCat(@RequestBody HolidayCat holidayCat) throws URISyntaxException {
        log.debug("REST request to update HolidayCat : {}", holidayCat);
        if (holidayCat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HolidayCat result = holidayCatService.save(holidayCat);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, holidayCat.getId().toString()))
            .body(result);
    }

    /**
     * GET  /holiday-cats : get all the holidayCats.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of holidayCats in body
     */
    @GetMapping("/holiday-cats")
    public ResponseEntity<List<HolidayCat>> getAllHolidayCats(Pageable pageable) {
        log.debug("REST request to get a page of HolidayCats");
        Page<HolidayCat> page = holidayCatService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/holiday-cats");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /holiday-cats/:id : get the "id" holidayCat.
     *
     * @param id the id of the holidayCat to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the holidayCat, or with status 404 (Not Found)
     */
    @GetMapping("/holiday-cats/{id}")
    public ResponseEntity<HolidayCat> getHolidayCat(@PathVariable Long id) {
        log.debug("REST request to get HolidayCat : {}", id);
        Optional<HolidayCat> holidayCat = holidayCatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(holidayCat);
    }

    /**
     * DELETE  /holiday-cats/:id : delete the "id" holidayCat.
     *
     * @param id the id of the holidayCat to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/holiday-cats/{id}")
    public ResponseEntity<Void> deleteHolidayCat(@PathVariable Long id) {
        log.debug("REST request to delete HolidayCat : {}", id);
        holidayCatService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/holiday-cats?query=:query : search for the holidayCat corresponding
     * to the query.
     *
     * @param query the query of the holidayCat search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/holiday-cats")
    public ResponseEntity<List<HolidayCat>> searchHolidayCats(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of HolidayCats for query {}", query);
        Page<HolidayCat> page = holidayCatService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/holiday-cats");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
