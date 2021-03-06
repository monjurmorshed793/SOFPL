package org.sofpl.web.rest;
import org.sofpl.domain.Holiday;
import org.sofpl.service.HolidayService;
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
 * REST controller for managing Holiday.
 */
@RestController
@RequestMapping("/api")
public class HolidayResource {

    private final Logger log = LoggerFactory.getLogger(HolidayResource.class);

    private static final String ENTITY_NAME = "holiday";

    private final HolidayService holidayService;

    public HolidayResource(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    /**
     * POST  /holidays : Create a new holiday.
     *
     * @param holiday the holiday to create
     * @return the ResponseEntity with status 201 (Created) and with body the new holiday, or with status 400 (Bad Request) if the holiday has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/holidays")
    public ResponseEntity<Holiday> createHoliday(@RequestBody Holiday holiday) throws URISyntaxException {
        log.debug("REST request to save Holiday : {}", holiday);
        if (holiday.getId() != null) {
            throw new BadRequestAlertException("A new holiday cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Holiday result = holidayService.save(holiday);
        return ResponseEntity.created(new URI("/api/holidays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /holidays : Updates an existing holiday.
     *
     * @param holiday the holiday to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated holiday,
     * or with status 400 (Bad Request) if the holiday is not valid,
     * or with status 500 (Internal Server Error) if the holiday couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/holidays")
    public ResponseEntity<Holiday> updateHoliday(@RequestBody Holiday holiday) throws URISyntaxException {
        log.debug("REST request to update Holiday : {}", holiday);
        if (holiday.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Holiday result = holidayService.save(holiday);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, holiday.getId().toString()))
            .body(result);
    }

    /**
     * GET  /holidays : get all the holidays.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of holidays in body
     */
    @GetMapping("/holidays")
    public ResponseEntity<List<Holiday>> getAllHolidays(Pageable pageable) {
        log.debug("REST request to get a page of Holidays");
        Page<Holiday> page = holidayService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/holidays");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /holidays/:id : get the "id" holiday.
     *
     * @param id the id of the holiday to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the holiday, or with status 404 (Not Found)
     */
    @GetMapping("/holidays/{id}")
    public ResponseEntity<Holiday> getHoliday(@PathVariable Long id) {
        log.debug("REST request to get Holiday : {}", id);
        Optional<Holiday> holiday = holidayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(holiday);
    }

    /**
     * DELETE  /holidays/:id : delete the "id" holiday.
     *
     * @param id the id of the holiday to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/holidays/{id}")
    public ResponseEntity<Void> deleteHoliday(@PathVariable Long id) {
        log.debug("REST request to delete Holiday : {}", id);
        holidayService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/holidays?query=:query : search for the holiday corresponding
     * to the query.
     *
     * @param query the query of the holiday search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/holidays")
    public ResponseEntity<List<Holiday>> searchHolidays(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Holidays for query {}", query);
        Page<Holiday> page = holidayService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/holidays");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
