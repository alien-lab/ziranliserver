package com.alienlab.ziranli.web.rest;

import com.alienlab.ziranli.ZiranliserverApp;

import com.alienlab.ziranli.domain.ExhibitionArtwork;
import com.alienlab.ziranli.repository.ExhibitionArtworkRepository;
import com.alienlab.ziranli.service.ExhibitionArtworkService;
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
 * Test class for the ExhibitionArtworkResource REST controller.
 *
 * @see ExhibitionArtworkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZiranliserverApp.class)
public class ExhibitionArtworkResourceIntTest {

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private ExhibitionArtworkRepository exhibitionArtworkRepository;

    @Autowired
    private ExhibitionArtworkService exhibitionArtworkService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExhibitionArtworkMockMvc;

    private ExhibitionArtwork exhibitionArtwork;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExhibitionArtworkResource exhibitionArtworkResource = new ExhibitionArtworkResource(exhibitionArtworkService);
        this.restExhibitionArtworkMockMvc = MockMvcBuilders.standaloneSetup(exhibitionArtworkResource)
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
    public static ExhibitionArtwork createEntity(EntityManager em) {
        ExhibitionArtwork exhibitionArtwork = new ExhibitionArtwork()
            .amount(DEFAULT_AMOUNT)
            .price(DEFAULT_PRICE)
            .status(DEFAULT_STATUS);
        return exhibitionArtwork;
    }

    @Before
    public void initTest() {
        exhibitionArtwork = createEntity(em);
    }

    @Test
    @Transactional
    public void createExhibitionArtwork() throws Exception {
        int databaseSizeBeforeCreate = exhibitionArtworkRepository.findAll().size();

        // Create the ExhibitionArtwork
        restExhibitionArtworkMockMvc.perform(post("/api/exhibition-artworks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exhibitionArtwork)))
            .andExpect(status().isCreated());

        // Validate the ExhibitionArtwork in the database
        List<ExhibitionArtwork> exhibitionArtworkList = exhibitionArtworkRepository.findAll();
        assertThat(exhibitionArtworkList).hasSize(databaseSizeBeforeCreate + 1);
        ExhibitionArtwork testExhibitionArtwork = exhibitionArtworkList.get(exhibitionArtworkList.size() - 1);
        assertThat(testExhibitionArtwork.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testExhibitionArtwork.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testExhibitionArtwork.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createExhibitionArtworkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = exhibitionArtworkRepository.findAll().size();

        // Create the ExhibitionArtwork with an existing ID
        exhibitionArtwork.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExhibitionArtworkMockMvc.perform(post("/api/exhibition-artworks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exhibitionArtwork)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ExhibitionArtwork> exhibitionArtworkList = exhibitionArtworkRepository.findAll();
        assertThat(exhibitionArtworkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllExhibitionArtworks() throws Exception {
        // Initialize the database
        exhibitionArtworkRepository.saveAndFlush(exhibitionArtwork);

        // Get all the exhibitionArtworkList
        restExhibitionArtworkMockMvc.perform(get("/api/exhibition-artworks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exhibitionArtwork.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getExhibitionArtwork() throws Exception {
        // Initialize the database
        exhibitionArtworkRepository.saveAndFlush(exhibitionArtwork);

        // Get the exhibitionArtwork
        restExhibitionArtworkMockMvc.perform(get("/api/exhibition-artworks/{id}", exhibitionArtwork.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(exhibitionArtwork.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExhibitionArtwork() throws Exception {
        // Get the exhibitionArtwork
        restExhibitionArtworkMockMvc.perform(get("/api/exhibition-artworks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExhibitionArtwork() throws Exception {
        // Initialize the database
        exhibitionArtworkService.save(exhibitionArtwork);

        int databaseSizeBeforeUpdate = exhibitionArtworkRepository.findAll().size();

        // Update the exhibitionArtwork
        ExhibitionArtwork updatedExhibitionArtwork = exhibitionArtworkRepository.findOne(exhibitionArtwork.getId());
        updatedExhibitionArtwork
            .amount(UPDATED_AMOUNT)
            .price(UPDATED_PRICE)
            .status(UPDATED_STATUS);

        restExhibitionArtworkMockMvc.perform(put("/api/exhibition-artworks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExhibitionArtwork)))
            .andExpect(status().isOk());

        // Validate the ExhibitionArtwork in the database
        List<ExhibitionArtwork> exhibitionArtworkList = exhibitionArtworkRepository.findAll();
        assertThat(exhibitionArtworkList).hasSize(databaseSizeBeforeUpdate);
        ExhibitionArtwork testExhibitionArtwork = exhibitionArtworkList.get(exhibitionArtworkList.size() - 1);
        assertThat(testExhibitionArtwork.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testExhibitionArtwork.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testExhibitionArtwork.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingExhibitionArtwork() throws Exception {
        int databaseSizeBeforeUpdate = exhibitionArtworkRepository.findAll().size();

        // Create the ExhibitionArtwork

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restExhibitionArtworkMockMvc.perform(put("/api/exhibition-artworks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exhibitionArtwork)))
            .andExpect(status().isCreated());

        // Validate the ExhibitionArtwork in the database
        List<ExhibitionArtwork> exhibitionArtworkList = exhibitionArtworkRepository.findAll();
        assertThat(exhibitionArtworkList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteExhibitionArtwork() throws Exception {
        // Initialize the database
        exhibitionArtworkService.save(exhibitionArtwork);

        int databaseSizeBeforeDelete = exhibitionArtworkRepository.findAll().size();

        // Get the exhibitionArtwork
        restExhibitionArtworkMockMvc.perform(delete("/api/exhibition-artworks/{id}", exhibitionArtwork.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ExhibitionArtwork> exhibitionArtworkList = exhibitionArtworkRepository.findAll();
        assertThat(exhibitionArtworkList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExhibitionArtwork.class);
        ExhibitionArtwork exhibitionArtwork1 = new ExhibitionArtwork();
        exhibitionArtwork1.setId(1L);
        ExhibitionArtwork exhibitionArtwork2 = new ExhibitionArtwork();
        exhibitionArtwork2.setId(exhibitionArtwork1.getId());
        assertThat(exhibitionArtwork1).isEqualTo(exhibitionArtwork2);
        exhibitionArtwork2.setId(2L);
        assertThat(exhibitionArtwork1).isNotEqualTo(exhibitionArtwork2);
        exhibitionArtwork1.setId(null);
        assertThat(exhibitionArtwork1).isNotEqualTo(exhibitionArtwork2);
    }
}
