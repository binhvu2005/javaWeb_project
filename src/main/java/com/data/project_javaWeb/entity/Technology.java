package com.data.project_javaWeb.entity;

import com.data.project_javaWeb.entity.RecruitmentPosition;
import com.data.project_javaWeb.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "technology")
public class Technology {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @ManyToMany(mappedBy = "technologies")
    private Set<Candidate> candidates;

    @ManyToMany(mappedBy = "technologies" ,fetch = FetchType.EAGER)
    private Set<RecruitmentPosition> positions;
}
