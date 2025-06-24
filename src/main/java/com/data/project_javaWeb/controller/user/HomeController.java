package com.data.project_javaWeb.controller.user;

import com.data.project_javaWeb.dto.RecruitmentPositionDTO;
import com.data.project_javaWeb.entity.Technology;
import com.data.project_javaWeb.service.admin.recruitmentPosition.RecruitmentPositionService;
import com.data.project_javaWeb.service.admin.technology.TechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user/home")
public class HomeController {

    @Autowired
    private RecruitmentPositionService positionService;

    @Autowired
    private TechnologyService technologyService;

    @GetMapping
    public String showHomePage(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "tech", required = false) String tech,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model
    ) {
        int size = 9;
        if (page < 0) page = 0;

        Page<RecruitmentPositionDTO> positions = positionService.searchByKeywordAndTech(keyword, tech, page, size);
        List<Technology> technologies = technologyService.getAllTechnologies();

        model.addAttribute("positions", positions.getContent());
        model.addAttribute("totalPages", positions.getTotalPages());
        model.addAttribute("currentPage", positions.getNumber()); // <<== chính xác
        model.addAttribute("technologies", technologies);
        model.addAttribute("selectedTech", tech);
        model.addAttribute("keyword", keyword);

        return "user/home";
    }
}
