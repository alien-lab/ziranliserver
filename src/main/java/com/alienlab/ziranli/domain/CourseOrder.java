package com.alienlab.ziranli.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A CourseOrder.
 */
@Entity
@Table(name = "course_order")
public class CourseOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pay_price")
    private Float payPrice;

    @Column(name = "pay_time")
    private ZonedDateTime payTime;

    @Column(name = "is_share")
    private String isShare;

    @Column(name = "pay_status")
    private String payStatus;

    @Column(name = "wechat_orderno")
    private String wechatOrderno;

    @Column(name = "order_time")
    private ZonedDateTime orderTime;

    @ManyToOne
    private WechatUser user;

    @ManyToOne
    private Course course;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPayPrice() {
        return payPrice;
    }

    public CourseOrder payPrice(Float payPrice) {
        this.payPrice = payPrice;
        return this;
    }

    public void setPayPrice(Float payPrice) {
        this.payPrice = payPrice;
    }

    public ZonedDateTime getPayTime() {
        return payTime;
    }

    public CourseOrder payTime(ZonedDateTime payTime) {
        this.payTime = payTime;
        return this;
    }

    public void setPayTime(ZonedDateTime payTime) {
        this.payTime = payTime;
    }

    public String getIsShare() {
        return isShare;
    }

    public CourseOrder isShare(String isShare) {
        this.isShare = isShare;
        return this;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public CourseOrder payStatus(String payStatus) {
        this.payStatus = payStatus;
        return this;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getWechatOrderno() {
        return wechatOrderno;
    }

    public CourseOrder wechatOrderno(String wechatOrderno) {
        this.wechatOrderno = wechatOrderno;
        return this;
    }

    public void setWechatOrderno(String wechatOrderno) {
        this.wechatOrderno = wechatOrderno;
    }

    public ZonedDateTime getOrderTime() {
        return orderTime;
    }

    public CourseOrder orderTime(ZonedDateTime orderTime) {
        this.orderTime = orderTime;
        return this;
    }

    public void setOrderTime(ZonedDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public WechatUser getUser() {
        return user;
    }

    public CourseOrder user(WechatUser wechatUser) {
        this.user = wechatUser;
        return this;
    }

    public void setUser(WechatUser wechatUser) {
        this.user = wechatUser;
    }

    public Course getCourse() {
        return course;
    }

    public CourseOrder course(Course course) {
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
        CourseOrder courseOrder = (CourseOrder) o;
        if (courseOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), courseOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CourseOrder{" +
            "id=" + getId() +
            ", payPrice='" + getPayPrice() + "'" +
            ", payTime='" + getPayTime() + "'" +
            ", isShare='" + getIsShare() + "'" +
            ", payStatus='" + getPayStatus() + "'" +
            ", wechatOrderno='" + getWechatOrderno() + "'" +
            ", orderTime='" + getOrderTime() + "'" +
            "}";
    }
}
