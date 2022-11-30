package org.example.dao;

import org.example.entities.Address;
import org.hibernate.SessionFactory;

public class AddressDAO  extends AbstractHibernateDAO<Address>{
    public AddressDAO(SessionFactory sessionFactory) {
        super(Address.class, sessionFactory);
    }
}
