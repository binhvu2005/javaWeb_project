package com.data.project_javaWeb.repository.util.login;

import com.data.project_javaWeb.entity.Account;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class LoginRepositoryImp implements LoginRepository {

    private final SessionFactory sessionFactory;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public LoginRepositoryImp(SessionFactory sessionFactory,
                              BCryptPasswordEncoder passwordEncoder) {
        this.sessionFactory = sessionFactory;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Account findByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        Query<Account> query = session.createQuery(
                "FROM Account WHERE email = :email", Account.class);
        query.setParameter("email", email);
        return query.uniqueResult();
    }

    @Override
    public boolean validatePassword(String rawPassword, String storedPassword) {
        return passwordEncoder.matches(rawPassword, storedPassword);
    }

    @Override
    public void updateLoginStatus(String email, boolean status) {
        Session session = sessionFactory.getCurrentSession();
        Query<?> query = session.createQuery(
                "UPDATE Account SET status = :status WHERE email = :email");
        query.setParameter("status", status ? "ACTIVE" : "INACTIVE");
        query.setParameter("email", email);
        query.executeUpdate();
    }

    @Override
    public void saveLoginAttempt(String email, boolean success) {
        Session session = sessionFactory.getCurrentSession();
        // Thực hiện lưu log đăng nhập vào bảng riêng
        // Ví dụ: LoginAttempt attempt = new LoginAttempt(email, success);
        // session.save(attempt);
    }

    @Override
    public void update(Account account) {
        Session session = sessionFactory.getCurrentSession();
        session.update(account);
    }
}
