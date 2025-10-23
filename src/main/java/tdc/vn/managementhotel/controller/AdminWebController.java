package tdc.vn.managementhotel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import tdc.vn.managementhotel.dto.RegisterRequest;
import tdc.vn.managementhotel.service.UserService;

@Controller
@RequiredArgsConstructor
public class AdminWebController {
    private final UserService userService;

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute RegisterRequest req, Model model) {
        try {
            userService.registerHost(req);
            model.addAttribute("success", "Đăng ký thành công!");
            return "login"; // chuyển sang trang login
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }

}
