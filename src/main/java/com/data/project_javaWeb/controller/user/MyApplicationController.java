package com.data.project_javaWeb.controller.user;

import com.data.project_javaWeb.dto.ApplicationDetailDTO;
import com.data.project_javaWeb.dto.MyApplicationDTO;
import com.data.project_javaWeb.entity.Application;
import com.data.project_javaWeb.entity.Candidate;
import com.data.project_javaWeb.entity.enums.Progress;
import com.data.project_javaWeb.service.admin.application.ApplicationService;
import com.data.project_javaWeb.service.admin.candidate.CandidateService;
import com.data.project_javaWeb.service.user.MyApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/user/applications")
public class MyApplicationController {

    @Autowired
    private MyApplicationService myApplicationService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private ApplicationService applicationService;

    // Hiển thị danh sách ứng tuyển
    @GetMapping
    public String viewMyApplications(@CookieValue(value = "rememberEmail", required = false) String email,
                                     @RequestParam(defaultValue = "1") int page,
                                     Model model) {

        if (email == null) return "redirect:/login";

        Candidate candidate = candidateService.findByEmail(email);
        if (candidate == null) return "redirect:/login";

        int size = 5;
        List<MyApplicationDTO> applications = myApplicationService.getApplicationsByCandidate(candidate.getId(), page, size);
        int totalPages = myApplicationService.getTotalPages(candidate.getId(), size);

        model.addAttribute("applications", applications);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);
        return "user/myApplication";
    }

    // API: Lấy chi tiết đơn
    @GetMapping("/api/{id}")
    @ResponseBody
    public ApplicationDetailDTO getApplicationDetail(@PathVariable("id") Integer id,
                                                     @CookieValue(value = "rememberEmail", required = false) String email) {
        if (email == null) return null;

        Candidate candidate = candidateService.findByEmail(email);
        Application app = applicationService.findById(id);

        if (candidate != null && app != null && app.getCandidate().getId().equals(candidate.getId())) {
            return applicationService.mapToDetailDTO(app);
        }
        return null;
    }

    // Rút đơn
    @PostMapping("/withdraw")
    public String withdrawApplication(@RequestParam("applicationId") Integer applicationId,
                                      @RequestParam("reason") String reason,
                                      @CookieValue(value = "rememberEmail", required = false) String email,
                                      RedirectAttributes redirectAttributes) {

        Candidate candidate = candidateService.findByEmail(email);
        Application app = applicationService.findById(applicationId);

        if (candidate != null && app != null && app.getCandidate().getId().equals(candidate.getId())) {
            app.setProgress(Progress.WITHDRAWN);
            app.setDestroyReason(reason);
            app.setDestroyAt(LocalDateTime.now());
            applicationService.save(app);
            redirectAttributes.addFlashAttribute("success", "Rút đơn thành công.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không thể rút đơn.");
        }

        return "redirect:/user/applications";
    }

    // Xác nhận phỏng vấn
    @PostMapping("/confirm-interview")
    public String confirmInterview(@RequestParam("applicationId") Integer applicationId,
                                   @CookieValue(value = "rememberEmail", required = false) String email,
                                   RedirectAttributes redirectAttributes) {

        Candidate candidate = candidateService.findByEmail(email);
        Application app = applicationService.findById(applicationId);

        boolean isValid = candidate != null &&
                app != null &&
                app.getCandidate().getId().equals(candidate.getId()) &&
                app.getInterviewTime() != null &&
                app.getInterviewLink() != null &&
                app.getProgress() == Progress.HANDLING;

        if (isValid) {
            app.setProgress(Progress.INTERVIEWING);
            applicationService.save(app);
            redirectAttributes.addFlashAttribute("success", "Xác nhận phỏng vấn thành công.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không thể xác nhận phỏng vấn.");
        }

        return "redirect:/user/applications";
    }
}
