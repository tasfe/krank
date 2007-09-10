package org.crank.jpasandbox;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.crank.crud.test.model.Employee;
import org.springframework.transaction.annotation.Transactional;

public class EmployeeDao {
    
    private EntityManagerFactory entityManagerFactory;


    @PersistenceContext
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }
    
    @Transactional
    public void create(Employee employee) {
        entityManagerFactory.createEntityManager().persist( employee );
    }
    
    @Transactional
    public void delete(Employee employee) {
        entityManagerFactory.createEntityManager().remove( employee );
    }
}
