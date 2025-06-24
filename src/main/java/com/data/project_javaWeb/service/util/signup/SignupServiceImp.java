package com.data.project_javaWeb.service.util.signup;

import com.data.project_javaWeb.dto.AccountDTO;
import com.data.project_javaWeb.entity.Account;
import com.data.project_javaWeb.entity.Candidate;
import com.data.project_javaWeb.entity.enums.AccountStatus;
import com.data.project_javaWeb.entity.enums.Role;
import com.data.project_javaWeb.repository.util.signup.SignupRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class SignupServiceImp implements SignupService {

    @Autowired
    private SignupRepository signupRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public boolean register(AccountDTO dto) {
        if (signupRepository.findByEmail(dto.getEmail()) != null) {
            return false;
        }

        Candidate candidate = new Candidate();
        candidate.setName(dto.getName());
        candidate.setEmail(dto.getEmail());
        sessionFactory.getCurrentSession().save(candidate);
        Account account = new Account();
        account.setEmail(dto.getEmail());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setRole(Role.CANDIDATE); // fixed role
        account.setStatus(AccountStatus.ACTIVE);
        account.setCandidate(candidate);
        candidate.setAccount(account);

        signupRepository.save(account);
        return true;
    }
}