package com.mpolina.cats.dao;

import com.mpolina.cats.entity.cat.Cat;
import com.mpolina.cats.entity.owner.Owner;
import com.mpolina.cats.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

public class OwnerDao implements Dao<Owner> {
    public Owner findById(Long id) {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Owner owner = session.get(Owner.class, id);
            session.getTransaction().commit();
            return owner;
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Owner> getAll() {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            List<Owner> owners = session.createQuery("SELECT a FROM Owner a", Owner.class).getResultList();

            session.getTransaction().commit();
            return owners;
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Owner owner) {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.persist(owner);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Owner owner) {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.merge(owner);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Owner owner) {
        try {
            CatDao catDao = new CatDao();
            for (Cat cat : owner.getCatsList()) {
                catDao.delete(cat);
            }

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            session.remove(owner);

            session.getTransaction().commit();

        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }
}
