package com.alienlab.ziranli.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
@ApiModel(value = "艺术品")
public class Artwork implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "艺术品名称")
    @Column(name = "name")
    private String name;

    @ApiModelProperty(value = "艺术品年份")
    @Column(name = "jhi_year")
    private String year;

    @ApiModelProperty(value = "艺术品作者")
    @Column(name = "author")
    private String author;

    @ApiModelProperty(value = "艺术品数量")
    @Column(name = "amount")
    private Integer amount;

    @ApiModelProperty(value = "艺术品价格")
    @Column(name = "price")
    private Float price;

    @ApiModelProperty(value = "艺术品介绍")
    @Column(name = "memo")
    private String memo;

    @ApiModelProperty(value = "艺术品图片")
    @Column(name = "cover_image")
    private String coverImage;

    @ApiModelProperty(value = "艺术品状态")
    @Column(name = "status")
    private String status;

    @ApiModelProperty(value = "艺术品类型")
    @Column(name = "artworkType")
    private String artworkType;

    @ApiModelProperty(value = "艺术品材质")
    @Column(name = "material")
    private String material;

    @ApiModelProperty(value = "艺术品尺寸")
    @Column(name = "size")
    private String size;

    @ApiModelProperty(value = "艺术品标签")
    @Column(name = "tags")
    private String tags;

    @ApiModelProperty(value = "艺术品二维码")
    @Column(name = "qr_code")
    private String qrCode;

    @ApiModelProperty(value = "关联图片")
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

    public String getArtworkType() {
        return artworkType;
    }

    public void setArtworkType(String artworkType) {
        this.artworkType = artworkType;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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
            "id=" + id +
            ", name='" + name + '\'' +
            ", year='" + year + '\'' +
            ", author='" + author + '\'' +
            ", amount=" + amount +
            ", price=" + price +
            ", memo='" + memo + '\'' +
            ", coverImage='" + coverImage + '\'' +
            ", status='" + status + '\'' +
            ", artworkType='" + artworkType + '\'' +
            ", material='" + material + '\'' +
            ", size='" + size + '\'' +
            ", tags='" + tags + '\'' +
            ", qrCode='" + qrCode + '\'' +
            ", images=" + images +
            '}';
    }
}
