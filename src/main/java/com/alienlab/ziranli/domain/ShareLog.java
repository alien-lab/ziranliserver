package com.alienlab.ziranli.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ShareLog.
 */
@Entity
@Table(name = "share_log")
public class ShareLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "share_type")
    private String shareType;

    @Column(name = "share_time")
    private ZonedDateTime shareTime;

    @Column(name = "share_content_key")
    private String shareContentKey;

    @Column(name = "share_link")
    private String shareLink;

    @ManyToOne
    private WechatUser user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShareType() {
        return shareType;
    }

    public ShareLog shareType(String shareType) {
        this.shareType = shareType;
        return this;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public ZonedDateTime getShareTime() {
        return shareTime;
    }

    public ShareLog shareTime(ZonedDateTime shareTime) {
        this.shareTime = shareTime;
        return this;
    }

    public void setShareTime(ZonedDateTime shareTime) {
        this.shareTime = shareTime;
    }

    public String getShareContentKey() {
        return shareContentKey;
    }

    public ShareLog shareContentKey(String shareContentKey) {
        this.shareContentKey = shareContentKey;
        return this;
    }

    public void setShareContentKey(String shareContentKey) {
        this.shareContentKey = shareContentKey;
    }

    public String getShareLink() {
        return shareLink;
    }

    public ShareLog shareLink(String shareLink) {
        this.shareLink = shareLink;
        return this;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    public WechatUser getUser() {
        return user;
    }

    public ShareLog user(WechatUser wechatUser) {
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
        ShareLog shareLog = (ShareLog) o;
        if (shareLog.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shareLog.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShareLog{" +
            "id=" + getId() +
            ", shareType='" + getShareType() + "'" +
            ", shareTime='" + getShareTime() + "'" +
            ", shareContentKey='" + getShareContentKey() + "'" +
            ", shareLink='" + getShareLink() + "'" +
            "}";
    }
}
