package com.data.project_javaWeb.service.admin.technology;

import com.data.project_javaWeb.dto.TechnologyDTO;
import com.data.project_javaWeb.entity.Technology;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TechnologyService {
    List<Technology> getAllTechnologies();
    Page<Technology> list(String keyword, int page, int size);
    TechnologyDTO get(Integer id);
    void saveOrUpdate(TechnologyDTO dto);
    void delete(Integer id);
}
