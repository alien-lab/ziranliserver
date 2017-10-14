package com.alienlab.ziranli.service.impl;

import com.alienlab.ziranli.domain.CourseImage;
import com.alienlab.ziranli.repository.CourseImageRepository;
import com.alienlab.ziranli.service.CourseImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 鸠小浅 on 2017/10/14.
 */

@Service
@Transactional
public class CourseImageServiceImpl implements CourseImageService {

    private final Logger log = LoggerFactory.getLogger(CourseImageServiceImpl.class);
    private final CourseImageRepository courseImageRepository;

    public CourseImageServiceImpl(CourseImageRepository courseImageRepository) {
        this.courseImageRepository = courseImageRepository;
    }

    @Override
    public CourseImage save(CourseImage courseImage) {
        log.debug("Request to save CourseImage : {}", courseImage);
        CourseImage result = courseImageRepository.save(courseImage);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseImage> findAll(Pageable pageable) {
        log.debug("Request to get all CourseImages");
        Page<CourseImage> result = courseImageRepository.findAll(pageable);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public CourseImage findOne(Long id) {
        log.debug("Request to get CourseImage : {}", id);
        CourseImage courseImage = courseImageRepository.findOne(id);
        return courseImage;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseImage : {}", id);
        courseImageRepository.delete(id);
    }
}
