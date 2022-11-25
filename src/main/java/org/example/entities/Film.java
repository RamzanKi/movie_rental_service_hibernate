package org.example.entities;

import jakarta.persistence.*;
import jakarta.persistence.metamodel.IdentifiableType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.security.auth.callback.LanguageCallback;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "movie", name = "film")
public class Film {
    @Id
    @Column(name = "film_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(length = 128)
    private String title;

    @Column(columnDefinition = "text")
    @Type(type = "text")
    private String description;

    @Column(name = "release_year", columnDefinition = "year")
    private Year year;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "original_language_id")
    private Language originalLanguage;

    @Column(name = "rental_duration", columnDefinition = "tinyint")
    private Byte rentalDuration;

    @Column(name = "rental_rate", columnDefinition = "decimal")
    private BigDecimal rentalRate;

    @Column(columnDefinition = "smallint")
    private Short length;

    @Column(name = "replacement_cost", columnDefinition = "decimal")
    private BigDecimal replacementCost;

    @Column(name = "rating", columnDefinition = "enum('G', 'PG', 'PG-13', 'R', 'NC-17')")
    private Rating rating;

    @Column(name = "special_features", columnDefinition = "set('Trailers', 'Commentaries', 'Deleted Scenes', 'Behind the Scenes')")
    private String specialFeatures;

    @Column(name = "last_update")
    @UpdateTimestamp
    private Timestamp lastUpdate;

    @ManyToMany
    @JoinTable(name = "film_category",
    joinColumns = @JoinColumn(name = "film_id", referencedColumnName = "film_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "category_id"))
    private Set<Category> categorySet = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "film_category",
            joinColumns = @JoinColumn(name = "film_id", referencedColumnName = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "actor_id"))
    private Set<Actor> actorSet = new HashSet<>();


}
