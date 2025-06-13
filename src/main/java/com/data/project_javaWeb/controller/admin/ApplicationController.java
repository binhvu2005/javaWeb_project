package com.data.project_javaWeb.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/application")
public class ApplicationController {
    @RequestMapping("")
    public String application() {
        return "admin/application/applicationList";
    }
}
