package com.alienlab.ziranli.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A WechatUser.
 */
@Entity
@Table(name = "wechat_user")
public class WechatUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "open_id")
    private String openId;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "icon")
    private String icon;

    @Column(name = "area")
    private String area;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "language")
    private String language;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public WechatUser openId(String openId) {
        this.openId = openId;
        return this;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public WechatUser nickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIcon() {
        return icon;
    }

    public WechatUser icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getArea() {
        return area;
    }

    public WechatUser area(String area) {
        this.area = area;
        return this;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public WechatUser name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public WechatUser phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public WechatUser address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLanguage() {
        return language;
    }

    public WechatUser language(String language) {
        this.language = language;
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WechatUser wechatUser = (WechatUser) o;
        if (wechatUser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wechatUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WechatUser{" +
            "id=" + getId() +
            ", openId='" + getOpenId() + "'" +
            ", nickName='" + getNickName() + "'" +
            ", icon='" + getIcon() + "'" +
            ", area='" + getArea() + "'" +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", language='" + getLanguage() + "'" +
            "}";
    }
}
