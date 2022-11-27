package org.example;

import org.example.entities.Customer;
import org.example.repository.MovieRentalServiceDB;

public class Main {

    public static void main(String[] args) {
        MovieRentalServiceDB db = new MovieRentalServiceDB();

        Customer customer = db.findById((short) 575).orElse(null);
        System.out.println(customer.getAddress().toString());

    }
}