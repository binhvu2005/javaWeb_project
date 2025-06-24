package com.data.project_javaWeb.dto;

import com.data.project_javaWeb.entity.Application;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.data.project_javaWeb.entity.enums.Progress.*;

@Data
@AllArgsConstructor
public class ApplicationListDTO {
    private Application application;
    private String createdAtFormatted;
    private String updatedAtFormatted;
    String cssClass;



}
