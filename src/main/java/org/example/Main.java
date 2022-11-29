package org.example;

import org.example.entities.Customer;
import org.example.repository.MovieRentalServiceDB;

import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        MovieRentalServiceDB db = new MovieRentalServiceDB();

        Customer customer = db.findById((short) 130).orElse(null);

//        db.createNewCustomer("newMAN", "db", "wqwe", db.getRandomStore(), "Test Street", "Nowhere", "yoyoyoyo", "mamaja", "12345", "88005053535");

        db.returnMovie(Objects.requireNonNull(customer).getId(), 367);

//        Film film = db.addNewFilm("newKino", "good", 2022, (byte) 1, (byte) 7, new BigDecimal("5.99"), (short) 88, new BigDecimal("15.99"), Rating.R, "behind the scenes,trailers", db.getRandomCategorySet(), db.getRandomActorSet());
//        System.out.println(film.getTitle());

//        Store randomStore = db.getRandomStore();
//        db.getFilmInStore(customer, randomStore, 367, randomStore.getStaff());
    }
}