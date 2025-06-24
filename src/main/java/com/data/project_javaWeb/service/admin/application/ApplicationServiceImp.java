package com.data.project_javaWeb.service.admin.application;

import com.data.project_javaWeb.entity.Application;
import com.data.project_javaWeb.entity.Technology;
import com.data.project_javaWeb.entity.enums.InterviewResult;
import com.data.project_javaWeb.entity.enums.Progress;
import com.data.project_javaWeb.repository.admin.application.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.data.project_javaWeb.dto.ApplicationDetailDTO;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApplicationServiceImp implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public Application findById(Integer id) {
        return applicationRepository.findById(id);
    }

    @Override
    public void save(Application application) {
        applicationRepository.save(application);
    }

    @Override
    public Page<Application> findApplications(String keyword, Progress progress, int page, int size) {
        return applicationRepository.searchApplications(keyword, progress, page, size);
    }
    @Override
    public ApplicationDetailDTO mapToDetailDTO(Application application) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        ApplicationDetailDTO dto = new ApplicationDetailDTO();
        dto.setId(application.getId());
        dto.setProgress(application.getProgress().toString());
        dto.setCvUrl(application.getCvUrl());

        // Thời gian
        if (application.getCreatedAt() != null)
            dto.setCreatedAt(application.getCreatedAt().format(formatter));
        if (application.getUpdatedAt() != null)
            dto.setUpdatedAt(application.getUpdatedAt().format(formatter));
        if (application.getDestroyAt() != null)
            dto.setDestroyAt(application.getDestroyAt().format(formatter));
        if (application.getInterviewTime() != null)
            dto.setInterviewTime(application.getInterviewTime().format(formatter));

        // Phỏng vấn
        dto.setInterviewLink(application.getInterviewLink());
        if (application.getInterviewResult() != null)
            dto.setInterviewResult(InterviewResult.valueOf(application.getInterviewResult().toString()));
        dto.setInterviewResultNote(application.getInterviewResultNote());

        // Lý do hủy
        dto.setDestroyReason(application.getDestroyReason());

        // Candidate
        if (application.getCandidate() != null) {
            dto.setCandidateName(application.getCandidate().getName());
            dto.setCandidateEmail(application.getCandidate().getEmail());
            dto.setCandidatePhone(application.getCandidate().getPhone());
            dto.setCandidateGender(application.getCandidate().getGender().toString());

            if (application.getCandidate().getDob() != null)
                dto.setCandidateDob(application.getCandidate().getDob().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            dto.setCandidateExperience(application.getCandidate().getExperience());
            dto.setCandidateDescription(application.getCandidate().getDescription());
            dto.setCandidateTechnologies(application.getCandidate().getTechnologies()
                    .stream()
                    .map(Technology::getName)
                    .collect(Collectors.toList()));
        }

        // Recruitment Position
        if (application.getRecruitmentPosition() != null) {
            dto.setRecruitmentName(application.getRecruitmentPosition().getName());
            dto.setRecruitmentDescription(application.getRecruitmentPosition().getDescription());
            dto.setRecruitmentMinSalary(application.getRecruitmentPosition().getMinSalary());
            dto.setRecruitmentMaxSalary(application.getRecruitmentPosition().getMaxSalary());
            dto.setRecruitmentMinExperience(application.getRecruitmentPosition().getMinExperience());

            if (application.getRecruitmentPosition().getExpiredDate() != null)
                dto.setRecruitmentExpiredDate(application.getRecruitmentPosition().getExpiredDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            dto.setRecruitmentTechnologies(application.getRecruitmentPosition().getTechnologies()
                    .stream()
                    .map(Technology::getName)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

}
