package org.example.dao;

import org.example.entities.Category;
import org.hibernate.SessionFactory;

public class CategoryDAO  extends AbstractHibernateDAO<Category>{
    public CategoryDAO(SessionFactory sessionFactory) {
        super(Category.class, sessionFactory);
    }
}
