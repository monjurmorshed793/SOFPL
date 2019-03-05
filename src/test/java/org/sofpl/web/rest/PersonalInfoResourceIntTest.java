package org.sofpl.web.rest;

import org.sofpl.SofplApp;

import org.sofpl.domain.PersonalInfo;
import org.sofpl.repository.PersonalInfoRepository;
import org.sofpl.repository.search.PersonalInfoSearchRepository;
import org.sofpl.service.PersonalInfoService;
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

import org.sofpl.domain.enumeration.MaritalStatus;
import org.sofpl.domain.enumeration.Gender;
import org.sofpl.domain.enumeration.Religion;
/**
 * Test class for the PersonalInfoResource REST controller.
 *
 * @see PersonalInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SofplApp.class)
public class PersonalInfoResourceIntTest {

    private static final String DEFAULT_EMPLOYEE_ID = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FATHERS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FATHERS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MOTHERS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MOTHERS_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final MaritalStatus DEFAULT_MARITAL_STATUS = MaritalStatus.MARRIED;
    private static final MaritalStatus UPDATED_MARITAL_STATUS = MaritalStatus.UNMARRIED;

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final Religion DEFAULT_RELIGION = Religion.ISLAM;
    private static final Religion UPDATED_RELIGION = Religion.HINDU;

    private static final String DEFAULT_PERMANENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PERMANENT_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PRESENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PRESENT_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private PersonalInfoRepository personalInfoRepository;

    @Autowired
    private PersonalInfoService personalInfoService;

    /**
     * This repository is mocked in the org.sofpl.repository.search test package.
     *
     * @see org.sofpl.repository.search.PersonalInfoSearchRepositoryMockConfiguration
     */
    @Autowired
    private PersonalInfoSearchRepository mockPersonalInfoSearchRepository;

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

    private MockMvc restPersonalInfoMockMvc;

    private PersonalInfo personalInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PersonalInfoResource personalInfoResource = new PersonalInfoResource(personalInfoService);
        this.restPersonalInfoMockMvc = MockMvcBuilders.standaloneSetup(personalInfoResource)
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
    public static PersonalInfo createEntity(EntityManager em) {
        PersonalInfo personalInfo = new PersonalInfo()
            .employeeId(DEFAULT_EMPLOYEE_ID)
            .fullName(DEFAULT_FULL_NAME)
            .fathersName(DEFAULT_FATHERS_NAME)
            .mothersName(DEFAULT_MOTHERS_NAME)
            .birthDate(DEFAULT_BIRTH_DATE)
            .maritalStatus(DEFAULT_MARITAL_STATUS)
            .gender(DEFAULT_GENDER)
            .religion(DEFAULT_RELIGION)
            .permanentAddress(DEFAULT_PERMANENT_ADDRESS)
            .presentAddress(DEFAULT_PRESENT_ADDRESS);
        return personalInfo;
    }

    @Before
    public void initTest() {
        personalInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createPersonalInfo() throws Exception {
        int databaseSizeBeforeCreate = personalInfoRepository.findAll().size();

        // Create the PersonalInfo
        restPersonalInfoMockMvc.perform(post("/api/personal-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personalInfo)))
            .andExpect(status().isCreated());

        // Validate the PersonalInfo in the database
        List<PersonalInfo> personalInfoList = personalInfoRepository.findAll();
        assertThat(personalInfoList).hasSize(databaseSizeBeforeCreate + 1);
        PersonalInfo testPersonalInfo = personalInfoList.get(personalInfoList.size() - 1);
        assertThat(testPersonalInfo.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testPersonalInfo.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testPersonalInfo.getFathersName()).isEqualTo(DEFAULT_FATHERS_NAME);
        assertThat(testPersonalInfo.getMothersName()).isEqualTo(DEFAULT_MOTHERS_NAME);
        assertThat(testPersonalInfo.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testPersonalInfo.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(testPersonalInfo.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPersonalInfo.getReligion()).isEqualTo(DEFAULT_RELIGION);
        assertThat(testPersonalInfo.getPermanentAddress()).isEqualTo(DEFAULT_PERMANENT_ADDRESS);
        assertThat(testPersonalInfo.getPresentAddress()).isEqualTo(DEFAULT_PRESENT_ADDRESS);

        // Validate the PersonalInfo in Elasticsearch
        verify(mockPersonalInfoSearchRepository, times(1)).save(testPersonalInfo);
    }

    @Test
    @Transactional
    public void createPersonalInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = personalInfoRepository.findAll().size();

        // Create the PersonalInfo with an existing ID
        personalInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonalInfoMockMvc.perform(post("/api/personal-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personalInfo)))
            .andExpect(status().isBadRequest());

        // Validate the PersonalInfo in the database
        List<PersonalInfo> personalInfoList = personalInfoRepository.findAll();
        assertThat(personalInfoList).hasSize(databaseSizeBeforeCreate);

        // Validate the PersonalInfo in Elasticsearch
        verify(mockPersonalInfoSearchRepository, times(0)).save(personalInfo);
    }

    @Test
    @Transactional
    public void getAllPersonalInfos() throws Exception {
        // Initialize the database
        personalInfoRepository.saveAndFlush(personalInfo);

        // Get all the personalInfoList
        restPersonalInfoMockMvc.perform(get("/api/personal-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.toString())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME.toString())))
            .andExpect(jsonPath("$.[*].fathersName").value(hasItem(DEFAULT_FATHERS_NAME.toString())))
            .andExpect(jsonPath("$.[*].mothersName").value(hasItem(DEFAULT_MOTHERS_NAME.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].religion").value(hasItem(DEFAULT_RELIGION.toString())))
            .andExpect(jsonPath("$.[*].permanentAddress").value(hasItem(DEFAULT_PERMANENT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].presentAddress").value(hasItem(DEFAULT_PRESENT_ADDRESS.toString())));
    }
    
    @Test
    @Transactional
    public void getPersonalInfo() throws Exception {
        // Initialize the database
        personalInfoRepository.saveAndFlush(personalInfo);

        // Get the personalInfo
        restPersonalInfoMockMvc.perform(get("/api/personal-infos/{id}", personalInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(personalInfo.getId().intValue()))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID.toString()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME.toString()))
            .andExpect(jsonPath("$.fathersName").value(DEFAULT_FATHERS_NAME.toString()))
            .andExpect(jsonPath("$.mothersName").value(DEFAULT_MOTHERS_NAME.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.maritalStatus").value(DEFAULT_MARITAL_STATUS.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.religion").value(DEFAULT_RELIGION.toString()))
            .andExpect(jsonPath("$.permanentAddress").value(DEFAULT_PERMANENT_ADDRESS.toString()))
            .andExpect(jsonPath("$.presentAddress").value(DEFAULT_PRESENT_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPersonalInfo() throws Exception {
        // Get the personalInfo
        restPersonalInfoMockMvc.perform(get("/api/personal-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersonalInfo() throws Exception {
        // Initialize the database
        personalInfoService.save(personalInfo);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPersonalInfoSearchRepository);

        int databaseSizeBeforeUpdate = personalInfoRepository.findAll().size();

        // Update the personalInfo
        PersonalInfo updatedPersonalInfo = personalInfoRepository.findById(personalInfo.getId()).get();
        // Disconnect from session so that the updates on updatedPersonalInfo are not directly saved in db
        em.detach(updatedPersonalInfo);
        updatedPersonalInfo
            .employeeId(UPDATED_EMPLOYEE_ID)
            .fullName(UPDATED_FULL_NAME)
            .fathersName(UPDATED_FATHERS_NAME)
            .mothersName(UPDATED_MOTHERS_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .gender(UPDATED_GENDER)
            .religion(UPDATED_RELIGION)
            .permanentAddress(UPDATED_PERMANENT_ADDRESS)
            .presentAddress(UPDATED_PRESENT_ADDRESS);

        restPersonalInfoMockMvc.perform(put("/api/personal-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPersonalInfo)))
            .andExpect(status().isOk());

        // Validate the PersonalInfo in the database
        List<PersonalInfo> personalInfoList = personalInfoRepository.findAll();
        assertThat(personalInfoList).hasSize(databaseSizeBeforeUpdate);
        PersonalInfo testPersonalInfo = personalInfoList.get(personalInfoList.size() - 1);
        assertThat(testPersonalInfo.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testPersonalInfo.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testPersonalInfo.getFathersName()).isEqualTo(UPDATED_FATHERS_NAME);
        assertThat(testPersonalInfo.getMothersName()).isEqualTo(UPDATED_MOTHERS_NAME);
        assertThat(testPersonalInfo.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testPersonalInfo.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testPersonalInfo.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPersonalInfo.getReligion()).isEqualTo(UPDATED_RELIGION);
        assertThat(testPersonalInfo.getPermanentAddress()).isEqualTo(UPDATED_PERMANENT_ADDRESS);
        assertThat(testPersonalInfo.getPresentAddress()).isEqualTo(UPDATED_PRESENT_ADDRESS);

        // Validate the PersonalInfo in Elasticsearch
        verify(mockPersonalInfoSearchRepository, times(1)).save(testPersonalInfo);
    }

    @Test
    @Transactional
    public void updateNonExistingPersonalInfo() throws Exception {
        int databaseSizeBeforeUpdate = personalInfoRepository.findAll().size();

        // Create the PersonalInfo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalInfoMockMvc.perform(put("/api/personal-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personalInfo)))
            .andExpect(status().isBadRequest());

        // Validate the PersonalInfo in the database
        List<PersonalInfo> personalInfoList = personalInfoRepository.findAll();
        assertThat(personalInfoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PersonalInfo in Elasticsearch
        verify(mockPersonalInfoSearchRepository, times(0)).save(personalInfo);
    }

    @Test
    @Transactional
    public void deletePersonalInfo() throws Exception {
        // Initialize the database
        personalInfoService.save(personalInfo);

        int databaseSizeBeforeDelete = personalInfoRepository.findAll().size();

        // Delete the personalInfo
        restPersonalInfoMockMvc.perform(delete("/api/personal-infos/{id}", personalInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PersonalInfo> personalInfoList = personalInfoRepository.findAll();
        assertThat(personalInfoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PersonalInfo in Elasticsearch
        verify(mockPersonalInfoSearchRepository, times(1)).deleteById(personalInfo.getId());
    }

    @Test
    @Transactional
    public void searchPersonalInfo() throws Exception {
        // Initialize the database
        personalInfoService.save(personalInfo);
        when(mockPersonalInfoSearchRepository.search(queryStringQuery("id:" + personalInfo.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(personalInfo), PageRequest.of(0, 1), 1));
        // Search the personalInfo
        restPersonalInfoMockMvc.perform(get("/api/_search/personal-infos?query=id:" + personalInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].fathersName").value(hasItem(DEFAULT_FATHERS_NAME)))
            .andExpect(jsonPath("$.[*].mothersName").value(hasItem(DEFAULT_MOTHERS_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].religion").value(hasItem(DEFAULT_RELIGION.toString())))
            .andExpect(jsonPath("$.[*].permanentAddress").value(hasItem(DEFAULT_PERMANENT_ADDRESS)))
            .andExpect(jsonPath("$.[*].presentAddress").value(hasItem(DEFAULT_PRESENT_ADDRESS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonalInfo.class);
        PersonalInfo personalInfo1 = new PersonalInfo();
        personalInfo1.setId(1L);
        PersonalInfo personalInfo2 = new PersonalInfo();
        personalInfo2.setId(personalInfo1.getId());
        assertThat(personalInfo1).isEqualTo(personalInfo2);
        personalInfo2.setId(2L);
        assertThat(personalInfo1).isNotEqualTo(personalInfo2);
        personalInfo1.setId(null);
        assertThat(personalInfo1).isNotEqualTo(personalInfo2);
    }
}
