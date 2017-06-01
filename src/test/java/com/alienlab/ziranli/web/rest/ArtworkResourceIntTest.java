package com.alienlab.ziranli.web.rest;

import com.alienlab.ziranli.ZiranliserverApp;

import com.alienlab.ziranli.domain.Artwork;
import com.alienlab.ziranli.repository.ArtworkRepository;
import com.alienlab.ziranli.service.ArtworkService;
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
 * Test class for the ArtworkResource REST controller.
 *
 * @see ArtworkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZiranliserverApp.class)
public class ArtworkResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_YEAR = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_COVER_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_COVER_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_TAGS = "BBBBBBBBBB";

    private static final String DEFAULT_QR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_QR_CODE = "BBBBBBBBBB";

    @Autowired
    private ArtworkRepository artworkRepository;

    @Autowired
    private ArtworkService artworkService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restArtworkMockMvc;

    private Artwork artwork;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ArtworkResource artworkResource = new ArtworkResource(artworkService);
        this.restArtworkMockMvc = MockMvcBuilders.standaloneSetup(artworkResource)
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
    public static Artwork createEntity(EntityManager em) {
        Artwork artwork = new Artwork()
            .name(DEFAULT_NAME)
            .year(DEFAULT_YEAR)
            .author(DEFAULT_AUTHOR)
            .amount(DEFAULT_AMOUNT)
            .price(DEFAULT_PRICE)
            .memo(DEFAULT_MEMO)
            .coverImage(DEFAULT_COVER_IMAGE)
            .status(DEFAULT_STATUS)
            .tags(DEFAULT_TAGS)
            .qrCode(DEFAULT_QR_CODE);
        return artwork;
    }

    @Before
    public void initTest() {
        artwork = createEntity(em);
    }

    @Test
    @Transactional
    public void createArtwork() throws Exception {
        int databaseSizeBeforeCreate = artworkRepository.findAll().size();

        // Create the Artwork
        restArtworkMockMvc.perform(post("/api/artworks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artwork)))
            .andExpect(status().isCreated());

        // Validate the Artwork in the database
        List<Artwork> artworkList = artworkRepository.findAll();
        assertThat(artworkList).hasSize(databaseSizeBeforeCreate + 1);
        Artwork testArtwork = artworkList.get(artworkList.size() - 1);
        assertThat(testArtwork.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testArtwork.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testArtwork.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testArtwork.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testArtwork.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testArtwork.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testArtwork.getCoverImage()).isEqualTo(DEFAULT_COVER_IMAGE);
        assertThat(testArtwork.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testArtwork.getTags()).isEqualTo(DEFAULT_TAGS);
        assertThat(testArtwork.getQrCode()).isEqualTo(DEFAULT_QR_CODE);
    }

    @Test
    @Transactional
    public void createArtworkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = artworkRepository.findAll().size();

        // Create the Artwork with an existing ID
        artwork.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtworkMockMvc.perform(post("/api/artworks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artwork)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Artwork> artworkList = artworkRepository.findAll();
        assertThat(artworkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllArtworks() throws Exception {
        // Initialize the database
        artworkRepository.saveAndFlush(artwork);

        // Get all the artworkList
        restArtworkMockMvc.perform(get("/api/artworks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artwork.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR.toString())))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].coverImage").value(hasItem(DEFAULT_COVER_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS.toString())))
            .andExpect(jsonPath("$.[*].qrCode").value(hasItem(DEFAULT_QR_CODE.toString())));
    }

    @Test
    @Transactional
    public void getArtwork() throws Exception {
        // Initialize the database
        artworkRepository.saveAndFlush(artwork);

        // Get the artwork
        restArtworkMockMvc.perform(get("/api/artworks/{id}", artwork.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(artwork.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.coverImage").value(DEFAULT_COVER_IMAGE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS.toString()))
            .andExpect(jsonPath("$.qrCode").value(DEFAULT_QR_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingArtwork() throws Exception {
        // Get the artwork
        restArtworkMockMvc.perform(get("/api/artworks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtwork() throws Exception {
        // Initialize the database
        artworkService.save(artwork);

        int databaseSizeBeforeUpdate = artworkRepository.findAll().size();

        // Update the artwork
        Artwork updatedArtwork = artworkRepository.findOne(artwork.getId());
        updatedArtwork
            .name(UPDATED_NAME)
            .year(UPDATED_YEAR)
            .author(UPDATED_AUTHOR)
            .amount(UPDATED_AMOUNT)
            .price(UPDATED_PRICE)
            .memo(UPDATED_MEMO)
            .coverImage(UPDATED_COVER_IMAGE)
            .status(UPDATED_STATUS)
            .tags(UPDATED_TAGS)
            .qrCode(UPDATED_QR_CODE);

        restArtworkMockMvc.perform(put("/api/artworks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedArtwork)))
            .andExpect(status().isOk());

        // Validate the Artwork in the database
        List<Artwork> artworkList = artworkRepository.findAll();
        assertThat(artworkList).hasSize(databaseSizeBeforeUpdate);
        Artwork testArtwork = artworkList.get(artworkList.size() - 1);
        assertThat(testArtwork.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testArtwork.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testArtwork.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testArtwork.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testArtwork.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testArtwork.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testArtwork.getCoverImage()).isEqualTo(UPDATED_COVER_IMAGE);
        assertThat(testArtwork.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testArtwork.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testArtwork.getQrCode()).isEqualTo(UPDATED_QR_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingArtwork() throws Exception {
        int databaseSizeBeforeUpdate = artworkRepository.findAll().size();

        // Create the Artwork

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restArtworkMockMvc.perform(put("/api/artworks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artwork)))
            .andExpect(status().isCreated());

        // Validate the Artwork in the database
        List<Artwork> artworkList = artworkRepository.findAll();
        assertThat(artworkList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteArtwork() throws Exception {
        // Initialize the database
        artworkService.save(artwork);

        int databaseSizeBeforeDelete = artworkRepository.findAll().size();

        // Get the artwork
        restArtworkMockMvc.perform(delete("/api/artworks/{id}", artwork.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Artwork> artworkList = artworkRepository.findAll();
        assertThat(artworkList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Artwork.class);
        Artwork artwork1 = new Artwork();
        artwork1.setId(1L);
        Artwork artwork2 = new Artwork();
        artwork2.setId(artwork1.getId());
        assertThat(artwork1).isEqualTo(artwork2);
        artwork2.setId(2L);
        assertThat(artwork1).isNotEqualTo(artwork2);
        artwork1.setId(null);
        assertThat(artwork1).isNotEqualTo(artwork2);
    }
}
