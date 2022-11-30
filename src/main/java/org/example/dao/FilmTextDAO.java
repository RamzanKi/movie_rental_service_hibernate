package org.example.dao;

import org.example.entities.FilmText;
import org.hibernate.SessionFactory;

public class FilmTextDAO  extends AbstractHibernateDAO<FilmText>{
    public FilmTextDAO(SessionFactory sessionFactory) {
        super(FilmText.class, sessionFactory);
    }
}
