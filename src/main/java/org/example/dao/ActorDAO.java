package org.example.dao;

import org.example.entities.Actor;
import org.hibernate.SessionFactory;

public class ActorDAO extends AbstractHibernateDAO<Actor>{
    public ActorDAO(SessionFactory sessionFactory) {
        super(Actor.class, sessionFactory);
    }
}
