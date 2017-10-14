package com.alienlab.ziranli.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by 鸠小浅 on 2017/10/14.
 */
@Entity
@Table(name = "course_image")
@ApiModel(value = "课程图片")
public class CourseImage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value="图片")
    @Column(name = "image")
    private String image;

    @ApiModelProperty(value="关联课程")
    @ManyToOne
    private Course course;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public CourseImage image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Course getCourse() {
        return course;
    }

    public CourseImage course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CourseImage courseImage = (CourseImage) o;
        if (courseImage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), courseImage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CourseImage{" +
            "id=" + id +
            ", image='" + image + '\'' +
            '}';
    }
}
