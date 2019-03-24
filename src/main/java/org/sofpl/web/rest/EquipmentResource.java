package org.sofpl.web.rest;
import org.sofpl.domain.Equipment;
import org.sofpl.repository.EquipmentRepository;
import org.sofpl.repository.search.EquipmentSearchRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Equipment.
 */
@RestController
@RequestMapping("/api")
public class EquipmentResource {

    private final Logger log = LoggerFactory.getLogger(EquipmentResource.class);

    private static final String ENTITY_NAME = "equipment";

    private final EquipmentRepository equipmentRepository;

    private final EquipmentSearchRepository equipmentSearchRepository;

    public EquipmentResource(EquipmentRepository equipmentRepository, EquipmentSearchRepository equipmentSearchRepository) {
        this.equipmentRepository = equipmentRepository;
        this.equipmentSearchRepository = equipmentSearchRepository;
    }

    /**
     * POST  /equipment : Create a new equipment.
     *
     * @param equipment the equipment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new equipment, or with status 400 (Bad Request) if the equipment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/equipment")
    public ResponseEntity<Equipment> createEquipment(@RequestBody Equipment equipment) throws URISyntaxException {
        log.debug("REST request to save Equipment : {}", equipment);
        if (equipment.getId() != null) {
            throw new BadRequestAlertException("A new equipment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Equipment result = equipmentRepository.save(equipment);
        equipmentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/equipment/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /equipment : Updates an existing equipment.
     *
     * @param equipment the equipment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated equipment,
     * or with status 400 (Bad Request) if the equipment is not valid,
     * or with status 500 (Internal Server Error) if the equipment couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/equipment")
    public ResponseEntity<Equipment> updateEquipment(@RequestBody Equipment equipment) throws URISyntaxException {
        log.debug("REST request to update Equipment : {}", equipment);
        if (equipment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Equipment result = equipmentRepository.save(equipment);
        equipmentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, equipment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /equipment : get all the equipment.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of equipment in body
     */
    @GetMapping("/equipment")
    public ResponseEntity<List<Equipment>> getAllEquipment(Pageable pageable) {
        log.debug("REST request to get a page of Equipment");
        Page<Equipment> page = equipmentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/equipment");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /equipment/:id : get the "id" equipment.
     *
     * @param id the id of the equipment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the equipment, or with status 404 (Not Found)
     */
    @GetMapping("/equipment/{id}")
    public ResponseEntity<Equipment> getEquipment(@PathVariable Long id) {
        log.debug("REST request to get Equipment : {}", id);
        Optional<Equipment> equipment = equipmentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(equipment);
    }

    /**
     * DELETE  /equipment/:id : delete the "id" equipment.
     *
     * @param id the id of the equipment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/equipment/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        log.debug("REST request to delete Equipment : {}", id);
        equipmentRepository.deleteById(id);
        equipmentSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/equipment?query=:query : search for the equipment corresponding
     * to the query.
     *
     * @param query the query of the equipment search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/equipment")
    public ResponseEntity<List<Equipment>> searchEquipment(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Equipment for query {}", query);
        Page<Equipment> page = equipmentSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/equipment");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
