package com.data.project_javaWeb.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/candidate")
public class CandidateController {
    @RequestMapping("")
    public String candidate() {
        return "admin/candidate/candidateList";
    }
}
