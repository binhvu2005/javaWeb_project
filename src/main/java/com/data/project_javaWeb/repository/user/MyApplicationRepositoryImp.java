package com.data.project_javaWeb.repository.user;

import com.data.project_javaWeb.dto.MyApplicationDTO;
import com.data.project_javaWeb.entity.Application;
import com.data.project_javaWeb.entity.Technology;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MyApplicationRepositoryImp implements MyApplicationRepository {

    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Application> findByCandidate(Integer candidateId, int page, int size) {
        String hql = "FROM Application a WHERE a.candidate.id = :candidateId ORDER BY a.createdAt DESC";
        TypedQuery<Application> query = currentSession().createQuery(hql, Application.class);
        query.setParameter("candidateId", candidateId);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public int getTotalPages(Integer candidateId, int size) {
        String countHql = "SELECT COUNT(a.id) FROM Application a WHERE a.candidate.id = :candidateId";
        Long totalCount = currentSession()
                .createQuery(countHql, Long.class)
                .setParameter("candidateId", candidateId)
                .getSingleResult();
        return (int) Math.ceil((double) totalCount / size);
    }

    @Override
    public List<MyApplicationDTO> mapToApplicationListDTO(List<Application> apps) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return apps.stream().map(app -> {
            MyApplicationDTO dto = new MyApplicationDTO();
            dto.setId(app.getId());
            dto.setProcess(app.getProgress().toString());
            dto.setCreatedAt(app.getCreatedAt() != null ? app.getCreatedAt().format(formatter) : "");
            dto.setUpdatedAt(app.getUpdatedAt() != null ? app.getUpdatedAt().format(formatter) : "");

            // Set recruitment info
            if (app.getRecruitmentPosition() != null) {
                dto.setRecruitmentId(app.getRecruitmentPosition().getId());
                dto.setRecruitmentName(app.getRecruitmentPosition().getName());
                dto.setTechnologies(
                        app.getRecruitmentPosition().getTechnologies()
                                .stream().map(Technology::getName).collect(Collectors.toList())
                );
            }

            // Set CSS class for progress
            String cssClass;
            switch (app.getProgress()) {
                case APPLIED:
                    cssClass = "bg-warning text-dark";
                    break;
                case INTERVIEWING:
                    cssClass = "bg-info text-dark";
                    break;
                case DONE:
                    cssClass = "bg-success text-white";
                    break;
                case REJECTED:
                    cssClass = "bg-danger text-white";
                    break;
                case WITHDRAWN:
                    cssClass = "bg-secondary text-white";
                    break;
                case HANDLING:
                    cssClass = "bg-primary text-white";
                    break;
                default:
                    cssClass = "bg-light text-dark";
                    break;
            }
            dto.setCssClass(cssClass);

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Application findById(Integer id) {
        return currentSession().get(Application.class, id);
    }


}
