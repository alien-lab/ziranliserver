package com.alienlab.ziranli.web.rest;

import com.alienlab.ziranli.ZiranliserverApp;

import com.alienlab.ziranli.domain.Exhibition;
import com.alienlab.ziranli.repository.ExhibitionRepository;
import com.alienlab.ziranli.service.ExhibitionService;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ExhibitionResource REST controller.
 *
 * @see ExhibitionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZiranliserverApp.class)
public class ExhibitionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_START_DATE = "AAAAAAAAAA";
    private static final String UPDATED_START_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_END_DATE = "AAAAAAAAAA";
    private static final String UPDATED_END_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_TIME_DESC = "AAAAAAAAAA";
    private static final String UPDATED_TIME_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_COVER_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_COVER_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_QR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_QR_CODE = "BBBBBBBBBB";

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Autowired
    private ExhibitionService exhibitionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExhibitionMockMvc;

    private Exhibition exhibition;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExhibitionResource exhibitionResource = new ExhibitionResource(exhibitionService);
        this.restExhibitionMockMvc = MockMvcBuilders.standaloneSetup(exhibitionResource)
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
    public static Exhibition createEntity(EntityManager em) {
        Exhibition exhibition = new Exhibition()
            .name(DEFAULT_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .timeDesc(DEFAULT_TIME_DESC)
            .memo(DEFAULT_MEMO)
            .coverImage(DEFAULT_COVER_IMAGE)
            .qrCode(DEFAULT_QR_CODE);
        return exhibition;
    }

    @Before
    public void initTest() {
        exhibition = createEntity(em);
    }

    @Test
    @Transactional
    public void createExhibition() throws Exception {
        int databaseSizeBeforeCreate = exhibitionRepository.findAll().size();

        // Create the Exhibition
        restExhibitionMockMvc.perform(post("/api/exhibitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exhibition)))
            .andExpect(status().isCreated());

        // Validate the Exhibition in the database
        List<Exhibition> exhibitionList = exhibitionRepository.findAll();
        assertThat(exhibitionList).hasSize(databaseSizeBeforeCreate + 1);
        Exhibition testExhibition = exhibitionList.get(exhibitionList.size() - 1);
        assertThat(testExhibition.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExhibition.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testExhibition.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testExhibition.getTimeDesc()).isEqualTo(DEFAULT_TIME_DESC);
        assertThat(testExhibition.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testExhibition.getCoverImage()).isEqualTo(DEFAULT_COVER_IMAGE);
        assertThat(testExhibition.getQrCode()).isEqualTo(DEFAULT_QR_CODE);
    }

    @Test
    @Transactional
    public void createExhibitionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = exhibitionRepository.findAll().size();

        // Create the Exhibition with an existing ID
        exhibition.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExhibitionMockMvc.perform(post("/api/exhibitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exhibition)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Exhibition> exhibitionList = exhibitionRepository.findAll();
        assertThat(exhibitionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllExhibitions() throws Exception {
        // Initialize the database
        exhibitionRepository.saveAndFlush(exhibition);

        // Get all the exhibitionList
        restExhibitionMockMvc.perform(get("/api/exhibitions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exhibition.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].timeDesc").value(hasItem(DEFAULT_TIME_DESC.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].coverImage").value(hasItem(DEFAULT_COVER_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].qrCode").value(hasItem(DEFAULT_QR_CODE.toString())));
    }

    @Test
    @Transactional
    public void getExhibition() throws Exception {
        // Initialize the database
        exhibitionRepository.saveAndFlush(exhibition);

        // Get the exhibition
        restExhibitionMockMvc.perform(get("/api/exhibitions/{id}", exhibition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(exhibition.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.timeDesc").value(DEFAULT_TIME_DESC.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.coverImage").value(DEFAULT_COVER_IMAGE.toString()))
            .andExpect(jsonPath("$.qrCode").value(DEFAULT_QR_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExhibition() throws Exception {
        // Get the exhibition
        restExhibitionMockMvc.perform(get("/api/exhibitions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExhibition() throws Exception {
        // Initialize the database
        exhibitionService.save(exhibition);

        int databaseSizeBeforeUpdate = exhibitionRepository.findAll().size();

        // Update the exhibition
        Exhibition updatedExhibition = exhibitionRepository.findOne(exhibition.getId());
        updatedExhibition
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .timeDesc(UPDATED_TIME_DESC)
            .memo(UPDATED_MEMO)
            .coverImage(UPDATED_COVER_IMAGE)
            .qrCode(UPDATED_QR_CODE);

        restExhibitionMockMvc.perform(put("/api/exhibitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExhibition)))
            .andExpect(status().isOk());

        // Validate the Exhibition in the database
        List<Exhibition> exhibitionList = exhibitionRepository.findAll();
        assertThat(exhibitionList).hasSize(databaseSizeBeforeUpdate);
        Exhibition testExhibition = exhibitionList.get(exhibitionList.size() - 1);
        assertThat(testExhibition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExhibition.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testExhibition.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testExhibition.getTimeDesc()).isEqualTo(UPDATED_TIME_DESC);
        assertThat(testExhibition.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testExhibition.getCoverImage()).isEqualTo(UPDATED_COVER_IMAGE);
        assertThat(testExhibition.getQrCode()).isEqualTo(UPDATED_QR_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingExhibition() throws Exception {
        int databaseSizeBeforeUpdate = exhibitionRepository.findAll().size();

        // Create the Exhibition

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restExhibitionMockMvc.perform(put("/api/exhibitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exhibition)))
            .andExpect(status().isCreated());

        // Validate the Exhibition in the database
        List<Exhibition> exhibitionList = exhibitionRepository.findAll();
        assertThat(exhibitionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteExhibition() throws Exception {
        // Initialize the database
        exhibitionService.save(exhibition);

        int databaseSizeBeforeDelete = exhibitionRepository.findAll().size();

        // Get the exhibition
        restExhibitionMockMvc.perform(delete("/api/exhibitions/{id}", exhibition.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Exhibition> exhibitionList = exhibitionRepository.findAll();
        assertThat(exhibitionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Exhibition.class);
        Exhibition exhibition1 = new Exhibition();
        exhibition1.setId(1L);
        Exhibition exhibition2 = new Exhibition();
        exhibition2.setId(exhibition1.getId());
        assertThat(exhibition1).isEqualTo(exhibition2);
        exhibition2.setId(2L);
        assertThat(exhibition1).isNotEqualTo(exhibition2);
        exhibition1.setId(null);
        assertThat(exhibition1).isNotEqualTo(exhibition2);
    }
}
