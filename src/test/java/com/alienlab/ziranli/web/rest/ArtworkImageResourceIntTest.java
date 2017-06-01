package com.alienlab.ziranli.web.rest;

import com.alienlab.ziranli.ZiranliserverApp;

import com.alienlab.ziranli.domain.ArtworkImage;
import com.alienlab.ziranli.repository.ArtworkImageRepository;
import com.alienlab.ziranli.service.ArtworkImageService;
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
 * Test class for the ArtworkImageResource REST controller.
 *
 * @see ArtworkImageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZiranliserverApp.class)
public class ArtworkImageResourceIntTest {

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    @Autowired
    private ArtworkImageRepository artworkImageRepository;

    @Autowired
    private ArtworkImageService artworkImageService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restArtworkImageMockMvc;

    private ArtworkImage artworkImage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ArtworkImageResource artworkImageResource = new ArtworkImageResource(artworkImageService);
        this.restArtworkImageMockMvc = MockMvcBuilders.standaloneSetup(artworkImageResource)
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
    public static ArtworkImage createEntity(EntityManager em) {
        ArtworkImage artworkImage = new ArtworkImage()
            .image(DEFAULT_IMAGE);
        return artworkImage;
    }

    @Before
    public void initTest() {
        artworkImage = createEntity(em);
    }

    @Test
    @Transactional
    public void createArtworkImage() throws Exception {
        int databaseSizeBeforeCreate = artworkImageRepository.findAll().size();

        // Create the ArtworkImage
        restArtworkImageMockMvc.perform(post("/api/artwork-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artworkImage)))
            .andExpect(status().isCreated());

        // Validate the ArtworkImage in the database
        List<ArtworkImage> artworkImageList = artworkImageRepository.findAll();
        assertThat(artworkImageList).hasSize(databaseSizeBeforeCreate + 1);
        ArtworkImage testArtworkImage = artworkImageList.get(artworkImageList.size() - 1);
        assertThat(testArtworkImage.getImage()).isEqualTo(DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    public void createArtworkImageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = artworkImageRepository.findAll().size();

        // Create the ArtworkImage with an existing ID
        artworkImage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtworkImageMockMvc.perform(post("/api/artwork-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artworkImage)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ArtworkImage> artworkImageList = artworkImageRepository.findAll();
        assertThat(artworkImageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllArtworkImages() throws Exception {
        // Initialize the database
        artworkImageRepository.saveAndFlush(artworkImage);

        // Get all the artworkImageList
        restArtworkImageMockMvc.perform(get("/api/artwork-images?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artworkImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())));
    }

    @Test
    @Transactional
    public void getArtworkImage() throws Exception {
        // Initialize the database
        artworkImageRepository.saveAndFlush(artworkImage);

        // Get the artworkImage
        restArtworkImageMockMvc.perform(get("/api/artwork-images/{id}", artworkImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(artworkImage.getId().intValue()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingArtworkImage() throws Exception {
        // Get the artworkImage
        restArtworkImageMockMvc.perform(get("/api/artwork-images/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtworkImage() throws Exception {
        // Initialize the database
        artworkImageService.save(artworkImage);

        int databaseSizeBeforeUpdate = artworkImageRepository.findAll().size();

        // Update the artworkImage
        ArtworkImage updatedArtworkImage = artworkImageRepository.findOne(artworkImage.getId());
        updatedArtworkImage
            .image(UPDATED_IMAGE);

        restArtworkImageMockMvc.perform(put("/api/artwork-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedArtworkImage)))
            .andExpect(status().isOk());

        // Validate the ArtworkImage in the database
        List<ArtworkImage> artworkImageList = artworkImageRepository.findAll();
        assertThat(artworkImageList).hasSize(databaseSizeBeforeUpdate);
        ArtworkImage testArtworkImage = artworkImageList.get(artworkImageList.size() - 1);
        assertThat(testArtworkImage.getImage()).isEqualTo(UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingArtworkImage() throws Exception {
        int databaseSizeBeforeUpdate = artworkImageRepository.findAll().size();

        // Create the ArtworkImage

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restArtworkImageMockMvc.perform(put("/api/artwork-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artworkImage)))
            .andExpect(status().isCreated());

        // Validate the ArtworkImage in the database
        List<ArtworkImage> artworkImageList = artworkImageRepository.findAll();
        assertThat(artworkImageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteArtworkImage() throws Exception {
        // Initialize the database
        artworkImageService.save(artworkImage);

        int databaseSizeBeforeDelete = artworkImageRepository.findAll().size();

        // Get the artworkImage
        restArtworkImageMockMvc.perform(delete("/api/artwork-images/{id}", artworkImage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ArtworkImage> artworkImageList = artworkImageRepository.findAll();
        assertThat(artworkImageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArtworkImage.class);
        ArtworkImage artworkImage1 = new ArtworkImage();
        artworkImage1.setId(1L);
        ArtworkImage artworkImage2 = new ArtworkImage();
        artworkImage2.setId(artworkImage1.getId());
        assertThat(artworkImage1).isEqualTo(artworkImage2);
        artworkImage2.setId(2L);
        assertThat(artworkImage1).isNotEqualTo(artworkImage2);
        artworkImage1.setId(null);
        assertThat(artworkImage1).isNotEqualTo(artworkImage2);
    }
}
