package com.alienlab.ziranli.domain;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ExhibitionArtwork.
 */
@Entity
@Table(name = "exhibition_artwork")
@ApiModel(value = "展览关联艺术品")
public class ExhibitionArtwork implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value="数量")
    @Column(name = "amount")
    private Integer amount;

    @ApiModelProperty(value="售价")
    @Column(name = "price")
    private Float price;

    @ApiModelProperty(value="状态")
    @Column(name = "status")
    private String status;

    @ApiModelProperty(value="关联展览")
    @ManyToOne
    private Exhibition exhibition;

    @ApiModelProperty(value="关联艺术品")
    @ManyToOne
    private Artwork artwork;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public ExhibitionArtwork amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Float getPrice() {
        return price;
    }

    public ExhibitionArtwork price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public ExhibitionArtwork status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Exhibition getExhibition() {
        return exhibition;
    }

    public ExhibitionArtwork exhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
        return this;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    public Artwork getArtwork() {
        return artwork;
    }

    public ExhibitionArtwork artwork(Artwork artwork) {
        this.artwork = artwork;
        return this;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExhibitionArtwork exhibitionArtwork = (ExhibitionArtwork) o;
        if (exhibitionArtwork.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), exhibitionArtwork.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExhibitionArtwork{" +
            "id=" + getId() +
            ", amount='" + getAmount() + "'" +
            ", price='" + getPrice() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
