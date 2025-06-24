package com.data.project_javaWeb.controller.util;

import com.data.project_javaWeb.dto.LoginDTO;
import com.data.project_javaWeb.entity.Account;
import com.data.project_javaWeb.repository.util.login.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Hiển thị form đăng nhập + kiểm tra cookie đăng nhập
    @GetMapping("/login")
    public String showLoginForm(Model model,
                                HttpSession session,
                                @CookieValue(value = "rememberEmail", required = false) String emailFromCookie) {

        // Nếu đã có session => chuyển hướng theo vai trò
        Account currentUser = (Account) session.getAttribute("currentUser");
        if (currentUser != null) {
            if ("ADMIN".equals(currentUser.getRole().name())) {
                return "redirect:/admin/dashboard";
            } else if ("CANDIDATE".equals(currentUser.getRole().name())) {
                return "redirect:/user/home";
            }
        }

        // Nếu có cookie => tìm lại tài khoản và tự động đăng nhập
        if (emailFromCookie != null) {
            Account account = loginRepository.findByEmail(emailFromCookie);
            if (account != null) {
                session.setAttribute("currentUser", account);
                if ("ADMIN".equals(account.getRole().name())) {
                    return "redirect:/admin/dashboard";
                } else if ("CANDIDATE".equals(account.getRole().name())) {
                    return "redirect:/user/home";
                }
            }
        }

        // Nếu chưa đăng nhập thì hiển thị form login
        model.addAttribute("loginDTO", new LoginDTO());
        return "util/login";
    }

    // Xử lý POST đăng nhập
    @PostMapping("/login")
    public String login(@ModelAttribute("loginDTO") @Valid LoginDTO loginDTO,
                        BindingResult result,
                        HttpSession session,
                        Model model,
                        HttpServletResponse response) {

        if (result.hasErrors()) {
            System.out.println("Dữ liệu không hợp lệ");
            return "util/login";
        }

        System.out.println("Email nhập: " + loginDTO.getEmail());
        System.out.println("Password nhập: " + loginDTO.getPassword());

        Account account = loginRepository.findByEmail(loginDTO.getEmail());
        System.out.println("Tài khoản tìm thấy: " + account);

        if (account == null) {
            model.addAttribute("loginError", "Email hoặc mật khẩu không đúng!");
            return "util/login";
        }

        boolean match = passwordEncoder.matches(loginDTO.getPassword(), account.getPassword());
        System.out.println("Mật khẩu khớp: " + match);

        if (!match) {
            model.addAttribute("loginError", "Email hoặc mật khẩu không đúng!");
            return "util/login";
        }

        // Đăng nhập thành công => lưu session
        session.setAttribute("currentUser", account);

        // Tạo cookie ghi nhớ email (7 ngày)
        Cookie cookie = new Cookie("rememberEmail", account.getEmail());
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
        cookie.setPath("/");
        response.addCookie(cookie);

        // Điều hướng theo vai trò
        if ("ADMIN".equals(account.getRole().name())) {
            return "redirect:/admin/dashboard";
        } else if ("CANDIDATE".equals(account.getRole().name())) {
            return "redirect:/user/home";
        } else {
            model.addAttribute("loginError", "Tài khoản không hợp lệ!");
            return "util/login";
        }
    }

    // Đăng xuất
    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();

        // Xóa cookie rememberEmail
        Cookie cookie = new Cookie("rememberEmail", null);
        cookie.setMaxAge(0); // xóa ngay
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/login";
    }
}
