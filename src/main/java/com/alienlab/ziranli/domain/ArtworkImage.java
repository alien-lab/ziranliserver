package com.alienlab.ziranli.domain;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ArtworkImage.
 */
@Entity
@Table(name = "artwork_image")
@ApiModel(value = "艺术品图片")
public class ArtworkImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value="图片")
    @Column(name = "image")
    private String image;

    @ApiModelProperty(value="关联艺术品")
    @ManyToOne
    private Artwork artwork;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public ArtworkImage image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Artwork getArtwork() {
        return artwork;
    }

    public ArtworkImage artwork(Artwork artwork) {
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
        ArtworkImage artworkImage = (ArtworkImage) o;
        if (artworkImage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), artworkImage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ArtworkImage{" +
            "id=" + getId() +
            ", image='" + getImage() + "'" +
            "}";
    }
}
