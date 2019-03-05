package org.sofpl.web.rest;

import org.sofpl.SofplApp;

import org.sofpl.domain.Holiday;
import org.sofpl.repository.HolidayRepository;
import org.sofpl.repository.search.HolidaySearchRepository;
import org.sofpl.service.HolidayService;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static org.sofpl.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.sofpl.domain.enumeration.HolidayType;
/**
 * Test class for the HolidayResource REST controller.
 *
 * @see HolidayResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SofplApp.class)
public class HolidayResourceIntTest {

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final HolidayType DEFAULT_TYPE = HolidayType.PUBLIC;
    private static final HolidayType UPDATED_TYPE = HolidayType.PRIVATE;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private HolidayService holidayService;

    /**
     * This repository is mocked in the org.sofpl.repository.search test package.
     *
     * @see org.sofpl.repository.search.HolidaySearchRepositoryMockConfiguration
     */
    @Autowired
    private HolidaySearchRepository mockHolidaySearchRepository;

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

    private MockMvc restHolidayMockMvc;

    private Holiday holiday;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HolidayResource holidayResource = new HolidayResource(holidayService);
        this.restHolidayMockMvc = MockMvcBuilders.standaloneSetup(holidayResource)
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
    public static Holiday createEntity(EntityManager em) {
        Holiday holiday = new Holiday()
            .year(DEFAULT_YEAR)
            .type(DEFAULT_TYPE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return holiday;
    }

    @Before
    public void initTest() {
        holiday = createEntity(em);
    }

    @Test
    @Transactional
    public void createHoliday() throws Exception {
        int databaseSizeBeforeCreate = holidayRepository.findAll().size();

        // Create the Holiday
        restHolidayMockMvc.perform(post("/api/holidays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(holiday)))
            .andExpect(status().isCreated());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeCreate + 1);
        Holiday testHoliday = holidayList.get(holidayList.size() - 1);
        assertThat(testHoliday.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testHoliday.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testHoliday.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testHoliday.getEndDate()).isEqualTo(DEFAULT_END_DATE);

        // Validate the Holiday in Elasticsearch
        verify(mockHolidaySearchRepository, times(1)).save(testHoliday);
    }

    @Test
    @Transactional
    public void createHolidayWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = holidayRepository.findAll().size();

        // Create the Holiday with an existing ID
        holiday.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHolidayMockMvc.perform(post("/api/holidays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(holiday)))
            .andExpect(status().isBadRequest());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeCreate);

        // Validate the Holiday in Elasticsearch
        verify(mockHolidaySearchRepository, times(0)).save(holiday);
    }

    @Test
    @Transactional
    public void getAllHolidays() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidayList
        restHolidayMockMvc.perform(get("/api/holidays?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holiday.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getHoliday() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get the holiday
        restHolidayMockMvc.perform(get("/api/holidays/{id}", holiday.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(holiday.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHoliday() throws Exception {
        // Get the holiday
        restHolidayMockMvc.perform(get("/api/holidays/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHoliday() throws Exception {
        // Initialize the database
        holidayService.save(holiday);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockHolidaySearchRepository);

        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();

        // Update the holiday
        Holiday updatedHoliday = holidayRepository.findById(holiday.getId()).get();
        // Disconnect from session so that the updates on updatedHoliday are not directly saved in db
        em.detach(updatedHoliday);
        updatedHoliday
            .year(UPDATED_YEAR)
            .type(UPDATED_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restHolidayMockMvc.perform(put("/api/holidays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHoliday)))
            .andExpect(status().isOk());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
        Holiday testHoliday = holidayList.get(holidayList.size() - 1);
        assertThat(testHoliday.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testHoliday.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testHoliday.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testHoliday.getEndDate()).isEqualTo(UPDATED_END_DATE);

        // Validate the Holiday in Elasticsearch
        verify(mockHolidaySearchRepository, times(1)).save(testHoliday);
    }

    @Test
    @Transactional
    public void updateNonExistingHoliday() throws Exception {
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();

        // Create the Holiday

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHolidayMockMvc.perform(put("/api/holidays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(holiday)))
            .andExpect(status().isBadRequest());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Holiday in Elasticsearch
        verify(mockHolidaySearchRepository, times(0)).save(holiday);
    }

    @Test
    @Transactional
    public void deleteHoliday() throws Exception {
        // Initialize the database
        holidayService.save(holiday);

        int databaseSizeBeforeDelete = holidayRepository.findAll().size();

        // Delete the holiday
        restHolidayMockMvc.perform(delete("/api/holidays/{id}", holiday.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Holiday in Elasticsearch
        verify(mockHolidaySearchRepository, times(1)).deleteById(holiday.getId());
    }

    @Test
    @Transactional
    public void searchHoliday() throws Exception {
        // Initialize the database
        holidayService.save(holiday);
        when(mockHolidaySearchRepository.search(queryStringQuery("id:" + holiday.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(holiday), PageRequest.of(0, 1), 1));
        // Search the holiday
        restHolidayMockMvc.perform(get("/api/_search/holidays?query=id:" + holiday.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holiday.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Holiday.class);
        Holiday holiday1 = new Holiday();
        holiday1.setId(1L);
        Holiday holiday2 = new Holiday();
        holiday2.setId(holiday1.getId());
        assertThat(holiday1).isEqualTo(holiday2);
        holiday2.setId(2L);
        assertThat(holiday1).isNotEqualTo(holiday2);
        holiday1.setId(null);
        assertThat(holiday1).isNotEqualTo(holiday2);
    }
}
