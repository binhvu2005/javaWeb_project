package com.data.project_javaWeb.repository.admin.candidate;

import com.data.project_javaWeb.dto.CandidateDTO;
import com.data.project_javaWeb.entity.Candidate;
import com.data.project_javaWeb.entity.enums.Gender;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CandidateRepositoryImp implements CandidateRepository {

    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Page<CandidateDTO> searchCandidates(String keyword, Integer techId, String gender, Integer age, Integer exp, Pageable pageable) {
        StringBuilder hql = new StringBuilder("SELECT DISTINCT c FROM Candidate c LEFT JOIN c.technologies t WHERE 1=1");

        if (keyword != null && !keyword.trim().isEmpty()) {
            hql.append(" AND (c.name LIKE :kw OR c.email LIKE :kw)");
        }
        if (techId != null) {
            hql.append(" AND t.id = :techId");
        }
        if (gender != null && !gender.trim().isEmpty()) {
            hql.append(" AND c.gender = :gender");
        }
        if (age != null) {
            hql.append(" AND YEAR(CURRENT_DATE) - YEAR(c.dob) >= :age");
        }
        if (exp != null) {
            hql.append(" AND c.experience >= :exp");
        }

        TypedQuery<Candidate> query = currentSession().createQuery(hql.toString(), Candidate.class);

        if (keyword != null && !keyword.trim().isEmpty()) {
            query.setParameter("kw", "%" + keyword + "%");
        }
        if (techId != null) {
            query.setParameter("techId", techId);
        }
        if (gender != null && !gender.trim().isEmpty()) {
            try {
                query.setParameter("gender", Gender.valueOf(gender.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Giới tính không hợp lệ: " + gender);
            }
        }
        if (age != null) {
            query.setParameter("age", age);
        }
        if (exp != null) {
            query.setParameter("exp", exp);
        }

        int total = query.getResultList().size();

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<CandidateDTO> content = query.getResultList().stream()
                .map(CandidateDTO::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public void updateAccountStatusByCandidateId(int candidateId) {
        String hql = "UPDATE Account SET status = CASE WHEN status = 'ACTIVE' THEN 'INACTIVE' ELSE 'ACTIVE' END WHERE candidate.id = :cid";
        currentSession().createQuery(hql).setParameter("cid", candidateId).executeUpdate();
    }
}
