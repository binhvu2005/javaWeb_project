package com.data.project_javaWeb.service.user;

import com.data.project_javaWeb.dto.MyApplicationDTO;

import java.util.List;

public interface MyApplicationService {
    List<MyApplicationDTO> getApplicationsByCandidate(Integer candidateId, int page, int size);
    int getTotalPages(Integer candidateId, int size);
}
