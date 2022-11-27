package org.example;

import org.example.entities.Customer;
import org.example.repository.MovieRentalServiceDB;

import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        MovieRentalServiceDB db = new MovieRentalServiceDB();

        Optional<Customer> byId = db.findById((short) 575);

    }
}