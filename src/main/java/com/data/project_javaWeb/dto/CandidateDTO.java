package com.data.project_javaWeb.dto;

import com.data.project_javaWeb.entity.Account;
import com.data.project_javaWeb.entity.Candidate;
import com.data.project_javaWeb.entity.Technology;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CandidateDTO {

    private Integer id;

    @NotBlank(message = "Tên không được để trống")
    @Size(max = 100, message = "Tên không vượt quá 100 ký tự")
    private String name;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @Pattern(regexp = "^(\\+84|0)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @NotBlank(message = "Giới tính không được để trống")
    @Pattern(regexp = "MALE|FEMALE|OTHER", message = "Giới tính không hợp lệ")
    private String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải là trong quá khứ")
    private LocalDate dob;

    @Min(value = 0, message = "Kinh nghiệm không được âm")
    private Integer experience;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String description;

    // Các trường không nhập từ người dùng nhưng dùng để hiển thị
    private List<String> technologies; // tên các công nghệ

    private String status; // ACTIVE / INACTIVE từ Account

    // Tính tuổi từ ngày sinh
    public Integer getAge() {
        if (dob == null) return null;
        return Period.between(dob, LocalDate.now()).getYears();
    }

    // Chuyển từ Entity sang DTO
    public static CandidateDTO fromEntity(Candidate entity) {
        CandidateDTO dto = new CandidateDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setGender(entity.getGender() != null ? entity.getGender().name() : null);
        dto.setDob(entity.getDob());
        dto.setExperience(entity.getExperience());
        dto.setDescription(entity.getDescription());

        if (entity.getTechnologies() != null) {
            dto.setTechnologies(entity.getTechnologies().stream()
                    .map(Technology::getName)
                    .collect(Collectors.toList()));
        }

        Account acc = entity.getAccount();
        if (acc != null && acc.getStatus() != null) {
            dto.setStatus(acc.getStatus().name());
        }



        return dto;
    }
}
