package com.data.project_javaWeb.repository.admin;

import com.data.project_javaWeb.entity.Technology;
import com.data.project_javaWeb.entity.enums.Status;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class TechnologyRepositoryImp implements TechnologyRepository {
    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Technology findById(Integer id) {
        return getSession().get(Technology.class, id);
    }

    @Override
    public Technology findByName(String name) {
        String hql = "FROM Technology t WHERE lower(t.name)=:name";
        return getSession().createQuery(hql, Technology.class)
                .setParameter("name", name.toLowerCase())
                .uniqueResult();
    }

    @Override
    public List<Technology> search(String keyword, int page, int size) {
        String hql = "FROM Technology t WHERE lower(t.name) LIKE :kw ORDER BY t.id";
        return getSession().createQuery(hql, Technology.class)
                .setParameter("kw", "%" + keyword.toLowerCase() + "%")
                .setFirstResult(page * size)
                .setMaxResults(size)
                .list();
    }

    @Override
    public long count(String keyword) {
        String hql = "SELECT count(t.id) FROM Technology t WHERE lower(t.name) LIKE :kw";
        return (Long) getSession().createQuery(hql)
                .setParameter("kw", "%" + keyword.toLowerCase() + "%")
                .uniqueResult();
    }

    @Override
    public void save(Technology tech) {
        getSession().save(tech);
    }

    @Override
    public void update(Technology tech) {
        getSession().update(tech);
    }

    @Override
    public void delete(Technology tech) {
        getSession().delete(tech);
    }
}
