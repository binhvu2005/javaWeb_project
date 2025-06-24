package com.data.project_javaWeb.repository.admin.application;

import com.data.project_javaWeb.entity.Application;
import com.data.project_javaWeb.entity.enums.Progress;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import org.hibernate.query.Query;

import java.util.List;

@Repository
public class ApplicationRepositoryImp implements ApplicationRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Application findById(Integer id) {
        try {
            String hql = "FROM Application a WHERE a.id = :id";
            return sessionFactory.getCurrentSession()
                    .createQuery(hql, Application.class)
                    .setParameter("id", id)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void save(Application application) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(application);
    }

    @Override
    public Page<Application> searchApplications(String keyword, Progress progress, int page, int size) {
        Session session = sessionFactory.getCurrentSession();

        StringBuilder hql = new StringBuilder("FROM Application a WHERE 1=1");
        if (keyword != null && !keyword.trim().isEmpty()) {
            hql.append(" AND (lower(a.candidate.name) LIKE :keyword OR lower(a.recruitmentPosition.name) LIKE :keyword)");
        }
        if (progress != null) {
            hql.append(" AND a.progress = :progress");
        }

        Query<Application> query = session.createQuery(hql.toString(), Application.class);

        if (keyword != null && !keyword.trim().isEmpty()) {
            query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
        }
        if (progress != null) {
            query.setParameter("progress", progress);
        }

        int totalRows = query.getResultList().size();

        query.setFirstResult(page * size);
        query.setMaxResults(size);
        List<Application> applications = query.getResultList();

        return new PageImpl<>(applications, PageRequest.of(page, size), totalRows);
    }
}
