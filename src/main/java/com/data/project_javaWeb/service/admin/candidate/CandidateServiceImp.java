package com.data.project_javaWeb.service.admin.candidate;

import com.data.project_javaWeb.dto.CandidateDTO;
import com.data.project_javaWeb.entity.Candidate;
import com.data.project_javaWeb.repository.admin.candidate.CandidateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CandidateServiceImp implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public Page<CandidateDTO> searchCandidates(String keyword, Integer techId, String gender, Integer age, Integer exp, Pageable pageable) {
        return candidateRepository.searchCandidates(keyword, techId, gender, age, exp, pageable);
    }

    @Override
    public void toggleStatus(int candidateId) {
        candidateRepository.updateAccountStatusByCandidateId(candidateId);
    }

    @Override
    public void update(Candidate candidate) {
        candidateRepository.update(candidate);
    }

    @Override
    public Candidate findByEmail(String email) {
        return candidateRepository.findByEmail(email);
    }
}
