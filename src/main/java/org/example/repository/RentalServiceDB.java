package org.example.repository;

import org.example.entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.query.Query;

import java.util.Properties;

public class RentalServiceDB {

    private final SessionFactory sessionFactory;

    public RentalServiceDB() {
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/movie");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "adminadmin");
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.put(Environment.HBM2DDL_AUTO, "validate");

        this.sessionFactory = new Configuration()
                .addProperties(properties)
                .addAnnotatedClass(Actor.class)
                .addAnnotatedClass(Address.class)
                .addAnnotatedClass(Category.class)
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Film.class)
                .addAnnotatedClass(FilmText.class)
                .addAnnotatedClass(Inventory.class)
                .addAnnotatedClass(Language.class)
                .addAnnotatedClass(Payment.class)
                .addAnnotatedClass(Rental.class)
                .addAnnotatedClass(Staff.class)
                .addAnnotatedClass(Store.class)
                .buildSessionFactory();
    }

    public Customer saveNewCustomer(Customer customer) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(customer);
            session.getTransaction().commit();
            return customer;
        }
    }

    public static void main(String[] args) {
        RentalServiceDB rentalServiceDB = new RentalServiceDB();
        Query<Film> ff;
        try (Session session = rentalServiceDB.sessionFactory.openSession()) {
            ff = session.createQuery("from Film where id = 1", Film.class);
            Film film = ff.uniqueResult();
            String specialFeatures = film.getSpecialFeatures();
            System.out.println(specialFeatures);
        }
    }
}
