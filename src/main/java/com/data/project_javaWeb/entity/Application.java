package com.data.project_javaWeb.entity;

import com.data.project_javaWeb.entity.enums.InterviewRequestResult;
import com.data.project_javaWeb.entity.enums.InterviewResult;
import com.data.project_javaWeb.entity.enums.Progress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "application")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "candidateId", nullable = false)
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "recruitmentPositionId", nullable = false)
    private RecruitmentPosition recruitmentPosition;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String cvUrl;

    @Enumerated(EnumType.STRING)
    private Progress progress = Progress.APPLIED;

    private LocalDateTime interviewRequestDate;

    @Enumerated(EnumType.STRING)
    private InterviewRequestResult interviewRequestResult = InterviewRequestResult.PENDING;

    @Column(columnDefinition = "TEXT")
    private String interviewLink;

    private LocalDateTime interviewTime;

    @Enumerated(EnumType.STRING)
    private InterviewResult interviewResult = InterviewResult.PENDING;

    @Column(columnDefinition = "TEXT")
    private String interviewResultNote;

    private LocalDateTime destroyAt;

    @Column(columnDefinition = "TEXT")
    private String destroyReason;

    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    private LocalDateTime updatedAt  = LocalDateTime.now();
}
