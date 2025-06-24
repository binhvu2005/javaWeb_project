package com.data.project_javaWeb.service.admin.candidate;

import com.data.project_javaWeb.dto.CandidateDTO;
import com.data.project_javaWeb.entity.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CandidateService {
    Page<CandidateDTO> searchCandidates(String keyword, Integer technologyId, String gender, Integer age, Integer experience, Pageable pageable);

    void toggleStatus(int candidateId);
    void update(Candidate candidate);
    Candidate findByEmail(String email);
}
