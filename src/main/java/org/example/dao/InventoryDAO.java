package org.example.dao;

import org.example.entities.Inventory;
import org.hibernate.SessionFactory;

public class InventoryDAO  extends AbstractHibernateDAO<Inventory>{
    public InventoryDAO(SessionFactory sessionFactory) {
        super(Inventory.class, sessionFactory);
    }
}
