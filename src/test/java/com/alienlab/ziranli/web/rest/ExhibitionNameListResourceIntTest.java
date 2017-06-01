package com.alienlab.ziranli.web.rest;

import com.alienlab.ziranli.ZiranliserverApp;

import com.alienlab.ziranli.domain.ExhibitionNameList;
import com.alienlab.ziranli.repository.ExhibitionNameListRepository;
import com.alienlab.ziranli.service.ExhibitionNameListService;
import com.alienlab.ziranli.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.alienlab.ziranli.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ExhibitionNameListResource REST controller.
 *
 * @see ExhibitionNameListResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZiranliserverApp.class)
public class ExhibitionNameListResourceIntTest {

    private static final ZonedDateTime DEFAULT_JOIN_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_JOIN_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_SIGN_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SIGN_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ExhibitionNameListRepository exhibitionNameListRepository;

    @Autowired
    private ExhibitionNameListService exhibitionNameListService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExhibitionNameListMockMvc;

    private ExhibitionNameList exhibitionNameList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExhibitionNameListResource exhibitionNameListResource = new ExhibitionNameListResource(exhibitionNameListService);
        this.restExhibitionNameListMockMvc = MockMvcBuilders.standaloneSetup(exhibitionNameListResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExhibitionNameList createEntity(EntityManager em) {
        ExhibitionNameList exhibitionNameList = new ExhibitionNameList()
            .joinTime(DEFAULT_JOIN_TIME)
            .signTime(DEFAULT_SIGN_TIME);
        return exhibitionNameList;
    }

    @Before
    public void initTest() {
        exhibitionNameList = createEntity(em);
    }

    @Test
    @Transactional
    public void createExhibitionNameList() throws Exception {
        int databaseSizeBeforeCreate = exhibitionNameListRepository.findAll().size();

        // Create the ExhibitionNameList
        restExhibitionNameListMockMvc.perform(post("/api/exhibition-name-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exhibitionNameList)))
            .andExpect(status().isCreated());

        // Validate the ExhibitionNameList in the database
        List<ExhibitionNameList> exhibitionNameListList = exhibitionNameListRepository.findAll();
        assertThat(exhibitionNameListList).hasSize(databaseSizeBeforeCreate + 1);
        ExhibitionNameList testExhibitionNameList = exhibitionNameListList.get(exhibitionNameListList.size() - 1);
        assertThat(testExhibitionNameList.getJoinTime()).isEqualTo(DEFAULT_JOIN_TIME);
        assertThat(testExhibitionNameList.getSignTime()).isEqualTo(DEFAULT_SIGN_TIME);
    }

    @Test
    @Transactional
    public void createExhibitionNameListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = exhibitionNameListRepository.findAll().size();

        // Create the ExhibitionNameList with an existing ID
        exhibitionNameList.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExhibitionNameListMockMvc.perform(post("/api/exhibition-name-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exhibitionNameList)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ExhibitionNameList> exhibitionNameListList = exhibitionNameListRepository.findAll();
        assertThat(exhibitionNameListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllExhibitionNameLists() throws Exception {
        // Initialize the database
        exhibitionNameListRepository.saveAndFlush(exhibitionNameList);

        // Get all the exhibitionNameListList
        restExhibitionNameListMockMvc.perform(get("/api/exhibition-name-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exhibitionNameList.getId().intValue())))
            .andExpect(jsonPath("$.[*].joinTime").value(hasItem(sameInstant(DEFAULT_JOIN_TIME))))
            .andExpect(jsonPath("$.[*].signTime").value(hasItem(sameInstant(DEFAULT_SIGN_TIME))));
    }

    @Test
    @Transactional
    public void getExhibitionNameList() throws Exception {
        // Initialize the database
        exhibitionNameListRepository.saveAndFlush(exhibitionNameList);

        // Get the exhibitionNameList
        restExhibitionNameListMockMvc.perform(get("/api/exhibition-name-lists/{id}", exhibitionNameList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(exhibitionNameList.getId().intValue()))
            .andExpect(jsonPath("$.joinTime").value(sameInstant(DEFAULT_JOIN_TIME)))
            .andExpect(jsonPath("$.signTime").value(sameInstant(DEFAULT_SIGN_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingExhibitionNameList() throws Exception {
        // Get the exhibitionNameList
        restExhibitionNameListMockMvc.perform(get("/api/exhibition-name-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExhibitionNameList() throws Exception {
        // Initialize the database
        exhibitionNameListService.save(exhibitionNameList);

        int databaseSizeBeforeUpdate = exhibitionNameListRepository.findAll().size();

        // Update the exhibitionNameList
        ExhibitionNameList updatedExhibitionNameList = exhibitionNameListRepository.findOne(exhibitionNameList.getId());
        updatedExhibitionNameList
            .joinTime(UPDATED_JOIN_TIME)
            .signTime(UPDATED_SIGN_TIME);

        restExhibitionNameListMockMvc.perform(put("/api/exhibition-name-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExhibitionNameList)))
            .andExpect(status().isOk());

        // Validate the ExhibitionNameList in the database
        List<ExhibitionNameList> exhibitionNameListList = exhibitionNameListRepository.findAll();
        assertThat(exhibitionNameListList).hasSize(databaseSizeBeforeUpdate);
        ExhibitionNameList testExhibitionNameList = exhibitionNameListList.get(exhibitionNameListList.size() - 1);
        assertThat(testExhibitionNameList.getJoinTime()).isEqualTo(UPDATED_JOIN_TIME);
        assertThat(testExhibitionNameList.getSignTime()).isEqualTo(UPDATED_SIGN_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingExhibitionNameList() throws Exception {
        int databaseSizeBeforeUpdate = exhibitionNameListRepository.findAll().size();

        // Create the ExhibitionNameList

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restExhibitionNameListMockMvc.perform(put("/api/exhibition-name-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exhibitionNameList)))
            .andExpect(status().isCreated());

        // Validate the ExhibitionNameList in the database
        List<ExhibitionNameList> exhibitionNameListList = exhibitionNameListRepository.findAll();
        assertThat(exhibitionNameListList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteExhibitionNameList() throws Exception {
        // Initialize the database
        exhibitionNameListService.save(exhibitionNameList);

        int databaseSizeBeforeDelete = exhibitionNameListRepository.findAll().size();

        // Get the exhibitionNameList
        restExhibitionNameListMockMvc.perform(delete("/api/exhibition-name-lists/{id}", exhibitionNameList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ExhibitionNameList> exhibitionNameListList = exhibitionNameListRepository.findAll();
        assertThat(exhibitionNameListList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExhibitionNameList.class);
        ExhibitionNameList exhibitionNameList1 = new ExhibitionNameList();
        exhibitionNameList1.setId(1L);
        ExhibitionNameList exhibitionNameList2 = new ExhibitionNameList();
        exhibitionNameList2.setId(exhibitionNameList1.getId());
        assertThat(exhibitionNameList1).isEqualTo(exhibitionNameList2);
        exhibitionNameList2.setId(2L);
        assertThat(exhibitionNameList1).isNotEqualTo(exhibitionNameList2);
        exhibitionNameList1.setId(null);
        assertThat(exhibitionNameList1).isNotEqualTo(exhibitionNameList2);
    }
}
