package org.example.dao;

import org.example.entities.Inventory;
import org.example.entities.Staff;
import org.example.entities.Store;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class InventoryDAO  extends AbstractHibernateDAO<Inventory>{
    public InventoryDAO(SessionFactory sessionFactory) {
        super(Inventory.class, sessionFactory);
    }

    public List<Integer> getReservedInventories(Staff staff) {
        Query<Integer> query1 = getCurrentSession().createQuery("select inventory.id from Rental where returnDate = null and staff.id = :st", Integer.class);
        query1.setParameter("st", staff.getId());
        List<Integer> resultList = query1.getResultList();
        return resultList;
    }

    public Inventory getAvailableInventory(String filmTitle, Store store, List<Integer> reservedInventories) {
        Query<Inventory> query = getCurrentSession().createQuery("from Inventory where film.title = :title and store.id = :store and id not in (:id_list)", Inventory.class);
        query.setParameter("title", filmTitle);
        query.setParameter("store", store.getId());
        query.setParameterList("id_list", reservedInventories);
        Inventory inventory = query.setMaxResults(1).uniqueResult();
        return inventory;
    }
}
