package org.example;

import org.example.entities.Customer;
import org.example.entities.Store;
import org.example.repository.MovieRentalServiceDB;

public class Main {

    public static void main(String[] args) {
        MovieRentalServiceDB db = new MovieRentalServiceDB();

        Customer customer = db.findById((short) 130).orElse(null);

//        db.createNewCustomer("heeey", "dbasdw", "wqwe@@", db.getRandomStore(), "Test Street", "Nowhere", "Ukraine", "Kyiv", "12345", "88005053535");

//        db.returnMovie((short)130, 7);

//        Film film = db.addNewFilm("neguyguywKino", "good", 2022, (byte) 1, (byte) 7, new BigDecimal("5.99"), (short) 88, new BigDecimal("15.99"), Rating.R, "behind the scenes,trailers", db.getRandomCategorySet(), db.getRandomActorSet());
//        System.out.println(film.getTitle());

        Store randomStore = db.getRandomStore();
        db.rentFilmInStore(customer, randomStore, randomStore.getStaff(), "ACADEMY DINOSAUR");
    }
}