package org.example.dao;

import org.example.entities.Payment;
import org.hibernate.SessionFactory;

public class PaymentDAO  extends AbstractHibernateDAO<Payment>{
    public PaymentDAO(SessionFactory sessionFactory) {
        super(Payment.class, sessionFactory);
    }
}
