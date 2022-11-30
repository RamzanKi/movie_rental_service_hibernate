package org.example.dao;

import org.example.entities.Country;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class CountryDAO  extends AbstractHibernateDAO<Country>{
    public CountryDAO(SessionFactory sessionFactory) {
        super(Country.class, sessionFactory);
    }

    public Country getCountryByName(String country) {
        Query<Country> countryQuery = getCurrentSession().createQuery("from Country where country = :country", Country.class);
        countryQuery.setParameter("country", country);
        Country countryObj = countryQuery.uniqueResult();
        return countryObj;
    }
}
