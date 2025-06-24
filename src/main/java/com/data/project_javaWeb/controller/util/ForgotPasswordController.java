package com.data.project_javaWeb.controller.util;

import com.data.project_javaWeb.entity.Account;
import com.data.project_javaWeb.repository.util.login.LoginRepository;
import com.data.project_javaWeb.repository.util.signup.SignupRepository;
import com.data.project_javaWeb.service.util.login.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Random;

@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/send-code")
    public String sendCode(@RequestParam String email,
                           RedirectAttributes redirectAttributes,
                           HttpSession session) {
        Account account = loginRepository.findByEmail(email);
        if (account == null) {
            redirectAttributes.addFlashAttribute("error", "Email không tồn tại.");
            return "redirect:/login";
        }

        String code = String.format("%06d", new Random().nextInt(999999));
        session.setAttribute("resetEmail", email);
        session.setAttribute("verifyCode", code);

        emailService.sendSimpleMessage(email, "Mã xác minh đặt lại mật khẩu", "Mã xác minh của bạn là: " + code);

        redirectAttributes.addFlashAttribute("step", "verify");
        redirectAttributes.addFlashAttribute("message", "Đã gửi mã xác minh đến email.");
        return "redirect:/login";
    }

    @PostMapping("/verify-code")
    public String verifyCode(@RequestParam String code,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {
        String sessionCode = (String) session.getAttribute("verifyCode");

        if (sessionCode != null && sessionCode.equals(code)) {
            redirectAttributes.addFlashAttribute("step", "reset");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Mã xác minh không đúng!");
            redirectAttributes.addFlashAttribute("step", "verify");
            return "redirect:/login";
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String password,
                                @RequestParam String confirmPassword,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Xác nhận mật khẩu không khớp!");
            redirectAttributes.addFlashAttribute("step", "reset");
            return "redirect:/login";
        }

        String email = (String) session.getAttribute("resetEmail");
        if (email == null) {
            redirectAttributes.addFlashAttribute("error", "Phiên xác minh đã hết hạn!");
            return "redirect:/login";
        }

        Account account = loginRepository.findByEmail(email);
        if (account != null) {
            account.setPassword(passwordEncoder.encode(password));
            loginRepository.update(account);
            session.removeAttribute("resetEmail");
            session.removeAttribute("verifyCode");

            redirectAttributes.addFlashAttribute("message", "Đặt lại mật khẩu thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Tài khoản không tồn tại!");
        }

        return "redirect:/login";
    }

}
