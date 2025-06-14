package com.data.project_javaWeb.repository.admin.candidate;

import com.data.project_javaWeb.dto.CandidateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CandidateRepository {
    Page<CandidateDTO> searchCandidates(String keyword, Integer technologyId, String gender, Integer age, Integer experience, Pageable pageable);

    void updateAccountStatusByCandidateId(int candidateId);
}
