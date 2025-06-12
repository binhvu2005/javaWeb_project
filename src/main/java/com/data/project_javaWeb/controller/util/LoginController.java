package com.data.project_javaWeb.controller.util;

import com.data.project_javaWeb.dto.AccountDTO;
import com.data.project_javaWeb.entity.Account;
import com.data.project_javaWeb.repository.util.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Hiển thị form đăng nhập
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("accountDTO", new AccountDTO());
        return "util/login";
    }

    // Xử lý đăng nhập thủ công
    @PostMapping("/login")
    public String login(@ModelAttribute("accountDTO") @Valid AccountDTO accountDTO,
                        BindingResult result,
                        HttpSession session,
                        Model model) {
        if (result.hasErrors()) {
            System.out.println("Dữ liệu không hợp lệ");
            return "util/login";
        }

        System.out.println("Email nhập: " + accountDTO.getEmail());
        System.out.println("Password nhập: " + accountDTO.getPassword());

        Account account = loginRepository.findByEmail(accountDTO.getEmail());
        System.out.println("Tài khoản tìm thấy: " + account);

        if (account == null) {
            System.out.println("Không tìm thấy tài khoản.");
            model.addAttribute("loginError", "Email hoặc mật khẩu không đúng!");
            return "util/login";
        }

        boolean match = passwordEncoder.matches(accountDTO.getPassword(), account.getPassword());
        System.out.println("Mật khẩu khớp: " + match);

        if (!match) {
            model.addAttribute("loginError", "Email hoặc mật khẩu không đúng!");
            return "util/login";
        }

        System.out.println("Đăng nhập thành công với vai trò: " + account.getRole());

        session.setAttribute("currentUser", account);

        if ("ADMIN".equals(account.getRole().name())) {
            return "redirect:/admin/dashboard";
        } else if ("CANDIDATE".equals(account.getRole().name())) {
            return "redirect:/candidate/home";
        } else {
            model.addAttribute("loginError", "Tài khoản không hợp lệ!");
            return "util/login";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }
}
