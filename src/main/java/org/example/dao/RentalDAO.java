package org.example.dao;

import org.example.entities.Rental;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class RentalDAO  extends AbstractHibernateDAO<Rental>{
    public RentalDAO(SessionFactory sessionFactory) {
        super(Rental.class, sessionFactory);
    }

    public Rental getRentalInformation(Short customerId, Integer inventoryId) {
        Query<Rental> query = getCurrentSession().createQuery("from Rental where customer.id = :custId and inventory.id = :invId and returnDate = null", Rental.class);
        query.setParameter("custId", customerId);
        query.setParameter("invId", inventoryId);
        Rental rental = query.setMaxResults(1).uniqueResult();
        return rental;
    }

    public List<Rental> getAllCustomerRentalInfo(Short customerId) {
        Query<Rental> query2 = getCurrentSession().createQuery("from Rental where customer.id = :custId", Rental.class);
        query2.setParameter("custId", customerId);
        List<Rental> list = query2.list();
        return list;
    }
}
