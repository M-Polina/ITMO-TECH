package com.mpolina.cats.dao;

import com.mpolina.cats.entity.cat.Cat;
import com.mpolina.cats.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

public class CatDao implements Dao<Cat> {
    public Cat findById(Long id) {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Cat cat = session.get(Cat.class, id);
            session.getTransaction().commit();
            return cat;
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Cat> getAll() {
        List<Cat> cats;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            cats = session.createQuery("SELECT a FROM Cat a", Cat.class).getResultList();

            session.getTransaction().commit();
            return cats;
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Cat cat) {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.persist(cat);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Cat cat) {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.merge(cat);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Cat cat) {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            for (Cat c : cat.getFriendCatsList()) {
                c.getFriendCatsList().removeIf(cati -> (cati.getId() == cat.getId()));
                session.merge(c);
            }

            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }

        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            session.remove(cat);

            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }
}