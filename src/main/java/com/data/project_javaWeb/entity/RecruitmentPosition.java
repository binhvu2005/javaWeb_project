package com.data.project_javaWeb.entity;

import com.data.project_javaWeb.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "recruitment_position")
public class RecruitmentPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal minSalary;

    private BigDecimal maxSalary;

    private Integer minExperience = 0;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private LocalDate createdDate = LocalDate.now();

    private LocalDate expiredDate;

    @ManyToMany
    @JoinTable(
            name = "recruitment_position_technology",
            joinColumns = @JoinColumn(name = "positionId"),
            inverseJoinColumns = @JoinColumn(name = "technologyId")
    )
    private Set<Technology> technologies;
}
