package com.alienlab.ziranli.web.rest;

import com.alienlab.ziranli.ZiranliserverApp;

import com.alienlab.ziranli.domain.CourseType;
import com.alienlab.ziranli.repository.CourseTypeRepository;
import com.alienlab.ziranli.service.CourseTypeService;
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
 * Test class for the CourseTypeResource REST controller.
 *
 * @see CourseTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZiranliserverApp.class)
public class CourseTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @Autowired
    private CourseTypeService courseTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCourseTypeMockMvc;

    private CourseType courseType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CourseTypeResource courseTypeResource = new CourseTypeResource(courseTypeService);
        this.restCourseTypeMockMvc = MockMvcBuilders.standaloneSetup(courseTypeResource)
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
    public static CourseType createEntity(EntityManager em) {
        CourseType courseType = new CourseType()
            .name(DEFAULT_NAME);
        return courseType;
    }

    @Before
    public void initTest() {
        courseType = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourseType() throws Exception {
        int databaseSizeBeforeCreate = courseTypeRepository.findAll().size();

        // Create the CourseType
        restCourseTypeMockMvc.perform(post("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseType)))
            .andExpect(status().isCreated());

        // Validate the CourseType in the database
        List<CourseType> courseTypeList = courseTypeRepository.findAll();
        assertThat(courseTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CourseType testCourseType = courseTypeList.get(courseTypeList.size() - 1);
        assertThat(testCourseType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCourseTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseTypeRepository.findAll().size();

        // Create the CourseType with an existing ID
        courseType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseTypeMockMvc.perform(post("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseType)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CourseType> courseTypeList = courseTypeRepository.findAll();
        assertThat(courseTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCourseTypes() throws Exception {
        // Initialize the database
        courseTypeRepository.saveAndFlush(courseType);

        // Get all the courseTypeList
        restCourseTypeMockMvc.perform(get("/api/course-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCourseType() throws Exception {
        // Initialize the database
        courseTypeRepository.saveAndFlush(courseType);

        // Get the courseType
        restCourseTypeMockMvc.perform(get("/api/course-types/{id}", courseType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(courseType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCourseType() throws Exception {
        // Get the courseType
        restCourseTypeMockMvc.perform(get("/api/course-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourseType() throws Exception {
        // Initialize the database
        courseTypeService.save(courseType);

        int databaseSizeBeforeUpdate = courseTypeRepository.findAll().size();

        // Update the courseType
        CourseType updatedCourseType = courseTypeRepository.findOne(courseType.getId());
        updatedCourseType
            .name(UPDATED_NAME);

        restCourseTypeMockMvc.perform(put("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourseType)))
            .andExpect(status().isOk());

        // Validate the CourseType in the database
        List<CourseType> courseTypeList = courseTypeRepository.findAll();
        assertThat(courseTypeList).hasSize(databaseSizeBeforeUpdate);
        CourseType testCourseType = courseTypeList.get(courseTypeList.size() - 1);
        assertThat(testCourseType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingCourseType() throws Exception {
        int databaseSizeBeforeUpdate = courseTypeRepository.findAll().size();

        // Create the CourseType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCourseTypeMockMvc.perform(put("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseType)))
            .andExpect(status().isCreated());

        // Validate the CourseType in the database
        List<CourseType> courseTypeList = courseTypeRepository.findAll();
        assertThat(courseTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCourseType() throws Exception {
        // Initialize the database
        courseTypeService.save(courseType);

        int databaseSizeBeforeDelete = courseTypeRepository.findAll().size();

        // Get the courseType
        restCourseTypeMockMvc.perform(delete("/api/course-types/{id}", courseType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CourseType> courseTypeList = courseTypeRepository.findAll();
        assertThat(courseTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseType.class);
        CourseType courseType1 = new CourseType();
        courseType1.setId(1L);
        CourseType courseType2 = new CourseType();
        courseType2.setId(courseType1.getId());
        assertThat(courseType1).isEqualTo(courseType2);
        courseType2.setId(2L);
        assertThat(courseType1).isNotEqualTo(courseType2);
        courseType1.setId(null);
        assertThat(courseType1).isNotEqualTo(courseType2);
    }
}
