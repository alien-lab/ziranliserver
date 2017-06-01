package com.alienlab.ziranli.web.rest;

import com.alienlab.ziranli.ZiranliserverApp;

import com.alienlab.ziranli.domain.WechatUser;
import com.alienlab.ziranli.repository.WechatUserRepository;
import com.alienlab.ziranli.service.WechatUserService;
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
 * Test class for the WechatUserResource REST controller.
 *
 * @see WechatUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZiranliserverApp.class)
public class WechatUserResourceIntTest {

    private static final String DEFAULT_OPEN_ID = "AAAAAAAAAA";
    private static final String UPDATED_OPEN_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NICK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NICK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_AREA = "AAAAAAAAAA";
    private static final String UPDATED_AREA = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    @Autowired
    private WechatUserRepository wechatUserRepository;

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWechatUserMockMvc;

    private WechatUser wechatUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WechatUserResource wechatUserResource = new WechatUserResource(wechatUserService);
        this.restWechatUserMockMvc = MockMvcBuilders.standaloneSetup(wechatUserResource)
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
    public static WechatUser createEntity(EntityManager em) {
        WechatUser wechatUser = new WechatUser()
            .openId(DEFAULT_OPEN_ID)
            .nickName(DEFAULT_NICK_NAME)
            .icon(DEFAULT_ICON)
            .area(DEFAULT_AREA)
            .name(DEFAULT_NAME)
            .phone(DEFAULT_PHONE)
            .address(DEFAULT_ADDRESS)
            .language(DEFAULT_LANGUAGE);
        return wechatUser;
    }

    @Before
    public void initTest() {
        wechatUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createWechatUser() throws Exception {
        int databaseSizeBeforeCreate = wechatUserRepository.findAll().size();

        // Create the WechatUser
        restWechatUserMockMvc.perform(post("/api/wechat-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wechatUser)))
            .andExpect(status().isCreated());

        // Validate the WechatUser in the database
        List<WechatUser> wechatUserList = wechatUserRepository.findAll();
        assertThat(wechatUserList).hasSize(databaseSizeBeforeCreate + 1);
        WechatUser testWechatUser = wechatUserList.get(wechatUserList.size() - 1);
        assertThat(testWechatUser.getOpenId()).isEqualTo(DEFAULT_OPEN_ID);
        assertThat(testWechatUser.getNickName()).isEqualTo(DEFAULT_NICK_NAME);
        assertThat(testWechatUser.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testWechatUser.getArea()).isEqualTo(DEFAULT_AREA);
        assertThat(testWechatUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWechatUser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testWechatUser.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testWechatUser.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    public void createWechatUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wechatUserRepository.findAll().size();

        // Create the WechatUser with an existing ID
        wechatUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWechatUserMockMvc.perform(post("/api/wechat-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wechatUser)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<WechatUser> wechatUserList = wechatUserRepository.findAll();
        assertThat(wechatUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWechatUsers() throws Exception {
        // Initialize the database
        wechatUserRepository.saveAndFlush(wechatUser);

        // Get all the wechatUserList
        restWechatUserMockMvc.perform(get("/api/wechat-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wechatUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].openId").value(hasItem(DEFAULT_OPEN_ID.toString())))
            .andExpect(jsonPath("$.[*].nickName").value(hasItem(DEFAULT_NICK_NAME.toString())))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON.toString())))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())));
    }

    @Test
    @Transactional
    public void getWechatUser() throws Exception {
        // Initialize the database
        wechatUserRepository.saveAndFlush(wechatUser);

        // Get the wechatUser
        restWechatUserMockMvc.perform(get("/api/wechat-users/{id}", wechatUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wechatUser.getId().intValue()))
            .andExpect(jsonPath("$.openId").value(DEFAULT_OPEN_ID.toString()))
            .andExpect(jsonPath("$.nickName").value(DEFAULT_NICK_NAME.toString()))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON.toString()))
            .andExpect(jsonPath("$.area").value(DEFAULT_AREA.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWechatUser() throws Exception {
        // Get the wechatUser
        restWechatUserMockMvc.perform(get("/api/wechat-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWechatUser() throws Exception {
        // Initialize the database
        wechatUserService.save(wechatUser);

        int databaseSizeBeforeUpdate = wechatUserRepository.findAll().size();

        // Update the wechatUser
        WechatUser updatedWechatUser = wechatUserRepository.findOne(wechatUser.getId());
        updatedWechatUser
            .openId(UPDATED_OPEN_ID)
            .nickName(UPDATED_NICK_NAME)
            .icon(UPDATED_ICON)
            .area(UPDATED_AREA)
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .language(UPDATED_LANGUAGE);

        restWechatUserMockMvc.perform(put("/api/wechat-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWechatUser)))
            .andExpect(status().isOk());

        // Validate the WechatUser in the database
        List<WechatUser> wechatUserList = wechatUserRepository.findAll();
        assertThat(wechatUserList).hasSize(databaseSizeBeforeUpdate);
        WechatUser testWechatUser = wechatUserList.get(wechatUserList.size() - 1);
        assertThat(testWechatUser.getOpenId()).isEqualTo(UPDATED_OPEN_ID);
        assertThat(testWechatUser.getNickName()).isEqualTo(UPDATED_NICK_NAME);
        assertThat(testWechatUser.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testWechatUser.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testWechatUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWechatUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testWechatUser.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testWechatUser.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingWechatUser() throws Exception {
        int databaseSizeBeforeUpdate = wechatUserRepository.findAll().size();

        // Create the WechatUser

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWechatUserMockMvc.perform(put("/api/wechat-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wechatUser)))
            .andExpect(status().isCreated());

        // Validate the WechatUser in the database
        List<WechatUser> wechatUserList = wechatUserRepository.findAll();
        assertThat(wechatUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWechatUser() throws Exception {
        // Initialize the database
        wechatUserService.save(wechatUser);

        int databaseSizeBeforeDelete = wechatUserRepository.findAll().size();

        // Get the wechatUser
        restWechatUserMockMvc.perform(delete("/api/wechat-users/{id}", wechatUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WechatUser> wechatUserList = wechatUserRepository.findAll();
        assertThat(wechatUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WechatUser.class);
        WechatUser wechatUser1 = new WechatUser();
        wechatUser1.setId(1L);
        WechatUser wechatUser2 = new WechatUser();
        wechatUser2.setId(wechatUser1.getId());
        assertThat(wechatUser1).isEqualTo(wechatUser2);
        wechatUser2.setId(2L);
        assertThat(wechatUser1).isNotEqualTo(wechatUser2);
        wechatUser1.setId(null);
        assertThat(wechatUser1).isNotEqualTo(wechatUser2);
    }
}
