package org.example.dao;

import org.example.entities.Store;
import org.hibernate.SessionFactory;

public class StoreDAO  extends AbstractHibernateDAO<Store>{
    public StoreDAO(SessionFactory sessionFactory) {
        super(Store.class, sessionFactory);
    }
}
