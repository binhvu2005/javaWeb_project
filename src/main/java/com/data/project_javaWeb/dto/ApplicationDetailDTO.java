package com.data.project_javaWeb.dto;

import com.data.project_javaWeb.entity.enums.InterviewResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApplicationDetailDTO {
    private Integer id;
    private String progress;
    private String cvUrl;
    private String createdAt;
    private String updatedAt;
    private String interviewTime;
    private String destroyAt;

    private InterviewResult interviewResult;
    private String interviewLink;
    private String interviewResultNote;
    private String destroyReason;


    // Candidate
    private String candidateName;
    private String candidateEmail;
    private String candidatePhone;
    private String candidateGender;
    private String candidateDob;
    private Integer candidateExperience;
    private String candidateDescription;
    private List<String> candidateTechnologies;

    // Recruitment
    private String recruitmentName;
    private String recruitmentDescription;
    private BigDecimal recruitmentMinSalary;
    private BigDecimal recruitmentMaxSalary;
    private Integer recruitmentMinExperience;
    private String recruitmentExpiredDate;
    private List<String> recruitmentTechnologies;
}
