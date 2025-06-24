package com.data.project_javaWeb.controller.util;

import com.data.project_javaWeb.dto.AccountDTO;
import com.data.project_javaWeb.service.util.signup.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class SignupController {

    @Autowired
    private SignupService signupService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("accountDTO", new AccountDTO());
        return "util/signup";
    }

    @PostMapping("/register")
    public String processForm(@ModelAttribute("accountDTO") @Valid AccountDTO dto,
                              BindingResult result,
                              Model model) {

        if (result.hasErrors()) {
            return "util/signup";
        }

        boolean success = signupService.register(dto);
        if (!success) {
            result.rejectValue("email", "email.exists", "Email đã tồn tại!");
            return "util/signup";
        }


        return "redirect:/login";
    }
}
