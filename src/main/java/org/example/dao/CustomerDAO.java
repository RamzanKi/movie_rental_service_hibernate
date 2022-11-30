package org.example.dao;

import org.example.entities.Customer;
import org.hibernate.SessionFactory;

public class CustomerDAO  extends AbstractHibernateDAO<Customer>{
    public CustomerDAO(SessionFactory sessionFactory) {
        super(Customer.class, sessionFactory);
    }

    public Customer getById(short id) {
        return getCurrentSession().get(Customer.class, id);
    }
}
