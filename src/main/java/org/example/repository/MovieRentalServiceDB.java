package org.example.repository;

import org.example.entities.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class MovieRentalServiceDB {
    public final SessionFactory sessionFactory;

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

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public Address setAddress(String address, String district, String country, String city, String postalcode, String phone) {
        Address fullAddress = new Address();
        fullAddress.setAddress(address);
        fullAddress.setDistrict(district);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<City> query = session.createQuery("from City where city = :city", City.class);
            query.setParameter("city", city);
            City cityObj;
            try {
                cityObj = query.uniqueResult();
            } catch (Exception e) {
                cityObj = null;
            }
            if (cityObj == null) {
                Query<Country> countryQuery = session.createQuery("from Country where country = :country", Country.class);
                countryQuery.setParameter("country", country);
                Country countryObj = countryQuery.uniqueResult();
                if (countryObj == null) {
                    countryObj = new Country();
                    countryObj.setCountry(country);
                }
                session.saveOrUpdate(countryObj);
                cityObj = new City();
                cityObj.setCity(city);
                cityObj.setCountry(countryObj);
            }
            session.saveOrUpdate(cityObj);
            fullAddress.setCity(cityObj);
            fullAddress.setPostalCode(postalcode);
            fullAddress.setPhone(phone);
            session.getTransaction().commit();
            return fullAddress;
        }
    }


    public Customer createNewCustomer(String firstName, String lastName, String email, Store store, String address, String district, String country, String city, String postalcode, String phone) {

        Address fullAddress = setAddress(address, district, country, city, postalcode, phone);

        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setActive(true);
        customer.setStore(store);
        customer.setAddress(fullAddress);

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(fullAddress);
            session.persist(customer);
            session.getTransaction().commit();
            return customer;
        }
    }

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

    public void rentFilm(Short customerId, Boolean isActive, Rental rental, Inventory inventory, Payment payment, Staff staff) {

    }

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

    public Optional<Customer> findById(Short id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Customer> customer = Optional.ofNullable(session.get(Customer.class, id));
            session.getTransaction().commit();
            return customer;
        }
    }

    public Store getRandomStore() {
        try (Session session = sessionFactory.openSession()) {
            Criteria criteria = session.createCriteria(Store.class);
            criteria.add(Restrictions.sqlRestriction("1=1 order by rand()"));
            criteria.setMaxResults(1);
            return (Store) criteria.uniqueResult();
        }
    }

    public Address getRandomAddress() {
        try (Session session = sessionFactory.openSession()) {
            Criteria criteria = session.createCriteria(Address.class);
            criteria.add(Restrictions.sqlRestriction("1=1 order by rand()"));
            criteria.setMaxResults(1);
            return (Address) criteria.uniqueResult();
        }
    }

    public Set<Actor> getRandomActorSet() {
        try (Session session = sessionFactory.openSession()) {
            Criteria criteria = session.createCriteria(Actor.class);
            criteria.add(Restrictions.sqlRestriction("1=1 order by rand()"));
            criteria.setMaxResults(5);
            return new HashSet<>(criteria.list());
        }
    }

    public Set<Category> getRandomCategorySet() {
        try (Session session = sessionFactory.openSession()) {
            Criteria criteria2 = session.createCriteria(Category.class);
            criteria2.add(Restrictions.sqlRestriction("1=1 order by rand()"));
            criteria2.setMaxResults(3);
            return new HashSet<>(criteria2.list());
        }
    }
}
