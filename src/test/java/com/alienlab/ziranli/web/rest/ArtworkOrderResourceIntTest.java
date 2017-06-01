package com.alienlab.ziranli.web.rest;

import com.alienlab.ziranli.ZiranliserverApp;

import com.alienlab.ziranli.domain.ArtworkOrder;
import com.alienlab.ziranli.repository.ArtworkOrderRepository;
import com.alienlab.ziranli.service.ArtworkOrderService;
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
 * Test class for the ArtworkOrderResource REST controller.
 *
 * @see ArtworkOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZiranliserverApp.class)
public class ArtworkOrderResourceIntTest {

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;

    private static final Float DEFAULT_PAY_PRICE = 1F;
    private static final Float UPDATED_PAY_PRICE = 2F;

    private static final String DEFAULT_PAY_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_PAY_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_WECHAT_ORDERNO = "AAAAAAAAAA";
    private static final String UPDATED_WECHAT_ORDERNO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_ORDER_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ORDER_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_PAY_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PAY_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_FLAG = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_FLAG = "BBBBBBBBBB";

    @Autowired
    private ArtworkOrderRepository artworkOrderRepository;

    @Autowired
    private ArtworkOrderService artworkOrderService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restArtworkOrderMockMvc;

    private ArtworkOrder artworkOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ArtworkOrderResource artworkOrderResource = new ArtworkOrderResource(artworkOrderService);
        this.restArtworkOrderMockMvc = MockMvcBuilders.standaloneSetup(artworkOrderResource)
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
    public static ArtworkOrder createEntity(EntityManager em) {
        ArtworkOrder artworkOrder = new ArtworkOrder()
            .amount(DEFAULT_AMOUNT)
            .payPrice(DEFAULT_PAY_PRICE)
            .payStatus(DEFAULT_PAY_STATUS)
            .wechatOrderno(DEFAULT_WECHAT_ORDERNO)
            .orderTime(DEFAULT_ORDER_TIME)
            .payTime(DEFAULT_PAY_TIME)
            .address(DEFAULT_ADDRESS)
            .phone(DEFAULT_PHONE)
            .contact(DEFAULT_CONTACT)
            .orderFlag(DEFAULT_ORDER_FLAG);
        return artworkOrder;
    }

    @Before
    public void initTest() {
        artworkOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createArtworkOrder() throws Exception {
        int databaseSizeBeforeCreate = artworkOrderRepository.findAll().size();

        // Create the ArtworkOrder
        restArtworkOrderMockMvc.perform(post("/api/artwork-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artworkOrder)))
            .andExpect(status().isCreated());

        // Validate the ArtworkOrder in the database
        List<ArtworkOrder> artworkOrderList = artworkOrderRepository.findAll();
        assertThat(artworkOrderList).hasSize(databaseSizeBeforeCreate + 1);
        ArtworkOrder testArtworkOrder = artworkOrderList.get(artworkOrderList.size() - 1);
        assertThat(testArtworkOrder.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testArtworkOrder.getPayPrice()).isEqualTo(DEFAULT_PAY_PRICE);
        assertThat(testArtworkOrder.getPayStatus()).isEqualTo(DEFAULT_PAY_STATUS);
        assertThat(testArtworkOrder.getWechatOrderno()).isEqualTo(DEFAULT_WECHAT_ORDERNO);
        assertThat(testArtworkOrder.getOrderTime()).isEqualTo(DEFAULT_ORDER_TIME);
        assertThat(testArtworkOrder.getPayTime()).isEqualTo(DEFAULT_PAY_TIME);
        assertThat(testArtworkOrder.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testArtworkOrder.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testArtworkOrder.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testArtworkOrder.getOrderFlag()).isEqualTo(DEFAULT_ORDER_FLAG);
    }

    @Test
    @Transactional
    public void createArtworkOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = artworkOrderRepository.findAll().size();

        // Create the ArtworkOrder with an existing ID
        artworkOrder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtworkOrderMockMvc.perform(post("/api/artwork-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artworkOrder)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ArtworkOrder> artworkOrderList = artworkOrderRepository.findAll();
        assertThat(artworkOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllArtworkOrders() throws Exception {
        // Initialize the database
        artworkOrderRepository.saveAndFlush(artworkOrder);

        // Get all the artworkOrderList
        restArtworkOrderMockMvc.perform(get("/api/artwork-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artworkOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].payPrice").value(hasItem(DEFAULT_PAY_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].payStatus").value(hasItem(DEFAULT_PAY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].wechatOrderno").value(hasItem(DEFAULT_WECHAT_ORDERNO.toString())))
            .andExpect(jsonPath("$.[*].orderTime").value(hasItem(sameInstant(DEFAULT_ORDER_TIME))))
            .andExpect(jsonPath("$.[*].payTime").value(hasItem(sameInstant(DEFAULT_PAY_TIME))))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.toString())))
            .andExpect(jsonPath("$.[*].orderFlag").value(hasItem(DEFAULT_ORDER_FLAG.toString())));
    }

    @Test
    @Transactional
    public void getArtworkOrder() throws Exception {
        // Initialize the database
        artworkOrderRepository.saveAndFlush(artworkOrder);

        // Get the artworkOrder
        restArtworkOrderMockMvc.perform(get("/api/artwork-orders/{id}", artworkOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(artworkOrder.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.payPrice").value(DEFAULT_PAY_PRICE.doubleValue()))
            .andExpect(jsonPath("$.payStatus").value(DEFAULT_PAY_STATUS.toString()))
            .andExpect(jsonPath("$.wechatOrderno").value(DEFAULT_WECHAT_ORDERNO.toString()))
            .andExpect(jsonPath("$.orderTime").value(sameInstant(DEFAULT_ORDER_TIME)))
            .andExpect(jsonPath("$.payTime").value(sameInstant(DEFAULT_PAY_TIME)))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT.toString()))
            .andExpect(jsonPath("$.orderFlag").value(DEFAULT_ORDER_FLAG.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingArtworkOrder() throws Exception {
        // Get the artworkOrder
        restArtworkOrderMockMvc.perform(get("/api/artwork-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtworkOrder() throws Exception {
        // Initialize the database
        artworkOrderService.save(artworkOrder);

        int databaseSizeBeforeUpdate = artworkOrderRepository.findAll().size();

        // Update the artworkOrder
        ArtworkOrder updatedArtworkOrder = artworkOrderRepository.findOne(artworkOrder.getId());
        updatedArtworkOrder
            .amount(UPDATED_AMOUNT)
            .payPrice(UPDATED_PAY_PRICE)
            .payStatus(UPDATED_PAY_STATUS)
            .wechatOrderno(UPDATED_WECHAT_ORDERNO)
            .orderTime(UPDATED_ORDER_TIME)
            .payTime(UPDATED_PAY_TIME)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .contact(UPDATED_CONTACT)
            .orderFlag(UPDATED_ORDER_FLAG);

        restArtworkOrderMockMvc.perform(put("/api/artwork-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedArtworkOrder)))
            .andExpect(status().isOk());

        // Validate the ArtworkOrder in the database
        List<ArtworkOrder> artworkOrderList = artworkOrderRepository.findAll();
        assertThat(artworkOrderList).hasSize(databaseSizeBeforeUpdate);
        ArtworkOrder testArtworkOrder = artworkOrderList.get(artworkOrderList.size() - 1);
        assertThat(testArtworkOrder.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testArtworkOrder.getPayPrice()).isEqualTo(UPDATED_PAY_PRICE);
        assertThat(testArtworkOrder.getPayStatus()).isEqualTo(UPDATED_PAY_STATUS);
        assertThat(testArtworkOrder.getWechatOrderno()).isEqualTo(UPDATED_WECHAT_ORDERNO);
        assertThat(testArtworkOrder.getOrderTime()).isEqualTo(UPDATED_ORDER_TIME);
        assertThat(testArtworkOrder.getPayTime()).isEqualTo(UPDATED_PAY_TIME);
        assertThat(testArtworkOrder.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testArtworkOrder.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testArtworkOrder.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testArtworkOrder.getOrderFlag()).isEqualTo(UPDATED_ORDER_FLAG);
    }

    @Test
    @Transactional
    public void updateNonExistingArtworkOrder() throws Exception {
        int databaseSizeBeforeUpdate = artworkOrderRepository.findAll().size();

        // Create the ArtworkOrder

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restArtworkOrderMockMvc.perform(put("/api/artwork-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artworkOrder)))
            .andExpect(status().isCreated());

        // Validate the ArtworkOrder in the database
        List<ArtworkOrder> artworkOrderList = artworkOrderRepository.findAll();
        assertThat(artworkOrderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteArtworkOrder() throws Exception {
        // Initialize the database
        artworkOrderService.save(artworkOrder);

        int databaseSizeBeforeDelete = artworkOrderRepository.findAll().size();

        // Get the artworkOrder
        restArtworkOrderMockMvc.perform(delete("/api/artwork-orders/{id}", artworkOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ArtworkOrder> artworkOrderList = artworkOrderRepository.findAll();
        assertThat(artworkOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArtworkOrder.class);
        ArtworkOrder artworkOrder1 = new ArtworkOrder();
        artworkOrder1.setId(1L);
        ArtworkOrder artworkOrder2 = new ArtworkOrder();
        artworkOrder2.setId(artworkOrder1.getId());
        assertThat(artworkOrder1).isEqualTo(artworkOrder2);
        artworkOrder2.setId(2L);
        assertThat(artworkOrder1).isNotEqualTo(artworkOrder2);
        artworkOrder1.setId(null);
        assertThat(artworkOrder1).isNotEqualTo(artworkOrder2);
    }
}
