package com.data.project_javaWeb.repository.admin;

import com.data.project_javaWeb.entity.Technology;
import java.util.List;

public interface TechnologyRepository {
    Technology findById(Integer id);
    Technology findByName(String name);
    List<Technology> search(String keyword, int page, int size);

    long count(String keyword);
    void save(Technology tech);
    void update(Technology tech);
    void delete(Technology tech);
}
