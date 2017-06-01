package com.alienlab.ziranli.web.rest;

import com.alienlab.ziranli.ZiranliserverApp;

import com.alienlab.ziranli.domain.Course;
import com.alienlab.ziranli.repository.CourseRepository;
import com.alienlab.ziranli.service.CourseService;
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
 * Test class for the CourseResource REST controller.
 *
 * @see CourseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZiranliserverApp.class)
public class CourseResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_COVER_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_COVER_IMAGE = "BBBBBBBBBB";

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final Float DEFAULT_PRICE_2 = 1F;
    private static final Float UPDATED_PRICE_2 = 2F;

    private static final ZonedDateTime DEFAULT_COURSE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_COURSE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CREATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_QR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_QR_CODE = "BBBBBBBBBB";

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCourseMockMvc;

    private Course course;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CourseResource courseResource = new CourseResource(courseService);
        this.restCourseMockMvc = MockMvcBuilders.standaloneSetup(courseResource)
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
    public static Course createEntity(EntityManager em) {
        Course course = new Course()
            .name(DEFAULT_NAME)
            .memo(DEFAULT_MEMO)
            .coverImage(DEFAULT_COVER_IMAGE)
            .price(DEFAULT_PRICE)
            .price2(DEFAULT_PRICE_2)
            .courseTime(DEFAULT_COURSE_TIME)
            .createTime(DEFAULT_CREATE_TIME)
            .status(DEFAULT_STATUS)
            .qrCode(DEFAULT_QR_CODE);
        return course;
    }

    @Before
    public void initTest() {
        course = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isCreated());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourse.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testCourse.getCoverImage()).isEqualTo(DEFAULT_COVER_IMAGE);
        assertThat(testCourse.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCourse.getPrice2()).isEqualTo(DEFAULT_PRICE_2);
        assertThat(testCourse.getCourseTime()).isEqualTo(DEFAULT_COURSE_TIME);
        assertThat(testCourse.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testCourse.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCourse.getQrCode()).isEqualTo(DEFAULT_QR_CODE);
    }

    @Test
    @Transactional
    public void createCourseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course with an existing ID
        course.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCourses() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].coverImage").value(hasItem(DEFAULT_COVER_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].price2").value(hasItem(DEFAULT_PRICE_2.doubleValue())))
            .andExpect(jsonPath("$.[*].courseTime").value(hasItem(sameInstant(DEFAULT_COURSE_TIME))))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(sameInstant(DEFAULT_CREATE_TIME))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].qrCode").value(hasItem(DEFAULT_QR_CODE.toString())));
    }

    @Test
    @Transactional
    public void getCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", course.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(course.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.coverImage").value(DEFAULT_COVER_IMAGE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.price2").value(DEFAULT_PRICE_2.doubleValue()))
            .andExpect(jsonPath("$.courseTime").value(sameInstant(DEFAULT_COURSE_TIME)))
            .andExpect(jsonPath("$.createTime").value(sameInstant(DEFAULT_CREATE_TIME)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.qrCode").value(DEFAULT_QR_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCourse() throws Exception {
        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourse() throws Exception {
        // Initialize the database
        courseService.save(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course
        Course updatedCourse = courseRepository.findOne(course.getId());
        updatedCourse
            .name(UPDATED_NAME)
            .memo(UPDATED_MEMO)
            .coverImage(UPDATED_COVER_IMAGE)
            .price(UPDATED_PRICE)
            .price2(UPDATED_PRICE_2)
            .courseTime(UPDATED_COURSE_TIME)
            .createTime(UPDATED_CREATE_TIME)
            .status(UPDATED_STATUS)
            .qrCode(UPDATED_QR_CODE);

        restCourseMockMvc.perform(put("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourse)))
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourse.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testCourse.getCoverImage()).isEqualTo(UPDATED_COVER_IMAGE);
        assertThat(testCourse.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCourse.getPrice2()).isEqualTo(UPDATED_PRICE_2);
        assertThat(testCourse.getCourseTime()).isEqualTo(UPDATED_COURSE_TIME);
        assertThat(testCourse.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testCourse.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCourse.getQrCode()).isEqualTo(UPDATED_QR_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Create the Course

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCourseMockMvc.perform(put("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isCreated());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCourse() throws Exception {
        // Initialize the database
        courseService.save(course);

        int databaseSizeBeforeDelete = courseRepository.findAll().size();

        // Get the course
        restCourseMockMvc.perform(delete("/api/courses/{id}", course.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Course.class);
        Course course1 = new Course();
        course1.setId(1L);
        Course course2 = new Course();
        course2.setId(course1.getId());
        assertThat(course1).isEqualTo(course2);
        course2.setId(2L);
        assertThat(course1).isNotEqualTo(course2);
        course1.setId(null);
        assertThat(course1).isNotEqualTo(course2);
    }
}
