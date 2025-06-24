package com.data.project_javaWeb.repository.admin.application;

import com.data.project_javaWeb.entity.Application;
import com.data.project_javaWeb.entity.enums.Progress;
import org.springframework.data.domain.Page;

public interface ApplicationRepository {
    Application findById(Integer id);
    void save(Application application);
    Page<Application> searchApplications(String keyword, Progress progress, int page, int size);
}
