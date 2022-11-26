package org.example.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;

@Entity
@Table(schema = "movie", name = "film_text")
public class FilmText {
    @Id
    @Column(name = "film_id", columnDefinition = "smallint")
    private Short id;

    @OneToOne
    @JoinColumn(name = "film_id")
    private Film film;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "text")
    @Type(type = "text")
    private String description;

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
