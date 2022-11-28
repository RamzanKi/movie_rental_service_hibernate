package org.example;

import org.example.entities.Customer;
import org.example.repository.MovieRentalServiceDB;
import org.example.repository.RentalService;

public class Main {

    public static void main(String[] args) {
        RentalService db = new MovieRentalServiceDB();

        Customer customer = db.findById((short) 575).orElse(null);

        db.returnMovie(customer.getId(), true, 830);
    }
}