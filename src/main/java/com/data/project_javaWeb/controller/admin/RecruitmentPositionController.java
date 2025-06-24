package com.data.project_javaWeb.controller.admin;

import com.data.project_javaWeb.dto.RecruitmentPositionDTO;
import com.data.project_javaWeb.service.admin.recruitmentPosition.RecruitmentPositionService;
import com.data.project_javaWeb.service.admin.technology.TechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/recruitmentPosition")
public class RecruitmentPositionController {

    @Autowired
    private RecruitmentPositionService recruitmentPositionService;

    @Autowired
    private TechnologyService technologyService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "5") int size,
                       @RequestParam(defaultValue = "") String searchKeyword,
                       Model model) {
        Page<RecruitmentPositionDTO> positionPage = recruitmentPositionService.getAll(page, size, searchKeyword);
        model.addAttribute("positions", positionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", positionPage.getTotalPages());
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("recruitmentPositionDTO", new RecruitmentPositionDTO());
        model.addAttribute("technologies", technologyService.getAllTechnologies());
        return "admin/recruitmentPosition/recruitmentPositionList";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("recruitmentPositionDTO") @Valid RecruitmentPositionDTO dto,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        System.out.println("===> BẮT ĐẦU SAVE");

        System.out.println("Dữ liệu nhận được:");
        System.out.println("Name: " + dto.getName());
        System.out.println("Min Salary: " + dto.getMinSalary());
        System.out.println("Max Salary: " + dto.getMaxSalary());
        System.out.println("Min Experience: " + dto.getMinExperience());
        System.out.println("Expired Date: " + dto.getExpiredDate());
        System.out.println("Technologies: " + dto.getTechnologies());
        System.out.println("Description: " + dto.getDescription());

        if (bindingResult.hasErrors()) {
            System.out.println("===> LỖI VALIDATION, KHÔNG LƯU ĐƯỢC");
            bindingResult.getFieldErrors().forEach(error -> {
                System.out.println("Field Error - " + error.getField() + ": " + error.getDefaultMessage());
            });

            model.addAttribute("recruitmentPositionDTO", dto);
            Page<RecruitmentPositionDTO> positionPage = recruitmentPositionService.getAll(1, 5, "");
            model.addAttribute("positions", positionPage.getContent());
            model.addAttribute("currentPage", 1);
            model.addAttribute("totalPages", positionPage.getTotalPages());
            model.addAttribute("searchKeyword", "");
            model.addAttribute("technologies", technologyService.getAllTechnologies());
            model.addAttribute("showModal", true);
            return "admin/recruitmentPosition/recruitmentPositionList";
        }

        System.out.println("===> KHÔNG CÓ LỖI, TIẾN HÀNH LƯU");
        recruitmentPositionService.save(dto);
        System.out.println("===> LƯU THÀNH CÔNG");
        redirectAttributes.addFlashAttribute("message", "Saved successfully");
        return "redirect:/admin/recruitmentPosition";
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id,
                       Model model,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "5") int size,
                       @RequestParam(defaultValue = "") String searchKeyword) {
        RecruitmentPositionDTO dto = recruitmentPositionService.findById(id);
        Page<RecruitmentPositionDTO> positionPage = recruitmentPositionService.getAll(page, size, searchKeyword);

        model.addAttribute("editMode", true);
        model.addAttribute("showModal", true);
        model.addAttribute("recruitmentPositionDTO", dto);
        model.addAttribute("technologies", technologyService.getAllTechnologies());
        model.addAttribute("positions", positionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", positionPage.getTotalPages());
        model.addAttribute("searchKeyword", searchKeyword);
        return "admin/recruitmentPosition/recruitmentPositionList";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("recruitmentPositionDTO") @Valid RecruitmentPositionDTO dto,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            Page<RecruitmentPositionDTO> positionPage = recruitmentPositionService.getAll(1, 5, "");
            model.addAttribute("editMode", true);
            model.addAttribute("showModal", true);
            model.addAttribute("recruitmentPositionDTO", dto);
            model.addAttribute("technologies", technologyService.getAllTechnologies());
            model.addAttribute("positions", positionPage.getContent());
            model.addAttribute("currentPage", 1);
            model.addAttribute("totalPages", positionPage.getTotalPages());
            model.addAttribute("searchKeyword", "");
            return "admin/recruitmentPosition/recruitmentPositionList";
        }
        recruitmentPositionService.update(dto);
        redirectAttributes.addFlashAttribute("message", "Updated successfully");
        return "redirect:/admin/recruitmentPosition";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        recruitmentPositionService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Deleted successfully");
        return "redirect:/admin/recruitmentPosition";
    }
}