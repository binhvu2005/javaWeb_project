package com.data.project_javaWeb.controller.admin;

import com.data.project_javaWeb.dto.CandidateDTO;
import com.data.project_javaWeb.entity.Technology;
import com.data.project_javaWeb.service.admin.candidate.CandidateService;
import com.data.project_javaWeb.service.admin.technology.TechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private TechnologyService technologyService;

    @GetMapping
    public String listCandidates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer technologyId,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) Integer experience,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CandidateDTO> candidatePage = candidateService.searchCandidates(
                keyword, technologyId, gender, age, experience, pageable);

        model.addAttribute("candidates", candidatePage.getContent());
        model.addAttribute("technologies", technologyService.getAllTechnologies());
        System.out.println("Total technologies found: " + technologyService.getAllTechnologies());
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", candidatePage.getTotalPages());
        model.addAttribute("totalElements", candidatePage.getTotalElements());

        model.addAttribute("keyword", keyword);
        model.addAttribute("technologyId", technologyId);
        model.addAttribute("gender", gender);
        model.addAttribute("age", age);
        model.addAttribute("experience", experience);

        return "admin/candidate/candidateList";
    }

    @GetMapping("/toggle/{id}")
    public String toggleStatus(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        candidateService.toggleStatus(id);
        redirectAttributes.addFlashAttribute("message", "Candidate status updated successfully!");
        return "redirect:/admin/candidates";
    }
}
