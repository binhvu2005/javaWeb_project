package com.data.project_javaWeb.repository.user;

import com.data.project_javaWeb.dto.MyApplicationDTO;
import com.data.project_javaWeb.entity.Application;

import java.util.List;

public interface MyApplicationRepository {
    List<Application> findByCandidate(Integer candidateId, int page, int size);
    int getTotalPages(Integer candidateId, int size);
    List<MyApplicationDTO> mapToApplicationListDTO(List<Application> apps);
    Application findById(Integer id);

}
