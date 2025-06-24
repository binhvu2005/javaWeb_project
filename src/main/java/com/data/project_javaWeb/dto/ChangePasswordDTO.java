package com.data.project_javaWeb.dto;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ChangePasswordDTO {

    @NotBlank(message = "Mật khẩu cũ không được để trống")
    private String oldPassword;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    @Size(min = 6, max = 255, message = "Mật khẩu phải từ 6 đến 255 ký tự")
    private String newPassword;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;
    @AssertTrue(message = "Xác nhận mật khẩu không khớp")
    // Kiểm tra confirm khớp với newPassword
    public boolean isConfirmPasswordValid() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }
}
