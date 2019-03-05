package org.sofpl.web.rest;

import org.sofpl.SofplApp;

import org.sofpl.domain.HolidayCat;
import org.sofpl.repository.HolidayCatRepository;
import org.sofpl.repository.search.HolidayCatSearchRepository;
import org.sofpl.service.HolidayCatService;
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
 * Test class for the HolidayCatResource REST controller.
 *
 * @see HolidayCatResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SofplApp.class)
public class HolidayCatResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private HolidayCatRepository holidayCatRepository;

    @Autowired
    private HolidayCatService holidayCatService;

    /**
     * This repository is mocked in the org.sofpl.repository.search test package.
     *
     * @see org.sofpl.repository.search.HolidayCatSearchRepositoryMockConfiguration
     */
    @Autowired
    private HolidayCatSearchRepository mockHolidayCatSearchRepository;

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

    private MockMvc restHolidayCatMockMvc;

    private HolidayCat holidayCat;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HolidayCatResource holidayCatResource = new HolidayCatResource(holidayCatService);
        this.restHolidayCatMockMvc = MockMvcBuilders.standaloneSetup(holidayCatResource)
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
    public static HolidayCat createEntity(EntityManager em) {
        HolidayCat holidayCat = new HolidayCat()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return holidayCat;
    }

    @Before
    public void initTest() {
        holidayCat = createEntity(em);
    }

    @Test
    @Transactional
    public void createHolidayCat() throws Exception {
        int databaseSizeBeforeCreate = holidayCatRepository.findAll().size();

        // Create the HolidayCat
        restHolidayCatMockMvc.perform(post("/api/holiday-cats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(holidayCat)))
            .andExpect(status().isCreated());

        // Validate the HolidayCat in the database
        List<HolidayCat> holidayCatList = holidayCatRepository.findAll();
        assertThat(holidayCatList).hasSize(databaseSizeBeforeCreate + 1);
        HolidayCat testHolidayCat = holidayCatList.get(holidayCatList.size() - 1);
        assertThat(testHolidayCat.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHolidayCat.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the HolidayCat in Elasticsearch
        verify(mockHolidayCatSearchRepository, times(1)).save(testHolidayCat);
    }

    @Test
    @Transactional
    public void createHolidayCatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = holidayCatRepository.findAll().size();

        // Create the HolidayCat with an existing ID
        holidayCat.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHolidayCatMockMvc.perform(post("/api/holiday-cats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(holidayCat)))
            .andExpect(status().isBadRequest());

        // Validate the HolidayCat in the database
        List<HolidayCat> holidayCatList = holidayCatRepository.findAll();
        assertThat(holidayCatList).hasSize(databaseSizeBeforeCreate);

        // Validate the HolidayCat in Elasticsearch
        verify(mockHolidayCatSearchRepository, times(0)).save(holidayCat);
    }

    @Test
    @Transactional
    public void getAllHolidayCats() throws Exception {
        // Initialize the database
        holidayCatRepository.saveAndFlush(holidayCat);

        // Get all the holidayCatList
        restHolidayCatMockMvc.perform(get("/api/holiday-cats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holidayCat.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getHolidayCat() throws Exception {
        // Initialize the database
        holidayCatRepository.saveAndFlush(holidayCat);

        // Get the holidayCat
        restHolidayCatMockMvc.perform(get("/api/holiday-cats/{id}", holidayCat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(holidayCat.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHolidayCat() throws Exception {
        // Get the holidayCat
        restHolidayCatMockMvc.perform(get("/api/holiday-cats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHolidayCat() throws Exception {
        // Initialize the database
        holidayCatService.save(holidayCat);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockHolidayCatSearchRepository);

        int databaseSizeBeforeUpdate = holidayCatRepository.findAll().size();

        // Update the holidayCat
        HolidayCat updatedHolidayCat = holidayCatRepository.findById(holidayCat.getId()).get();
        // Disconnect from session so that the updates on updatedHolidayCat are not directly saved in db
        em.detach(updatedHolidayCat);
        updatedHolidayCat
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restHolidayCatMockMvc.perform(put("/api/holiday-cats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHolidayCat)))
            .andExpect(status().isOk());

        // Validate the HolidayCat in the database
        List<HolidayCat> holidayCatList = holidayCatRepository.findAll();
        assertThat(holidayCatList).hasSize(databaseSizeBeforeUpdate);
        HolidayCat testHolidayCat = holidayCatList.get(holidayCatList.size() - 1);
        assertThat(testHolidayCat.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHolidayCat.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the HolidayCat in Elasticsearch
        verify(mockHolidayCatSearchRepository, times(1)).save(testHolidayCat);
    }

    @Test
    @Transactional
    public void updateNonExistingHolidayCat() throws Exception {
        int databaseSizeBeforeUpdate = holidayCatRepository.findAll().size();

        // Create the HolidayCat

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHolidayCatMockMvc.perform(put("/api/holiday-cats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(holidayCat)))
            .andExpect(status().isBadRequest());

        // Validate the HolidayCat in the database
        List<HolidayCat> holidayCatList = holidayCatRepository.findAll();
        assertThat(holidayCatList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HolidayCat in Elasticsearch
        verify(mockHolidayCatSearchRepository, times(0)).save(holidayCat);
    }

    @Test
    @Transactional
    public void deleteHolidayCat() throws Exception {
        // Initialize the database
        holidayCatService.save(holidayCat);

        int databaseSizeBeforeDelete = holidayCatRepository.findAll().size();

        // Delete the holidayCat
        restHolidayCatMockMvc.perform(delete("/api/holiday-cats/{id}", holidayCat.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HolidayCat> holidayCatList = holidayCatRepository.findAll();
        assertThat(holidayCatList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HolidayCat in Elasticsearch
        verify(mockHolidayCatSearchRepository, times(1)).deleteById(holidayCat.getId());
    }

    @Test
    @Transactional
    public void searchHolidayCat() throws Exception {
        // Initialize the database
        holidayCatService.save(holidayCat);
        when(mockHolidayCatSearchRepository.search(queryStringQuery("id:" + holidayCat.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(holidayCat), PageRequest.of(0, 1), 1));
        // Search the holidayCat
        restHolidayCatMockMvc.perform(get("/api/_search/holiday-cats?query=id:" + holidayCat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holidayCat.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HolidayCat.class);
        HolidayCat holidayCat1 = new HolidayCat();
        holidayCat1.setId(1L);
        HolidayCat holidayCat2 = new HolidayCat();
        holidayCat2.setId(holidayCat1.getId());
        assertThat(holidayCat1).isEqualTo(holidayCat2);
        holidayCat2.setId(2L);
        assertThat(holidayCat1).isNotEqualTo(holidayCat2);
        holidayCat1.setId(null);
        assertThat(holidayCat1).isNotEqualTo(holidayCat2);
    }
}
