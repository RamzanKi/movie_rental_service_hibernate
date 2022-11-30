package org.example.repository;

import org.example.dao.*;
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

    private final ActorDAO actorDAO;
    private final AddressDAO addressDAO;
    private final CategoryDAO category;
    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;
    private final CustomerDAO customerDAO;
    private final FilmDAO filmDAO;
    private final FilmTextDAO filmTextDAO;
    private final InventoryDAO inventoryDAO;
    private final LanguageDAO languageDAO;
    private final PaymentDAO paymentDAO;
    private final RentalDAO rentalDAO;
    private final StaffDAO staffDAO;
    private final StoreDAO storeDAO;



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

        actorDAO = new ActorDAO(sessionFactory);
        addressDAO = new AddressDAO(sessionFactory);
        category = new CategoryDAO(sessionFactory);
        cityDAO = new CityDAO(sessionFactory);
        countryDAO = new CountryDAO(sessionFactory);
        customerDAO = new CustomerDAO(sessionFactory);
        filmDAO = new FilmDAO(sessionFactory);
        filmTextDAO = new FilmTextDAO(sessionFactory);
        inventoryDAO = new InventoryDAO(sessionFactory);
        languageDAO = new LanguageDAO(sessionFactory);
        paymentDAO = new PaymentDAO(sessionFactory);
        rentalDAO = new RentalDAO(sessionFactory);
        staffDAO = new StaffDAO(sessionFactory);
        storeDAO = new StoreDAO(sessionFactory);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public Address setAddress(String address, String district, String country, String city, String postalcode, String phone) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();

            Address fullAddress = new Address();
            fullAddress.setAddress(address);
            fullAddress.setDistrict(district);

            City cityObj = cityDAO.getCityByNameAndCountry(city, country);
            if (cityObj == null) {
                Country countryObj = countryDAO.getCountryByName(country);
                if (countryObj == null) {
                    countryObj = new Country();
                    countryObj.setCountry(country);
                }
                countryDAO.save(countryObj);
                cityObj = new City();
                cityObj.setCity(city);
                cityObj.setCountry(countryObj);
            }

            cityDAO.save(cityObj);
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

        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            addressDAO.save(fullAddress);
            customerDAO.save(customer);
            session.getTransaction().commit();
            return customer;
        }
    }

    public void returnMovie(Short customerId, Integer inventoryId) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();

            Customer customer = customerDAO.getById(customerId);
            if (customer != null) {
                customer.setActive(false);
            }
            Rental rental = rentalDAO.getRentalInformation(customerId, inventoryId);
            if (rental == null) {
                return;
            }
            rental.setReturnDate(LocalDateTime.now());

            List<Rental> list = rentalDAO.getAllCustomerRentalInfo(customerId);
            for (Rental rent : list) {
                if (rent.getReturnDate() == null) {
                    Objects.requireNonNull(customer).setActive(true);
                }
            }

            session.getTransaction().commit();
        }

    }

    public void rentFilmInStore(Customer customer, Store store, Staff staff, String filmTitle) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();

            List<Integer> reservedInventories = inventoryDAO.getReservedInventories(staff);

            Inventory inventory = inventoryDAO.getAvailableInventory(filmTitle, store, reservedInventories);

            if (inventory == null) {
                System.out.println("товара нет в наличии");
            } else {
                Query<Rental> queryRental = session.createQuery("from Rental where inventory.id = :invId and staff.id = :staff and returnDate != null", Rental.class);
                queryRental.setParameter("invId", inventory.getId());
                queryRental.setParameter("staff", staff.getId());
                Rental rental = queryRental.setMaxResults(1).uniqueResult();

                if (rental == null) {
                    rental = new Rental();
                    rental.setRentalDate(LocalDateTime.now());
                    rental.setCustomer(customer);
                    rental.setInventory(inventory);
                    rental.setStaff(staff);
                    session.save(rental);
                }
                else if (rental.getReturnDate() != null) {
                    rental.setRentalDate(LocalDateTime.now());
                    rental.setReturnDate(null);
                    rental.setCustomer(customer);
                    rental.setInventory(inventory);
                    rental.setStaff(staff);
                    session.saveOrUpdate(rental);
                }
                else {
                    System.out.println("фильм недоступен для аренды");
                }
                BigDecimal cost = filmDAO.getCost(filmTitle);

                Payment payment = new Payment();
                payment.setCustomer(customer);
                payment.setRental(rental);
                payment.setStaff(staff);
                payment.setAmount(cost);
                paymentDAO.save(payment);
            }
            session.getTransaction().commit();
        }
    }

    public Film addNewFilm(String title, String description, Integer releaseYear, Byte languageId, Byte rentalDuration, BigDecimal rentalRate, Short length, BigDecimal replacementCost, Rating rating, String specialFeatures, Set<Category> categorySet, Set<Actor> actorSet) {
        Language lang;
        try (Session session = sessionFactory.getCurrentSession()) {
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

            filmDAO.save(film);
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
