package com.data.project_javaWeb.repository.admin.technology;

import com.data.project_javaWeb.entity.Technology;
import java.util.List;

public interface TechnologyRepository {
    List<Technology> getAllTechnologies();
    Technology findById(Integer id);
    Technology findByName(String name);
    List<Technology> search(String keyword, int page, int size);

    long count(String keyword);
    void save(Technology tech);
    void update(Technology tech);
    void delete(Technology tech);
}
