package org.example;

import org.example.entities.Customer;
import org.example.entities.Film;
import org.example.entities.Rating;
import org.example.entities.Store;
import org.example.repository.MovieRentalServiceDB;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        MovieRentalServiceDB db = new MovieRentalServiceDB();

        Customer customer = db.findById((short) 130).orElse(null);
//
        db.createNewCustomer("heeey", "dbasdw", "wqwe@@", db.getRandomStore(), "Test Street", "Nowhere", "Germany", "Berlin", "12345", "88005053535");

        db.returnMovie((short)130, 11);

        Film film = db.addNewFilm("my film", "good", 2022, (byte) 1, (byte) 7, new BigDecimal("5.99"), (short) 88, new BigDecimal("15.99"), Rating.getRatingByValue("PG-13"), "behind the scenes,trailers", db.getRandomCategorySet(), db.getRandomActorSet());
        System.out.println(film.toString());

        Store randomStore = db.getRandomStore();
        db.rentFilmInStore(customer, randomStore, randomStore.getStaff(), "ACE GOLDFINGER");
    }
}