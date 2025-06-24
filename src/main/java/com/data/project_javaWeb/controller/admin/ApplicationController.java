package com.data.project_javaWeb.controller.admin;

import com.data.project_javaWeb.dto.ApplicationDetailDTO;
import com.data.project_javaWeb.dto.ApplicationListDTO;
import com.data.project_javaWeb.entity.Application;
import com.data.project_javaWeb.entity.enums.InterviewRequestResult;
import com.data.project_javaWeb.entity.enums.InterviewResult;
import com.data.project_javaWeb.entity.enums.Progress;
import com.data.project_javaWeb.service.admin.application.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/application")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping
    public String listApplications(
            @RequestParam(value = "searchKeyword", required = false) String keyword,
            @RequestParam(value = "processFilter", required = false) Progress progress,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        Page<Application> applicationPage = applicationService.findApplications(keyword, progress, page, 5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        List<ApplicationListDTO> applicationDTOs = applicationPage.getContent().stream()
                .map(app -> {
                    String cssClass;
                    switch (app.getProgress()) {
                        case APPLIED:
                            cssClass = "bg-warning text-dark";
                            break;
                        case INTERVIEWING:
                            cssClass = "bg-info text-dark";
                            break;
                        case DONE:
                            cssClass = "bg-success";
                            break;
                        case REJECTED:
                            cssClass = "bg-danger";
                            break;
                        case WITHDRAWN:
                            cssClass = "bg-secondary";
                            break;
                        case HANDLING:
                            cssClass = "bg-primary text-light";
                            break;
                        default:
                            cssClass = "";
                            break;
                    }

                    return new ApplicationListDTO(
                            app,
                            app.getCreatedAt() != null ? app.getCreatedAt().format(formatter) : "",
                            app.getUpdatedAt() != null ? app.getUpdatedAt().format(formatter) : "",
                            cssClass
                    );
                })
                .collect(Collectors.toList());

        Page<ApplicationListDTO> applicationDTOPage = new PageImpl<>(
                applicationDTOs,
                applicationPage.getPageable(),
                applicationPage.getTotalElements()
        );

        model.addAttribute("applications", applicationDTOPage);
        model.addAttribute("searchKeyword", keyword);
        model.addAttribute("processFilter", progress);
        return "admin/application/applicationList";
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> getDetailApi(@PathVariable Integer id) {
        Application application = applicationService.findById(id);
        if (application == null) return ResponseEntity.notFound().build();

        // Chuyển sang trạng thái HANDLING nếu đang APPLIED
        if (application.getProgress() == Progress.APPLIED) {
            application.setProgress(Progress.HANDLING);
            applicationService.save(application);
        }

        ApplicationDetailDTO dto = applicationService.mapToDetailDTO(application);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/send-interview")
    public String sendInterview(
            @RequestParam Integer applicationId,
            @RequestParam("interviewTime")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime interviewTime,
            @RequestParam("interviewLink") String interviewLink,
            RedirectAttributes redirectAttributes) {

        Application application = applicationService.findById(applicationId);
        application.setInterviewTime(interviewTime);
        application.setInterviewLink(interviewLink);
        application.setProgress(Progress.HANDLING);
        application.setInterviewRequestResult(InterviewRequestResult.PENDING);
        applicationService.save(application);

        redirectAttributes.addFlashAttribute("success", "Gửi lịch phỏng vấn thành công.");
        return "redirect:/admin/application";
    }

    @PostMapping("/send-result")
    public String sendResult(
            @RequestParam Integer applicationId,
            @RequestParam("interviewResult") InterviewResult result,
            @RequestParam("interviewResultNote") String note,
            RedirectAttributes redirectAttributes) {

        Application application = applicationService.findById(applicationId);
        application.setInterviewResult(result);
        application.setInterviewResultNote(note);
        application.setProgress(result == InterviewResult.PASS ? Progress.DONE : Progress.REJECTED);
        applicationService.save(application);

        redirectAttributes.addFlashAttribute("success", "Đã cập nhật kết quả phỏng vấn.");
        return "redirect:/admin/application";
    }

    @PostMapping("/cancel")
    public String cancelApplication(
            @RequestParam Integer applicationId,
            @RequestParam("destroyReason") String reason,
            RedirectAttributes redirectAttributes) {

        Application application = applicationService.findById(applicationId);
        application.setDestroyReason(reason);
        application.setDestroyAt(LocalDateTime.now());
        application.setProgress(Progress.REJECTED);
        applicationService.save(application);

        redirectAttributes.addFlashAttribute("success", "Đơn đã được hủy.");
        return "redirect:/admin/application";
    }
}
