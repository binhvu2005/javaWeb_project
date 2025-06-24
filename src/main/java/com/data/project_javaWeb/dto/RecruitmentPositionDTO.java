package com.data.project_javaWeb.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class RecruitmentPositionDTO {
    private Integer id;

    @NotBlank(message = "Tên vị trí không được để trống")
    @Size(max = 100)
    private String name;

    private String description;

    @DecimalMin(value = "0.01", message = "Lương tối thiểu phải lớn hơn 0")
    private BigDecimal minSalary;

    @DecimalMin(value = "0.01", message = "Lương tối đa phải lớn hơn 0")
    private BigDecimal maxSalary;

    @Min(value = 0, message = "Kinh nghiệm tối thiểu không được âm")
    private Integer minExperience;

    @Pattern(regexp = "ACTIVE|INACTIVE", message = "Trạng thái không hợp lệ")
    private String status;

    private LocalDate createdDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Ngày hết hạn không được để trống")
    private LocalDate expiredDate;

    @NotEmpty(message = "Danh sách công nghệ không được để trống")
    private List<String> technologies; // Tên công nghệ
    public String getFormattedCreatedDate() {
        return createdDate != null ? createdDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }
    public String getFormattedExpiredDate() {
        return expiredDate != null ? expiredDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }
}
