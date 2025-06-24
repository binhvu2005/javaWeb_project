package com.data.project_javaWeb.repository.util.signup;

import com.data.project_javaWeb.entity.Account;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public class SignupRepositoryImp implements SignupRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Account findByEmail(String email) {
        String hql = "FROM Account a WHERE a.email = :email";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Account.class)
                .setParameter("email", email)
                .uniqueResult();
    }

    @Override
    public void save(Account account) {
        sessionFactory.getCurrentSession().save(account);
    }
}