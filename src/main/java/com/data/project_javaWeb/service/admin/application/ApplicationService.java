package com.data.project_javaWeb.service.admin.application;

import com.data.project_javaWeb.dto.ApplicationDetailDTO;
import com.data.project_javaWeb.entity.Application;
import com.data.project_javaWeb.entity.enums.Progress;
import org.springframework.data.domain.Page;

public interface ApplicationService {
    Application findById(Integer id);
    void save(Application application);
    Page<Application> findApplications(String keyword, Progress progress, int page, int size);

    ApplicationDetailDTO mapToDetailDTO(Application application);

}
