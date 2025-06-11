package com.data.project_javaWeb.entity;

import com.data.project_javaWeb.entity.enums.AccountStatus;
import com.data.project_javaWeb.entity.enums.Role;
import com.data.project_javaWeb.entity.Candidate;

import javax.persistence.*;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "candidateId")
    private Candidate candidate;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;
}
