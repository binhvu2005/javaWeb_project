package com.data.project_javaWeb.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/recruitmentPosition")
public class RecruitmentPositionController {
    @RequestMapping("")
    public String recruitmentPosition() {
        return "admin/recruitmentPosition/recruitmentPositionList";
    }
}
