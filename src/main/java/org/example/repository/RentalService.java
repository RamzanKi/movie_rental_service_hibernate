package org.example.repository;

import org.example.entities.*;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RentalService {

    Customer createNewCustomer(String firstName, String lastName, String email, Boolean isActive, Store store, Address address);

    void returnMovie(Short customerId, Boolean isActive, Integer inventoryId);

    void rentFilm(Short customerId, Boolean isActive, Rental rental, Inventory inventory, Payment payment, Staff staff);

    void addNewFilm(Film film);

    Optional<Customer> findById(Short id);
}
