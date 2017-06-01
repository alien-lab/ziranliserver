package com.alienlab.ziranli.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Artwork.
 */
@Entity
@Table(name = "artwork")
public class Artwork implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "jhi_year")
    private String year;

    @Column(name = "author")
    private String author;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "price")
    private Float price;

    @Column(name = "memo")
    private String memo;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "status")
    private String status;

    @Column(name = "tags")
    private String tags;

    @Column(name = "qr_code")
    private String qrCode;

    @OneToMany(mappedBy = "artwork")
    @JsonIgnore
    private Set<ArtworkImage> images = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Artwork name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public Artwork year(String year) {
        this.year = year;
        return this;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAuthor() {
        return author;
    }

    public Artwork author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getAmount() {
        return amount;
    }

    public Artwork amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Float getPrice() {
        return price;
    }

    public Artwork price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getMemo() {
        return memo;
    }

    public Artwork memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public Artwork coverImage(String coverImage) {
        this.coverImage = coverImage;
        return this;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getStatus() {
        return status;
    }

    public Artwork status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTags() {
        return tags;
    }

    public Artwork tags(String tags) {
        this.tags = tags;
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getQrCode() {
        return qrCode;
    }

    public Artwork qrCode(String qrCode) {
        this.qrCode = qrCode;
        return this;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Set<ArtworkImage> getImages() {
        return images;
    }

    public Artwork images(Set<ArtworkImage> artworkImages) {
        this.images = artworkImages;
        return this;
    }

    public Artwork addImages(ArtworkImage artworkImage) {
        this.images.add(artworkImage);
        artworkImage.setArtwork(this);
        return this;
    }

    public Artwork removeImages(ArtworkImage artworkImage) {
        this.images.remove(artworkImage);
        artworkImage.setArtwork(null);
        return this;
    }

    public void setImages(Set<ArtworkImage> artworkImages) {
        this.images = artworkImages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Artwork artwork = (Artwork) o;
        if (artwork.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), artwork.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Artwork{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", year='" + getYear() + "'" +
            ", author='" + getAuthor() + "'" +
            ", amount='" + getAmount() + "'" +
            ", price='" + getPrice() + "'" +
            ", memo='" + getMemo() + "'" +
            ", coverImage='" + getCoverImage() + "'" +
            ", status='" + getStatus() + "'" +
            ", tags='" + getTags() + "'" +
            ", qrCode='" + getQrCode() + "'" +
            "}";
    }
}
