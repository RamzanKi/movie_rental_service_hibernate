package org.example.repository;

import org.example.entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class MovieRentalServiceDB implements RentalService{
    private final SessionFactory sessionFactory;

    public MovieRentalServiceDB() {
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


    @Override
    public Customer createNewCustomer(String firstName, String lastName, String email, Boolean isActive, Store store, Address address) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setActive(isActive);
        customer.setStore(store);
        customer.setAddress(address);

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(customer);
            session.getTransaction().commit();
            return customer;
        }
    }

    @Override
    public void returnMovie(Short customerId, Boolean isActive, Integer inventoryId) {
        try (Session session = sessionFactory.openSession()) {
            Customer customer = findById(customerId).orElse(null);
            Objects.requireNonNull(customer).setActive(false);

            session.beginTransaction();
            Query<Rental> query = session.createQuery("from Rental where customer.id = :custId and inventory.id = :invId", Rental.class);
            query.setParameter("custId", customerId);
            query.setParameter("invId", inventoryId);
            Rental rental = query.uniqueResult();
            rental.setReturnDate(LocalDateTime.now());


            Query<Rental> query2 = session.createQuery("from Rental where customer.id = :custId", Rental.class);
            query2.setParameter("custId", customerId);
            List<Rental> list = query2.list();
            for (Rental rent : list) {
                if (rent.getReturnDate() == null) {
                    Objects.requireNonNull(customer).setActive(true);
                }
            }

            session.getTransaction().commit();
        }

    }

    @Override
    public void rentFilm(Short customerId, Boolean isActive, Rental rental, Inventory inventory, Payment payment, Staff staff) {

    }

    @Override
    public Film addNewFilm(String title, String description, Integer releaseYear, Byte languageId, Byte rentalDuration, BigDecimal rentalRate, Short length, BigDecimal replacementCost, Rating rating, String specialFeatures, Set<Category> categorySet, Set<Actor> actorSet) {
        Language lang;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            lang = session.get(Language.class, languageId);

            Film film = new Film();
            film.setTitle(title);
            film.setDescription(description);
            film.setYear(releaseYear);
            film.setLanguage(lang);
            film.setRentalDuration(rentalDuration);
            film.setRentalRate(rentalRate);
            film.setLength(length);
            film.setReplacementCost(replacementCost);
            film.setRating(rating);
            film.setSpecialFeatures(specialFeatures);
            film.setCategorySet(categorySet);
            film.setActorSet(actorSet);

            session.persist(film);
            session.getTransaction().commit();
            return film;
        }
    }

    @Override
    public Optional<Customer> findById(Short id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Customer> customer = Optional.ofNullable(session.get(Customer.class, id));
            session.getTransaction().commit();
            return customer;
        }
    }
}
