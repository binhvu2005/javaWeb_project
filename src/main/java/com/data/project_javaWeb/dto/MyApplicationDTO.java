package com.data.project_javaWeb.dto;

import lombok.Data;

import java.util.List;

@Data
public class MyApplicationDTO {
    private Integer id;
    private String recruitmentName;
    private Integer recruitmentId;
    private List<String> technologies;
    private String createdAt;
    private String updatedAt;
    private String process;
    private String cssClass;
}
