package com.data.project_javaWeb.controller.admin;

import com.data.project_javaWeb.dto.TechnologyDTO;
import com.data.project_javaWeb.entity.Technology;
import com.data.project_javaWeb.service.admin.TechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/technology")
public class TechnologyController {

    @Autowired
    private TechnologyService service;

    @GetMapping("")
    public String list(@RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "6") int size,
                       Model model) {

        Page<Technology> pageData = service.list(keyword, page, size);
        model.addAttribute("page", pageData);
        model.addAttribute("technologies", pageData.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageData.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("technology", new TechnologyDTO());
        return "admin/technology/technologyList";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("technology") TechnologyDTO dto,
                       BindingResult br,
                       @RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "6") int size,
                       Model model) {

        if (br.hasErrors()) {
            Page<Technology> pageData = service.list(keyword, page, size);
            model.addAttribute("page", pageData);
            model.addAttribute("technologies", pageData.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pageData.getTotalPages());
            model.addAttribute("keyword", keyword);
            model.addAttribute("showModal", true);
            return "admin/technology/technologyList";
        }

        try {
            service.saveOrUpdate(dto);
        } catch (RuntimeException e) {
            Page<Technology> pageData = service.list(keyword, page, size);
            model.addAttribute("page", pageData);
            model.addAttribute("technologies", pageData.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pageData.getTotalPages());
            model.addAttribute("keyword", keyword);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("showModal", true);
            return "admin/technology/technologyList";
        }

        return "redirect:/admin/technology";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "redirect:/admin/technology";
    }
}
