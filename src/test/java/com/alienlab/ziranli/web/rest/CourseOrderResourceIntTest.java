package com.alienlab.ziranli.web.rest;

import com.alienlab.ziranli.ZiranliserverApp;

import com.alienlab.ziranli.domain.CourseOrder;
import com.alienlab.ziranli.repository.CourseOrderRepository;
import com.alienlab.ziranli.service.CourseOrderService;
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
 * Test class for the CourseOrderResource REST controller.
 *
 * @see CourseOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZiranliserverApp.class)
public class CourseOrderResourceIntTest {

    private static final Float DEFAULT_PAY_PRICE = 1F;
    private static final Float UPDATED_PAY_PRICE = 2F;

    private static final ZonedDateTime DEFAULT_PAY_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PAY_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_IS_SHARE = "AAAAAAAAAA";
    private static final String UPDATED_IS_SHARE = "BBBBBBBBBB";

    private static final String DEFAULT_PAY_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_PAY_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_WECHAT_ORDERNO = "AAAAAAAAAA";
    private static final String UPDATED_WECHAT_ORDERNO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_ORDER_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ORDER_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private CourseOrderRepository courseOrderRepository;

    @Autowired
    private CourseOrderService courseOrderService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCourseOrderMockMvc;

    private CourseOrder courseOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CourseOrderResource courseOrderResource = new CourseOrderResource(courseOrderService);
        this.restCourseOrderMockMvc = MockMvcBuilders.standaloneSetup(courseOrderResource)
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
    public static CourseOrder createEntity(EntityManager em) {
        CourseOrder courseOrder = new CourseOrder()
            .payPrice(DEFAULT_PAY_PRICE)
            .payTime(DEFAULT_PAY_TIME)
            .isShare(DEFAULT_IS_SHARE)
            .payStatus(DEFAULT_PAY_STATUS)
            .wechatOrderno(DEFAULT_WECHAT_ORDERNO)
            .orderTime(DEFAULT_ORDER_TIME);
        return courseOrder;
    }

    @Before
    public void initTest() {
        courseOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourseOrder() throws Exception {
        int databaseSizeBeforeCreate = courseOrderRepository.findAll().size();

        // Create the CourseOrder
        restCourseOrderMockMvc.perform(post("/api/course-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseOrder)))
            .andExpect(status().isCreated());

        // Validate the CourseOrder in the database
        List<CourseOrder> courseOrderList = courseOrderRepository.findAll();
        assertThat(courseOrderList).hasSize(databaseSizeBeforeCreate + 1);
        CourseOrder testCourseOrder = courseOrderList.get(courseOrderList.size() - 1);
        assertThat(testCourseOrder.getPayPrice()).isEqualTo(DEFAULT_PAY_PRICE);
        assertThat(testCourseOrder.getPayTime()).isEqualTo(DEFAULT_PAY_TIME);
        assertThat(testCourseOrder.getIsShare()).isEqualTo(DEFAULT_IS_SHARE);
        assertThat(testCourseOrder.getPayStatus()).isEqualTo(DEFAULT_PAY_STATUS);
        assertThat(testCourseOrder.getWechatOrderno()).isEqualTo(DEFAULT_WECHAT_ORDERNO);
        assertThat(testCourseOrder.getOrderTime()).isEqualTo(DEFAULT_ORDER_TIME);
    }

    @Test
    @Transactional
    public void createCourseOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseOrderRepository.findAll().size();

        // Create the CourseOrder with an existing ID
        courseOrder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseOrderMockMvc.perform(post("/api/course-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseOrder)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CourseOrder> courseOrderList = courseOrderRepository.findAll();
        assertThat(courseOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCourseOrders() throws Exception {
        // Initialize the database
        courseOrderRepository.saveAndFlush(courseOrder);

        // Get all the courseOrderList
        restCourseOrderMockMvc.perform(get("/api/course-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].payPrice").value(hasItem(DEFAULT_PAY_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].payTime").value(hasItem(sameInstant(DEFAULT_PAY_TIME))))
            .andExpect(jsonPath("$.[*].isShare").value(hasItem(DEFAULT_IS_SHARE.toString())))
            .andExpect(jsonPath("$.[*].payStatus").value(hasItem(DEFAULT_PAY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].wechatOrderno").value(hasItem(DEFAULT_WECHAT_ORDERNO.toString())))
            .andExpect(jsonPath("$.[*].orderTime").value(hasItem(sameInstant(DEFAULT_ORDER_TIME))));
    }

    @Test
    @Transactional
    public void getCourseOrder() throws Exception {
        // Initialize the database
        courseOrderRepository.saveAndFlush(courseOrder);

        // Get the courseOrder
        restCourseOrderMockMvc.perform(get("/api/course-orders/{id}", courseOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(courseOrder.getId().intValue()))
            .andExpect(jsonPath("$.payPrice").value(DEFAULT_PAY_PRICE.doubleValue()))
            .andExpect(jsonPath("$.payTime").value(sameInstant(DEFAULT_PAY_TIME)))
            .andExpect(jsonPath("$.isShare").value(DEFAULT_IS_SHARE.toString()))
            .andExpect(jsonPath("$.payStatus").value(DEFAULT_PAY_STATUS.toString()))
            .andExpect(jsonPath("$.wechatOrderno").value(DEFAULT_WECHAT_ORDERNO.toString()))
            .andExpect(jsonPath("$.orderTime").value(sameInstant(DEFAULT_ORDER_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingCourseOrder() throws Exception {
        // Get the courseOrder
        restCourseOrderMockMvc.perform(get("/api/course-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourseOrder() throws Exception {
        // Initialize the database
        courseOrderService.save(courseOrder);

        int databaseSizeBeforeUpdate = courseOrderRepository.findAll().size();

        // Update the courseOrder
        CourseOrder updatedCourseOrder = courseOrderRepository.findOne(courseOrder.getId());
        updatedCourseOrder
            .payPrice(UPDATED_PAY_PRICE)
            .payTime(UPDATED_PAY_TIME)
            .isShare(UPDATED_IS_SHARE)
            .payStatus(UPDATED_PAY_STATUS)
            .wechatOrderno(UPDATED_WECHAT_ORDERNO)
            .orderTime(UPDATED_ORDER_TIME);

        restCourseOrderMockMvc.perform(put("/api/course-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourseOrder)))
            .andExpect(status().isOk());

        // Validate the CourseOrder in the database
        List<CourseOrder> courseOrderList = courseOrderRepository.findAll();
        assertThat(courseOrderList).hasSize(databaseSizeBeforeUpdate);
        CourseOrder testCourseOrder = courseOrderList.get(courseOrderList.size() - 1);
        assertThat(testCourseOrder.getPayPrice()).isEqualTo(UPDATED_PAY_PRICE);
        assertThat(testCourseOrder.getPayTime()).isEqualTo(UPDATED_PAY_TIME);
        assertThat(testCourseOrder.getIsShare()).isEqualTo(UPDATED_IS_SHARE);
        assertThat(testCourseOrder.getPayStatus()).isEqualTo(UPDATED_PAY_STATUS);
        assertThat(testCourseOrder.getWechatOrderno()).isEqualTo(UPDATED_WECHAT_ORDERNO);
        assertThat(testCourseOrder.getOrderTime()).isEqualTo(UPDATED_ORDER_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingCourseOrder() throws Exception {
        int databaseSizeBeforeUpdate = courseOrderRepository.findAll().size();

        // Create the CourseOrder

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCourseOrderMockMvc.perform(put("/api/course-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseOrder)))
            .andExpect(status().isCreated());

        // Validate the CourseOrder in the database
        List<CourseOrder> courseOrderList = courseOrderRepository.findAll();
        assertThat(courseOrderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCourseOrder() throws Exception {
        // Initialize the database
        courseOrderService.save(courseOrder);

        int databaseSizeBeforeDelete = courseOrderRepository.findAll().size();

        // Get the courseOrder
        restCourseOrderMockMvc.perform(delete("/api/course-orders/{id}", courseOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CourseOrder> courseOrderList = courseOrderRepository.findAll();
        assertThat(courseOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseOrder.class);
        CourseOrder courseOrder1 = new CourseOrder();
        courseOrder1.setId(1L);
        CourseOrder courseOrder2 = new CourseOrder();
        courseOrder2.setId(courseOrder1.getId());
        assertThat(courseOrder1).isEqualTo(courseOrder2);
        courseOrder2.setId(2L);
        assertThat(courseOrder1).isNotEqualTo(courseOrder2);
        courseOrder1.setId(null);
        assertThat(courseOrder1).isNotEqualTo(courseOrder2);
    }
}
