package com.data.project_javaWeb.controller.user;

import com.data.project_javaWeb.dto.CandidateDTO;
import com.data.project_javaWeb.dto.ChangePasswordDTO;
import com.data.project_javaWeb.entity.Account;
import com.data.project_javaWeb.entity.Candidate;
import com.data.project_javaWeb.entity.Technology;
import com.data.project_javaWeb.entity.enums.Gender;
import com.data.project_javaWeb.repository.util.login.LoginRepository;
import com.data.project_javaWeb.service.admin.candidate.CandidateService;
import com.data.project_javaWeb.service.admin.technology.TechnologyService;
import com.data.project_javaWeb.service.util.login.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/user/info")
public class InfoController {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private TechnologyService technologyService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Hiển thị trang thông tin cá nhân
    @GetMapping("")
    public String showUserInfo(
            Model model,
            @CookieValue(value = "rememberEmail", required = false) String email
    ) {
        CandidateDTO candidateDTO = null;
        if (email != null) {
            Account account = loginRepository.findByEmail(email);
            if (account != null && account.getCandidate() != null) {
                candidateDTO = CandidateDTO.fromEntity(account.getCandidate());
            }
        }

        model.addAttribute("candidate", candidateDTO);
        model.addAttribute("changePasswordDTO", new ChangePasswordDTO()); // cần thêm dòng này
        model.addAttribute("technologies", technologyService.getAllTechnologies());
        return "user/info";
    }


    // Xử lý cập nhật thông tin ứng viên
    @PostMapping("/update")
    public String updateCandidate(
            @ModelAttribute("candidate") @Valid CandidateDTO candidateDTO,
            BindingResult bindingResult,
            @CookieValue(value = "rememberEmail", required = false) String email,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("technologies", technologyService.getAllTechnologies());
            model.addAttribute("candidate", candidateDTO);
            model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
            model.addAttribute("showEditModal", true);
            return "user/info";
        }

        Account account = loginRepository.findByEmail(email);
        if (account == null || account.getCandidate() == null) {
            return "redirect:/login";
        }

        try {
            Candidate candidate = account.getCandidate();
            candidate.setId(candidateDTO.getId());
            candidate.setName(candidateDTO.getName());
            candidate.setDob(candidateDTO.getDob());
            candidate.setExperience(candidateDTO.getExperience());
            candidate.setEmail(candidateDTO.getEmail());
            candidate.setPhone(candidateDTO.getPhone());
            candidate.setGender(Gender.valueOf(candidateDTO.getGender()));
            candidate.setDescription(candidateDTO.getDescription());

            if (candidateDTO.getTechnologies() != null && !candidateDTO.getTechnologies().isEmpty()) {
                List<Technology> selectedTechs = technologyService.findAllByNames(candidateDTO.getTechnologies());
                candidate.setTechnologies(new HashSet<>(selectedTechs));
            } else {
                candidate.setTechnologies(new HashSet<>());
            }

            candidateService.update(candidate);
            redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin thành công!");

        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi khi cập nhật thông tin.");
            return "user/info";
        }

        return "redirect:/user/info";
    }

    // Xử lý đổi mật khẩu
    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute("changePasswordDTO") @Valid ChangePasswordDTO changePasswordDTO,
                                 BindingResult result,
                                 HttpSession session,
                                 Model model) {

        Account account = (Account) session.getAttribute("currentUser");
        if (account == null) {
            return "redirect:/login";
        }

        // So sánh mật khẩu cũ
        if (!result.hasErrors() &&
                !passwordEncoder.matches(changePasswordDTO.getOldPassword(), account.getPassword())) {
            result.rejectValue("oldPassword", "error.oldPassword", "Mật khẩu cũ không đúng.");
        }

        // So sánh mật khẩu xác nhận
        if (!result.hasErrors() &&
                !changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "Mật khẩu xác nhận không khớp.");
        }

        if (result.hasErrors()) {
            model.addAttribute("showChangePasswordModal", true);
            model.addAttribute("technologies", technologyService.getAllTechnologies());
            model.addAttribute("changePasswordDTO", changePasswordDTO);
            CandidateDTO candidateDTO = CandidateDTO.fromEntity(account.getCandidate());
            model.addAttribute("candidate", candidateDTO);
            return "user/info";
        }

        // Cập nhật mật khẩu mới
        account.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        loginService.update(account);

        model.addAttribute("message", "Đổi mật khẩu thành công.");
        return "redirect:/user/info";
    }


}
