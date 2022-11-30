package org.example.dao;

import org.example.entities.City;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class CityDAO  extends AbstractHibernateDAO<City>{
    public CityDAO(SessionFactory sessionFactory) {
        super(City.class, sessionFactory);
    }

    public City getCityByNameAndCountry(String city, String country) {
        Query<City> query = getCurrentSession().createQuery("from City where city = :city and country.country = :country", City.class);
        query.setParameter("city", city);
        query.setParameter("country", country);
        City cityObj = query.setMaxResults(1).uniqueResult();
        return cityObj;
    }
}
