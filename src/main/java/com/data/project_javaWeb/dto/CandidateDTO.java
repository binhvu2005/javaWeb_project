package com.data.project_javaWeb.dto;


import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class CandidateDTO {
    private Integer id;

    @NotBlank(message = "Tên không được để trống")
    @Size(max = 100, message = "Tên không được vượt quá 100 ký tự")
    private String name;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 100)
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{7,20}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @Pattern(regexp = "MALE|FEMALE|OTHER", message = "Giới tính không hợp lệ")
    private String gender;

    private LocalDate dob;

    @Min(value = 0, message = "Số năm kinh nghiệm không được âm")
    private Integer experience;

    private String description;
}
