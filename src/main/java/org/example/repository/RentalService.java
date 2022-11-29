package org.example.repository;

import org.example.entities.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

public interface RentalService {

    Customer createNewCustomer(String firstName, String lastName, String email, Boolean isActive, Store store, Address address);

    void returnMovie(Short customerId, Boolean isActive, Integer inventoryId);

    void rentFilm(Short customerId, Boolean isActive, Rental rental, Inventory inventory, Payment payment, Staff staff);

    Film addNewFilm(String title, String description, Integer releaseYear, Byte languageId, Byte rentalDuration, BigDecimal rentalRate, Short length, BigDecimal replacementCost, Rating rating, String specialFeatures, Set<Category> categorySet, Set<Actor> actorSet);
    Optional<Customer> findById(Short id);
}
