package com.data.project_javaWeb.dto;


import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class ApplicationDTO {
    private Integer id;

    @NotNull(message = "Ứng viên không được để trống")
    private Integer candidateId;

    @NotNull(message = "Vị trí tuyển dụng không được để trống")
    private Integer recruitmentPositionId;

    @NotBlank(message = "CV không được để trống")
    private String cvUrl;

    @Pattern(regexp = "APPLIED|INTERVIEWING|OFFER|REJECTED|WITHDRAWN", message = "Trạng thái đơn không hợp lệ")
    private String progress;

    private LocalDateTime interviewRequestDate;

    @Pattern(regexp = "ACCEPTED|REJECTED|PENDING", message = "Kết quả phản hồi không hợp lệ")
    private String interviewRequestResult;

    private String interviewLink;

    private LocalDateTime interviewTime;

    @Pattern(regexp = "PASS|FAIL|PENDING", message = "Kết quả phỏng vấn không hợp lệ")
    private String interviewResult;

    private String interviewResultNote;

    private LocalDateTime destroyAt;

    private String destroyReason;
}
