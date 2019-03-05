package org.sofpl.web.rest;

import org.sofpl.SofplApp;

import org.sofpl.domain.Designation;
import org.sofpl.repository.DesignationRepository;
import org.sofpl.repository.search.DesignationSearchRepository;
import org.sofpl.service.DesignationService;
import org.sofpl.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static org.sofpl.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DesignationResource REST controller.
 *
 * @see DesignationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SofplApp.class)
public class DesignationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private DesignationService designationService;

    /**
     * This repository is mocked in the org.sofpl.repository.search test package.
     *
     * @see org.sofpl.repository.search.DesignationSearchRepositoryMockConfiguration
     */
    @Autowired
    private DesignationSearchRepository mockDesignationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDesignationMockMvc;

    private Designation designation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DesignationResource designationResource = new DesignationResource(designationService);
        this.restDesignationMockMvc = MockMvcBuilders.standaloneSetup(designationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Designation createEntity(EntityManager em) {
        Designation designation = new Designation()
            .name(DEFAULT_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return designation;
    }

    @Before
    public void initTest() {
        designation = createEntity(em);
    }

    @Test
    @Transactional
    public void createDesignation() throws Exception {
        int databaseSizeBeforeCreate = designationRepository.findAll().size();

        // Create the Designation
        restDesignationMockMvc.perform(post("/api/designations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(designation)))
            .andExpect(status().isCreated());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeCreate + 1);
        Designation testDesignation = designationList.get(designationList.size() - 1);
        assertThat(testDesignation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDesignation.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testDesignation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Designation in Elasticsearch
        verify(mockDesignationSearchRepository, times(1)).save(testDesignation);
    }

    @Test
    @Transactional
    public void createDesignationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = designationRepository.findAll().size();

        // Create the Designation with an existing ID
        designation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDesignationMockMvc.perform(post("/api/designations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(designation)))
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Designation in Elasticsearch
        verify(mockDesignationSearchRepository, times(0)).save(designation);
    }

    @Test
    @Transactional
    public void getAllDesignations() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get all the designationList
        restDesignationMockMvc.perform(get("/api/designations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(designation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getDesignation() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get the designation
        restDesignationMockMvc.perform(get("/api/designations/{id}", designation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(designation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDesignation() throws Exception {
        // Get the designation
        restDesignationMockMvc.perform(get("/api/designations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDesignation() throws Exception {
        // Initialize the database
        designationService.save(designation);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDesignationSearchRepository);

        int databaseSizeBeforeUpdate = designationRepository.findAll().size();

        // Update the designation
        Designation updatedDesignation = designationRepository.findById(designation.getId()).get();
        // Disconnect from session so that the updates on updatedDesignation are not directly saved in db
        em.detach(updatedDesignation);
        updatedDesignation
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .description(UPDATED_DESCRIPTION);

        restDesignationMockMvc.perform(put("/api/designations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDesignation)))
            .andExpect(status().isOk());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
        Designation testDesignation = designationList.get(designationList.size() - 1);
        assertThat(testDesignation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDesignation.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testDesignation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Designation in Elasticsearch
        verify(mockDesignationSearchRepository, times(1)).save(testDesignation);
    }

    @Test
    @Transactional
    public void updateNonExistingDesignation() throws Exception {
        int databaseSizeBeforeUpdate = designationRepository.findAll().size();

        // Create the Designation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDesignationMockMvc.perform(put("/api/designations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(designation)))
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Designation in Elasticsearch
        verify(mockDesignationSearchRepository, times(0)).save(designation);
    }

    @Test
    @Transactional
    public void deleteDesignation() throws Exception {
        // Initialize the database
        designationService.save(designation);

        int databaseSizeBeforeDelete = designationRepository.findAll().size();

        // Delete the designation
        restDesignationMockMvc.perform(delete("/api/designations/{id}", designation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Designation in Elasticsearch
        verify(mockDesignationSearchRepository, times(1)).deleteById(designation.getId());
    }

    @Test
    @Transactional
    public void searchDesignation() throws Exception {
        // Initialize the database
        designationService.save(designation);
        when(mockDesignationSearchRepository.search(queryStringQuery("id:" + designation.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(designation), PageRequest.of(0, 1), 1));
        // Search the designation
        restDesignationMockMvc.perform(get("/api/_search/designations?query=id:" + designation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(designation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Designation.class);
        Designation designation1 = new Designation();
        designation1.setId(1L);
        Designation designation2 = new Designation();
        designation2.setId(designation1.getId());
        assertThat(designation1).isEqualTo(designation2);
        designation2.setId(2L);
        assertThat(designation1).isNotEqualTo(designation2);
        designation1.setId(null);
        assertThat(designation1).isNotEqualTo(designation2);
    }
}
