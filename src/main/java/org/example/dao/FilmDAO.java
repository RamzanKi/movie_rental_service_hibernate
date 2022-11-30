package org.example.dao;

import org.example.entities.Film;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.math.BigDecimal;

public class FilmDAO  extends AbstractHibernateDAO<Film>{
    public FilmDAO(SessionFactory sessionFactory) {
        super(Film.class, sessionFactory);
    }

    public BigDecimal getCost(String filmTitle) {
        Query<BigDecimal> query = getCurrentSession().createQuery("select replacementCost from Film where title = :name", BigDecimal.class);
        query.setParameter("name", filmTitle);
        BigDecimal amount = query.uniqueResult();
        return amount;
    }
}
