package org.example.dao;

import org.example.entities.Film;
import org.hibernate.SessionFactory;

public class FilmDAO  extends AbstractHibernateDAO<Film>{
    public FilmDAO(SessionFactory sessionFactory) {
        super(Film.class, sessionFactory);
    }
}
