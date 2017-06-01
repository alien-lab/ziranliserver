package com.alienlab.ziranli.web.rest;

import com.alienlab.ziranli.ZiranliserverApp;

import com.alienlab.ziranli.domain.ShareLog;
import com.alienlab.ziranli.repository.ShareLogRepository;
import com.alienlab.ziranli.service.ShareLogService;
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
 * Test class for the ShareLogResource REST controller.
 *
 * @see ShareLogResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZiranliserverApp.class)
public class ShareLogResourceIntTest {

    private static final String DEFAULT_SHARE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SHARE_TYPE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_SHARE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SHARE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_SHARE_CONTENT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_SHARE_CONTENT_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_SHARE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_SHARE_LINK = "BBBBBBBBBB";

    @Autowired
    private ShareLogRepository shareLogRepository;

    @Autowired
    private ShareLogService shareLogService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restShareLogMockMvc;

    private ShareLog shareLog;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ShareLogResource shareLogResource = new ShareLogResource(shareLogService);
        this.restShareLogMockMvc = MockMvcBuilders.standaloneSetup(shareLogResource)
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
    public static ShareLog createEntity(EntityManager em) {
        ShareLog shareLog = new ShareLog()
            .shareType(DEFAULT_SHARE_TYPE)
            .shareTime(DEFAULT_SHARE_TIME)
            .shareContentKey(DEFAULT_SHARE_CONTENT_KEY)
            .shareLink(DEFAULT_SHARE_LINK);
        return shareLog;
    }

    @Before
    public void initTest() {
        shareLog = createEntity(em);
    }

    @Test
    @Transactional
    public void createShareLog() throws Exception {
        int databaseSizeBeforeCreate = shareLogRepository.findAll().size();

        // Create the ShareLog
        restShareLogMockMvc.perform(post("/api/share-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shareLog)))
            .andExpect(status().isCreated());

        // Validate the ShareLog in the database
        List<ShareLog> shareLogList = shareLogRepository.findAll();
        assertThat(shareLogList).hasSize(databaseSizeBeforeCreate + 1);
        ShareLog testShareLog = shareLogList.get(shareLogList.size() - 1);
        assertThat(testShareLog.getShareType()).isEqualTo(DEFAULT_SHARE_TYPE);
        assertThat(testShareLog.getShareTime()).isEqualTo(DEFAULT_SHARE_TIME);
        assertThat(testShareLog.getShareContentKey()).isEqualTo(DEFAULT_SHARE_CONTENT_KEY);
        assertThat(testShareLog.getShareLink()).isEqualTo(DEFAULT_SHARE_LINK);
    }

    @Test
    @Transactional
    public void createShareLogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shareLogRepository.findAll().size();

        // Create the ShareLog with an existing ID
        shareLog.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShareLogMockMvc.perform(post("/api/share-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shareLog)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ShareLog> shareLogList = shareLogRepository.findAll();
        assertThat(shareLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllShareLogs() throws Exception {
        // Initialize the database
        shareLogRepository.saveAndFlush(shareLog);

        // Get all the shareLogList
        restShareLogMockMvc.perform(get("/api/share-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shareLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].shareType").value(hasItem(DEFAULT_SHARE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].shareTime").value(hasItem(sameInstant(DEFAULT_SHARE_TIME))))
            .andExpect(jsonPath("$.[*].shareContentKey").value(hasItem(DEFAULT_SHARE_CONTENT_KEY.toString())))
            .andExpect(jsonPath("$.[*].shareLink").value(hasItem(DEFAULT_SHARE_LINK.toString())));
    }

    @Test
    @Transactional
    public void getShareLog() throws Exception {
        // Initialize the database
        shareLogRepository.saveAndFlush(shareLog);

        // Get the shareLog
        restShareLogMockMvc.perform(get("/api/share-logs/{id}", shareLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shareLog.getId().intValue()))
            .andExpect(jsonPath("$.shareType").value(DEFAULT_SHARE_TYPE.toString()))
            .andExpect(jsonPath("$.shareTime").value(sameInstant(DEFAULT_SHARE_TIME)))
            .andExpect(jsonPath("$.shareContentKey").value(DEFAULT_SHARE_CONTENT_KEY.toString()))
            .andExpect(jsonPath("$.shareLink").value(DEFAULT_SHARE_LINK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingShareLog() throws Exception {
        // Get the shareLog
        restShareLogMockMvc.perform(get("/api/share-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShareLog() throws Exception {
        // Initialize the database
        shareLogService.save(shareLog);

        int databaseSizeBeforeUpdate = shareLogRepository.findAll().size();

        // Update the shareLog
        ShareLog updatedShareLog = shareLogRepository.findOne(shareLog.getId());
        updatedShareLog
            .shareType(UPDATED_SHARE_TYPE)
            .shareTime(UPDATED_SHARE_TIME)
            .shareContentKey(UPDATED_SHARE_CONTENT_KEY)
            .shareLink(UPDATED_SHARE_LINK);

        restShareLogMockMvc.perform(put("/api/share-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShareLog)))
            .andExpect(status().isOk());

        // Validate the ShareLog in the database
        List<ShareLog> shareLogList = shareLogRepository.findAll();
        assertThat(shareLogList).hasSize(databaseSizeBeforeUpdate);
        ShareLog testShareLog = shareLogList.get(shareLogList.size() - 1);
        assertThat(testShareLog.getShareType()).isEqualTo(UPDATED_SHARE_TYPE);
        assertThat(testShareLog.getShareTime()).isEqualTo(UPDATED_SHARE_TIME);
        assertThat(testShareLog.getShareContentKey()).isEqualTo(UPDATED_SHARE_CONTENT_KEY);
        assertThat(testShareLog.getShareLink()).isEqualTo(UPDATED_SHARE_LINK);
    }

    @Test
    @Transactional
    public void updateNonExistingShareLog() throws Exception {
        int databaseSizeBeforeUpdate = shareLogRepository.findAll().size();

        // Create the ShareLog

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restShareLogMockMvc.perform(put("/api/share-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shareLog)))
            .andExpect(status().isCreated());

        // Validate the ShareLog in the database
        List<ShareLog> shareLogList = shareLogRepository.findAll();
        assertThat(shareLogList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteShareLog() throws Exception {
        // Initialize the database
        shareLogService.save(shareLog);

        int databaseSizeBeforeDelete = shareLogRepository.findAll().size();

        // Get the shareLog
        restShareLogMockMvc.perform(delete("/api/share-logs/{id}", shareLog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ShareLog> shareLogList = shareLogRepository.findAll();
        assertThat(shareLogList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShareLog.class);
        ShareLog shareLog1 = new ShareLog();
        shareLog1.setId(1L);
        ShareLog shareLog2 = new ShareLog();
        shareLog2.setId(shareLog1.getId());
        assertThat(shareLog1).isEqualTo(shareLog2);
        shareLog2.setId(2L);
        assertThat(shareLog1).isNotEqualTo(shareLog2);
        shareLog1.setId(null);
        assertThat(shareLog1).isNotEqualTo(shareLog2);
    }
}
