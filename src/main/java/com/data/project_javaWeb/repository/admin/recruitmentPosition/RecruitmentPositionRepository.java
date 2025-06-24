package com.data.project_javaWeb.repository.admin.recruitmentPosition;

import com.data.project_javaWeb.dto.RecruitmentPositionDTO;
import com.data.project_javaWeb.entity.RecruitmentPosition;
import com.data.project_javaWeb.entity.Technology;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface RecruitmentPositionRepository {
    Page<RecruitmentPosition> findAll(int page, int size, String keyword);
    RecruitmentPosition findById(Integer id);
    void save(RecruitmentPosition position);
    void update(RecruitmentPosition position);
    void delete(Integer id);
    Page<RecruitmentPosition> searchByKeywordAndTech(String keyword, String tech, int page, int size);
    RecruitmentPosition findEntityById(Integer id);

}
