package com.alienlab.ziranli.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * not an ignored comment
 */
@ApiModel(value = "展览")
@Entity
@Table(name = "exhibition")
public class Exhibition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value="展览名")
    @Column(name = "name")
    private String name;

    @ApiModelProperty(value="开始时间")
    @Column(name = "start_date")
    private String startDate;

    @ApiModelProperty(value="结束时间")
    @Column(name = "end_date")
    private String endDate;

    @ApiModelProperty(value="时段说明")
    @Column(name = "time_desc")
    private String timeDesc;

    @ApiModelProperty(value="展览说明")
    @Column(name = "memo")
    private String memo;

    @ApiModelProperty(value="展览图片")
    @Column(name = "cover_image")
    private String coverImage;

    @ApiModelProperty(value="展览二维码")
    @Column(name = "qr_code")
    private String qrCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Exhibition name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public Exhibition startDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Exhibition endDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTimeDesc() {
        return timeDesc;
    }

    public Exhibition timeDesc(String timeDesc) {
        this.timeDesc = timeDesc;
        return this;
    }

    public void setTimeDesc(String timeDesc) {
        this.timeDesc = timeDesc;
    }

    public String getMemo() {
        return memo;
    }

    public Exhibition memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public Exhibition coverImage(String coverImage) {
        this.coverImage = coverImage;
        return this;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getQrCode() {
        return qrCode;
    }

    public Exhibition qrCode(String qrCode) {
        this.qrCode = qrCode;
        return this;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Exhibition exhibition = (Exhibition) o;
        if (exhibition.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), exhibition.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Exhibition{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", timeDesc='" + getTimeDesc() + "'" +
            ", memo='" + getMemo() + "'" +
            ", coverImage='" + getCoverImage() + "'" +
            ", qrCode='" + getQrCode() + "'" +
            "}";
    }
}
