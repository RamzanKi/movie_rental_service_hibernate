package org.example.dao;

import org.example.entities.Language;
import org.hibernate.SessionFactory;

public class LanguageDAO  extends AbstractHibernateDAO<Language>{
    public LanguageDAO(SessionFactory sessionFactory) {
        super(Language.class, sessionFactory);
    }
}
