package org.example.dao;

import org.example.entities.Staff;
import org.hibernate.SessionFactory;

public class StaffDAO  extends AbstractHibernateDAO<Staff>{
    public StaffDAO(SessionFactory sessionFactory) {
        super(Staff.class, sessionFactory);
    }
}
