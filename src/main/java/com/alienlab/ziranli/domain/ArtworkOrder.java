package com.alienlab.ziranli.domain;


import com.alienlab.ziranli.web.wechat.bean.entity.WechatUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ArtworkOrder.
 */
@Entity
@Table(name = "artwork_order")
@ApiModel(value = "艺术品订单")
public class ArtworkOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value="订单数量")
    @Column(name = "amount")
    private Integer amount;

    @ApiModelProperty(value="支付价格")
    @Column(name = "pay_price")
    private Float payPrice;

    @ApiModelProperty(value="支付状态")
    @Column(name = "pay_status")
    private String payStatus;

    @ApiModelProperty(value="微信支付订单号")
    @Column(name = "wechat_orderno")
    private String wechatOrderno;

    @ApiModelProperty(value="订单时间")
    @Column(name = "order_time")
    private ZonedDateTime orderTime;

    @ApiModelProperty(value="支付时间")
    @Column(name = "pay_time")
    private ZonedDateTime payTime;

    @ApiModelProperty(value="派送地址")
    @Column(name = "address")
    private String address;

    @ApiModelProperty(value="联系电话")
    @Column(name = "phone")
    private String phone;

    @ApiModelProperty(value="收件人")
    @Column(name = "contact")
    private String contact;

    @ApiModelProperty(value="发货标记")
    @Column(name = "order_flag")
    private String orderFlag;

    @ApiModelProperty(value="关联艺术品")
    @ManyToOne
    private Artwork artwork;

    @ApiModelProperty(value="关联微信用户")
    @ManyToOne
    private WechatUser user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public ArtworkOrder amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Float getPayPrice() {
        return payPrice;
    }

    public ArtworkOrder payPrice(Float payPrice) {
        this.payPrice = payPrice;
        return this;
    }

    public void setPayPrice(Float payPrice) {
        this.payPrice = payPrice;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public ArtworkOrder payStatus(String payStatus) {
        this.payStatus = payStatus;
        return this;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getWechatOrderno() {
        return wechatOrderno;
    }

    public ArtworkOrder wechatOrderno(String wechatOrderno) {
        this.wechatOrderno = wechatOrderno;
        return this;
    }

    public void setWechatOrderno(String wechatOrderno) {
        this.wechatOrderno = wechatOrderno;
    }

    public ZonedDateTime getOrderTime() {
        return orderTime;
    }

    public ArtworkOrder orderTime(ZonedDateTime orderTime) {
        this.orderTime = orderTime;
        return this;
    }

    public void setOrderTime(ZonedDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public ZonedDateTime getPayTime() {
        return payTime;
    }

    public ArtworkOrder payTime(ZonedDateTime payTime) {
        this.payTime = payTime;
        return this;
    }

    public void setPayTime(ZonedDateTime payTime) {
        this.payTime = payTime;
    }

    public String getAddress() {
        return address;
    }

    public ArtworkOrder address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public ArtworkOrder phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContact() {
        return contact;
    }

    public ArtworkOrder contact(String contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getOrderFlag() {
        return orderFlag;
    }

    public ArtworkOrder orderFlag(String orderFlag) {
        this.orderFlag = orderFlag;
        return this;
    }

    public void setOrderFlag(String orderFlag) {
        this.orderFlag = orderFlag;
    }

    public Artwork getArtwork() {
        return artwork;
    }

    public ArtworkOrder artwork(Artwork artwork) {
        this.artwork = artwork;
        return this;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }

    public WechatUser getUser() {
        return user;
    }

    public ArtworkOrder user(WechatUser wechatUser) {
        this.user = wechatUser;
        return this;
    }

    public void setUser(WechatUser wechatUser) {
        this.user = wechatUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArtworkOrder artworkOrder = (ArtworkOrder) o;
        if (artworkOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), artworkOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ArtworkOrder{" +
            "id=" + getId() +
            ", amount='" + getAmount() + "'" +
            ", payPrice='" + getPayPrice() + "'" +
            ", payStatus='" + getPayStatus() + "'" +
            ", wechatOrderno='" + getWechatOrderno() + "'" +
            ", orderTime='" + getOrderTime() + "'" +
            ", payTime='" + getPayTime() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", contact='" + getContact() + "'" +
            ", orderFlag='" + getOrderFlag() + "'" +
            "}";
    }
}
