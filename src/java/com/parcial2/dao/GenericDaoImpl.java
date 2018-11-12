/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.parcial2.dao;

import com.parcial2.util.HibernateUtil;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.hibernate.criterion.Projections;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ThreadLocalSessionContext;
import org.hibernate.metamodel.source.annotations.entity.EntityClass;
/**
 * @author jayson   
 * @param <T>
 * @param <E>
 */
public class GenericDaoImpl<T, E extends Serializable> implements GenericDao<T, E> {
    private final Class<T> clazz;
    protected Session session;
    protected Transaction tx;

    /**
     *
     */
    @SuppressWarnings("unchecked")
    
    public GenericDaoImpl() {
        this.clazz = (Class<T>)((ParameterizedType)this.getClass().getGenericSuperclass())
                            .getActualTypeArguments()[0];
        
        this.session = HibernateUtil.getSessionFactory().openSession();
        ThreadLocalSessionContext.bind(session);
    }
    
    @Override
    public E create(T entity) {
        E id;
        try {
            startOperation();
            id = (E) session.save(entity);
            tx.commit();
        } catch (HibernateException e) {
            id = null;
            tx.rollback();
            throw e;
        }       
        return  id;
    }
    
    @Override
    public void delete(T entity) {
        try {
            startOperation();
            session.delete(entity);
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            throw e;
        }  
        
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void update(T entity) {
        try {
            startOperation();
            session.merge(entity);
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            throw e;
        } 

        //HibernateUtil.getSession().saveOrUpdate(entity);
        //entity = (T) HibernateUtil.getSession().merge(entity);
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() {
        List <T> list;
        try {
            startOperation();
            list = session.createCriteria(clazz).list();
            tx.commit();
        } catch (HibernateException e) {
            list = null ;
            tx.rollback();
            throw e;
        } 
        
        return list;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findList(int pageNo, int pageSize) {
        List <T> list;
        try {
            startOperation();
            list = session.createCriteria(clazz)
                          .setFirstResult((pageNo - 1) * pageSize)
                          .setMaxResults(pageSize)
                          .list();
            tx.commit();
        } catch (HibernateException e) {
            list = null ;
            tx.rollback();
            throw e;
        }  

        return list;
    }
    @Override
    public int getCountOfAll() {
        Long count;
        try {
            startOperation();
            count = (Long) HibernateUtil.getSession()
                                           .createCriteria(clazz)
                                           .setProjection(Projections.rowCount())
                                           .uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            count = null;
            tx.rollback();
            throw e;
        } 
        
        if (null == count) {
            return 0;
        } else {
            return count.intValue();
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public T findById(Serializable id) {
        T obj;
        try {
            startOperation();
            obj = (T) session.load(clazz, id);
            tx.commit();
        } catch (HibernateException e) {
            obj = null;
            tx.rollback();
            throw e;
        }        
        return  obj;
    }

    protected void startOperation() throws HibernateException {
        session = HibernateUtil.getSession();
        tx = session.beginTransaction();
    }
    
    @Override
    public List<T> findByCriteria(String whereCondicion){        
        try{
            startOperation();
            session = HibernateUtil.getSession();
            String where = ((whereCondicion == null) ||
                    (whereCondicion.length() == 0)) ? "" :("where " +
                    whereCondicion);
            final String queryString="select model from "
                    + clazz.getName() +" model "+where;
            List<T> list=session.createQuery(queryString).list();
            tx.commit();
            return list;
        }catch(Exception ex){
            tx.rollback();
            throw ex;
        }        
    }
}