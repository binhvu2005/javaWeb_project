package com.data.project_javaWeb.dto;


import lombok.Data;

import javax.validation.constraints.*;

@Data
public class TechnologyDTO {
    private Integer id;

    @NotBlank(message = "Tên công nghệ không được để trống")
    @Size(max = 100)
    private String name;

    @Pattern(regexp = "ACTIVE|INACTIVE", message = "Trạng thái không hợp lệ")
    private String status;
}
