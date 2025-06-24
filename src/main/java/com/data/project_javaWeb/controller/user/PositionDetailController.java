package com.data.project_javaWeb.controller.user;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.data.project_javaWeb.dto.ApplicationApplyDTO;
import com.data.project_javaWeb.dto.RecruitmentPositionDTO;
import com.data.project_javaWeb.entity.*;
import com.data.project_javaWeb.entity.enums.Progress;
import com.data.project_javaWeb.repository.util.login.LoginRepository;
import com.data.project_javaWeb.service.admin.application.ApplicationService;
import com.data.project_javaWeb.service.admin.recruitmentPosition.RecruitmentPositionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user/position")
public class PositionDetailController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private RecruitmentPositionService positionService;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private Cloudinary cloudinary;

    @GetMapping("/{id}")
    public String showPositionDetail(
            @PathVariable("id") Integer id,
            @CookieValue(value = "rememberEmail", required = false) String email,
            Model model
    ) {
        RecruitmentPositionDTO position = positionService.findById(id);
        if (position == null) {
            return "redirect:/user/home";
        }

        List<RecruitmentPositionDTO> related = positionService.searchByKeywordAndTech("", "", 0, 6)
                .stream()
                .filter(p -> !p.getId().equals(id))
                .collect(Collectors.toList());

        if (!model.containsAttribute("applicationDTO")) {
            ApplicationApplyDTO dto = new ApplicationApplyDTO();
            dto.setRecruitmentPositionId(id);
            model.addAttribute("applicationDTO", dto);
        }

        model.addAttribute("position", position);
        model.addAttribute("relatedPositions", related);
        return "user/positionDetail";
    }

    @PostMapping("/apply/{id}")
    public String apply(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("applicationDTO") ApplicationApplyDTO dto,
            BindingResult result,
            @CookieValue(value = "rememberEmail", required = false) String email,
            RedirectAttributes redirectAttributes
    ) {
        // Debug file
        System.out.println("=== File nhận được ===");
        if (dto.getCvFile() != null) {
            System.out.println("Tên file: " + dto.getCvFile().getOriginalFilename());
            System.out.println("Loại nội dung: " + dto.getCvFile().getContentType());
            System.out.println("Kích thước (bytes): " + dto.getCvFile().getSize());
        } else {
            System.out.println("Không có file hoặc file null");
        }

        // Kiểm tra đăng nhập
        if (email == null || email.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để nộp đơn.");
            return "redirect:/login";
        }

        Account account = loginRepository.findByEmail(email);
        if (account == null || account.getCandidate() == null) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản không hợp lệ hoặc không phải ứng viên.");
            return "redirect:/login";
        }

        Candidate candidate = account.getCandidate();
        if (isIncompleteCandidate(candidate)) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng cập nhật đầy đủ hồ sơ trước khi nộp đơn.");
            return "redirect:/user/profile";
        }

        // Kiểm tra form và file
        if (result.hasErrors() || dto.getCvFile() == null || dto.getCvFile().isEmpty()) {
            redirectAttributes.addFlashAttribute("applicationDTO", dto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.applicationDTO", result);
            redirectAttributes.addFlashAttribute("showModal", true);
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn file CV hợp lệ.");
            return "redirect:/user/position/" + id;
        }

        // Kiểm tra định dạng file
        String fileName = dto.getCvFile().getOriginalFilename().toLowerCase();
        if (!fileName.endsWith(".pdf") && !fileName.endsWith(".doc") &&
                !fileName.endsWith(".docx") && !fileName.endsWith(".txt")) {
            redirectAttributes.addFlashAttribute("applicationDTO", dto);
            redirectAttributes.addFlashAttribute("showModal", true);
            redirectAttributes.addFlashAttribute("error", "File CV phải là pdf, doc, docx hoặc txt.");
            return "redirect:/user/position/" + id;
        }

        // Upload lên Cloudinary
        String cvUrl;
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(dto.getCvFile().getBytes(), ObjectUtils.asMap(
                    "resource_type", "raw"
            ));
            cvUrl = uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("applicationDTO", dto);
            redirectAttributes.addFlashAttribute("showModal", true);
            redirectAttributes.addFlashAttribute("error", "Lỗi upload file: " + e.getMessage());
            return "redirect:/user/position/" + id;
        }

        // Tạo đơn
        RecruitmentPosition positionEntity = positionService.findEntityById(id);
        Application application = new Application();
        application.setCandidate(candidate);
        application.setRecruitmentPosition(positionEntity);
        application.setCvUrl(cvUrl);
        application.setProgress(Progress.APPLIED);

        applicationService.save(application);
        redirectAttributes.addFlashAttribute("success", "Bạn đã nộp đơn thành công!");
        return "redirect:/user/position/" + id;
    }

    private boolean isIncompleteCandidate(Candidate c) {
        return isNullOrBlank(c.getName()) ||
                isNullOrBlank(c.getPhone()) ||
                c.getGender() == null ||
                c.getDob() == null ||
                isNullOrBlank(c.getDescription()) ||
                c.getTechnologies() == null || c.getTechnologies().isEmpty();
    }

    private boolean isNullOrBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
