package com.data.project_javaWeb.repository.admin.recruitmentPosition;

import com.data.project_javaWeb.dto.RecruitmentPositionDTO;
import com.data.project_javaWeb.entity.RecruitmentPosition;
import com.data.project_javaWeb.entity.Technology;
import com.data.project_javaWeb.entity.enums.Status;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional
public class RecruitmentPositionRepositoryImp implements RecruitmentPositionRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Page<RecruitmentPosition> findAll(int page, int size, String keyword) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM RecruitmentPosition rp WHERE (:kw IS NULL OR rp.name LIKE :kw) AND rp.status = :status";
        Query<RecruitmentPosition> query = session.createQuery(hql, RecruitmentPosition.class);
        query.setParameter("kw", keyword == null || keyword.isBlank() ? null : "%" + keyword + "%");
        query.setParameter("status", Status.ACTIVE);

        int total = query.getResultList().size();
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        List<RecruitmentPosition> results = query.list();
        return new PageImpl<>(results, PageRequest.of(page - 1, size), total);
    }

    @Override
    public RecruitmentPosition findById(Integer id) {
        return sessionFactory.getCurrentSession().get(RecruitmentPosition.class, id);
    }

    @Override
    public void save(RecruitmentPosition position) {
        sessionFactory.getCurrentSession().save(position);
    }

    @Override
    public void update(RecruitmentPosition position) {
        sessionFactory.getCurrentSession().update(position);
    }

    @Override
    public void delete(Integer id) {
        RecruitmentPosition position = findById(id);
        if (position != null) {
            sessionFactory.getCurrentSession().delete(position);
        }
    }

    @Override
    public Page<RecruitmentPosition> searchByKeywordAndTech(String keyword, String tech, int page, int size) {
        Session session = sessionFactory.getCurrentSession();

        StringBuilder hql = new StringBuilder(
                "SELECT DISTINCT p FROM RecruitmentPosition p " +
                        "LEFT JOIN p.technologies t " +
                        "WHERE p.status = :status"
        );

        if (keyword != null && !keyword.trim().isEmpty()) {
            hql.append(" AND LOWER(p.name) LIKE :keyword");
        }

        if (tech != null && !tech.trim().isEmpty()) {
            hql.append(" AND LOWER(t.name) = :tech");
        }

        StringBuilder countHql = new StringBuilder(
                "SELECT COUNT(DISTINCT p.id) FROM RecruitmentPosition p LEFT JOIN p.technologies t WHERE p.status = :status"
        );
        if (keyword != null && !keyword.trim().isEmpty()) {
            countHql.append(" AND LOWER(p.name) LIKE :keyword");
        }
        if (tech != null && !tech.trim().isEmpty()) {
            countHql.append(" AND LOWER(t.name) = :tech");
        }

        Query<RecruitmentPosition> query = session.createQuery(hql.toString(), RecruitmentPosition.class);
        Query<Long> countQuery = session.createQuery(countHql.toString(), Long.class);

        query.setParameter("status", Status.ACTIVE);
        countQuery.setParameter("status", Status.ACTIVE);

        if (keyword != null && !keyword.trim().isEmpty()) {
            query.setParameter("keyword", "%" + keyword.trim().toLowerCase() + "%");
            countQuery.setParameter("keyword", "%" + keyword.trim().toLowerCase() + "%");
        }

        if (tech != null && !tech.trim().isEmpty()) {
            query.setParameter("tech", tech.trim().toLowerCase());
            countQuery.setParameter("tech", tech.trim().toLowerCase());
        }

        Long total = countQuery.uniqueResult();


        int offset = page * size;
        query.setFirstResult(offset);
        query.setMaxResults(size);

        List<RecruitmentPosition> pagedResult = query.list();


        return new PageImpl<>(pagedResult, PageRequest.of(page, size), total);
    }
    @Override
    public RecruitmentPosition findEntityById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(RecruitmentPosition.class, id);
    }
}
