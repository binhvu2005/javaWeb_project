package com.data.project_javaWeb.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class ApplicationApplyDTO {
    private MultipartFile cvFile;
    private Integer recruitmentPositionId;
}
