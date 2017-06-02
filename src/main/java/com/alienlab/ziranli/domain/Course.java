package com.alienlab.ziranli.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "course")
@ApiModel(value = "课程")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "课程名")
    @Column(name = "name")
    private String name;

    @ApiModelProperty(value="课程介绍")
    @Column(name = "memo")
    private String memo;

    @ApiModelProperty(value="课程图片")
    @Column(name = "cover_image")
    private String coverImage;

    @ApiModelProperty(value="入场费")
    @Column(name = "price")
    private Float price;

    @ApiModelProperty(value="分享后入场费")
    @Column(name = "price_2")
    private Float price2;

    @ApiModelProperty(value="课程时间")
    @Column(name = "course_time")
    private ZonedDateTime courseTime;

    @ApiModelProperty(value="创建时间")
    @Column(name = "create_time")
    private ZonedDateTime createTime;

    @ApiModelProperty(value="状态")
    @Column(name = "status")
    private String status;

    @ApiModelProperty(value="课程二维码")
    @Column(name = "qr_code")
    private String qrCode;

    @ApiModelProperty(value="关联课程类型")
    @ManyToOne
    private CourseType courseType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Course name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public Course memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public Course coverImage(String coverImage) {
        this.coverImage = coverImage;
        return this;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Float getPrice() {
        return price;
    }

    public Course price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getPrice2() {
        return price2;
    }

    public Course price2(Float price2) {
        this.price2 = price2;
        return this;
    }

    public void setPrice2(Float price2) {
        this.price2 = price2;
    }

    public ZonedDateTime getCourseTime() {
        return courseTime;
    }

    public Course courseTime(ZonedDateTime courseTime) {
        this.courseTime = courseTime;
        return this;
    }

    public void setCourseTime(ZonedDateTime courseTime) {
        this.courseTime = courseTime;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public Course createTime(ZonedDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public Course status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQrCode() {
        return qrCode;
    }

    public Course qrCode(String qrCode) {
        this.qrCode = qrCode;
        return this;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public Course courseType(CourseType courseType) {
        this.courseType = courseType;
        return this;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Course course = (Course) o;
        if (course.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), course.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", memo='" + getMemo() + "'" +
            ", coverImage='" + getCoverImage() + "'" +
            ", price='" + getPrice() + "'" +
            ", price2='" + getPrice2() + "'" +
            ", courseTime='" + getCourseTime() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            ", status='" + getStatus() + "'" +
            ", qrCode='" + getQrCode() + "'" +
            "}";
    }
}
