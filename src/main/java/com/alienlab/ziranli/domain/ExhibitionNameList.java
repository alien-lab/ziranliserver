package com.alienlab.ziranli.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ExhibitionNameList.
 */
@Entity
@Table(name = "exhibition_name_list")
public class ExhibitionNameList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "join_time")
    private ZonedDateTime joinTime;

    @Column(name = "sign_time")
    private ZonedDateTime signTime;

    @ManyToOne
    private WechatUser user;

    @ManyToOne
    private Exhibition exhibiton;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getJoinTime() {
        return joinTime;
    }

    public ExhibitionNameList joinTime(ZonedDateTime joinTime) {
        this.joinTime = joinTime;
        return this;
    }

    public void setJoinTime(ZonedDateTime joinTime) {
        this.joinTime = joinTime;
    }

    public ZonedDateTime getSignTime() {
        return signTime;
    }

    public ExhibitionNameList signTime(ZonedDateTime signTime) {
        this.signTime = signTime;
        return this;
    }

    public void setSignTime(ZonedDateTime signTime) {
        this.signTime = signTime;
    }

    public WechatUser getUser() {
        return user;
    }

    public ExhibitionNameList user(WechatUser wechatUser) {
        this.user = wechatUser;
        return this;
    }

    public void setUser(WechatUser wechatUser) {
        this.user = wechatUser;
    }

    public Exhibition getExhibiton() {
        return exhibiton;
    }

    public ExhibitionNameList exhibiton(Exhibition exhibition) {
        this.exhibiton = exhibition;
        return this;
    }

    public void setExhibiton(Exhibition exhibition) {
        this.exhibiton = exhibition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExhibitionNameList exhibitionNameList = (ExhibitionNameList) o;
        if (exhibitionNameList.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), exhibitionNameList.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExhibitionNameList{" +
            "id=" + getId() +
            ", joinTime='" + getJoinTime() + "'" +
            ", signTime='" + getSignTime() + "'" +
            "}";
    }
}
