package com.data.project_javaWeb.service.user;

import com.data.project_javaWeb.dto.MyApplicationDTO;
import com.data.project_javaWeb.entity.Application;
import com.data.project_javaWeb.repository.user.MyApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class MyApplicationServiceImp implements MyApplicationService {

    @Autowired
    private MyApplicationRepository myApplicationRepository;

    @Override
    public List<MyApplicationDTO> getApplicationsByCandidate(Integer candidateId, int page, int size) {
        List<Application> applications = myApplicationRepository.findByCandidate(candidateId, page, size);
        return myApplicationRepository.mapToApplicationListDTO(applications);
    }

    @Override
    public int getTotalPages(Integer candidateId, int size) {
        return myApplicationRepository.getTotalPages(candidateId, size);
    }
}
