package org.example;

import org.example.entities.Customer;
import org.example.entities.Film;
import org.example.entities.Rating;
import org.example.repository.MovieRentalServiceDB;
import org.example.repository.RentalService;

import java.math.BigDecimal;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        RentalService db = new MovieRentalServiceDB();

        Customer customer = db.findById((short) 575).orElse(null);

        db.createNewCustomer("newMAN", "db", "wqwe", false, null , null);

        db.returnMovie(Objects.requireNonNull(customer).getId(), true, 830);

        Film film = db.addNewFilm("testtest", "good", 2002, (byte) 1, (byte) 7, new BigDecimal("5.99"), (short) 88, new BigDecimal("15.99"), Rating.R, "behind the scenes,trailers", null, null);
        System.out.println(film.getTitle());
    }
}