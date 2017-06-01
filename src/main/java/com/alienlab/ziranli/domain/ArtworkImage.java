package com.alienlab.ziranli.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ArtworkImage.
 */
@Entity
@Table(name = "artwork_image")
public class ArtworkImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image")
    private String image;

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
