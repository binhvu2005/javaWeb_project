package com.data.project_javaWeb.service.admin.recruitmentPosition;

import com.data.project_javaWeb.dto.RecruitmentPositionDTO;
import com.data.project_javaWeb.entity.RecruitmentPosition;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RecruitmentPositionService {
    Page<RecruitmentPositionDTO> getAll(int page, int size, String keyword);
    RecruitmentPositionDTO findById(Integer id);
    void save(RecruitmentPositionDTO dto);
    void update(RecruitmentPositionDTO dto);
    void delete(Integer id);
    Page<RecruitmentPositionDTO> searchByKeywordAndTech(String keyword, String tech, int page, int size);
    RecruitmentPosition findEntityById(Integer id);
}
