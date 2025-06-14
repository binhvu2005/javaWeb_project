package com.data.project_javaWeb.dto;


import com.data.project_javaWeb.entity.enums.AccountStatus;
import com.data.project_javaWeb.entity.enums.Status;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class AccountDTO {
    private Integer id;
    private Integer candidateId;

    @NotBlank(message = "Email không được để trống")
    @Email
    @Size(max = 100)
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 255, message = "Mật khẩu phải từ 6 đến 255 ký tự")
    private String password;


    private String role;


    private AccountStatus status;
}
