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
    public String register(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }
    @GetMapping("/hosts")
    public String showHostManagement(Model model) {

        // Lấy danh sách host từ service
//        List<Host> hostList = hostService.findAll();

        // Thêm các thuộc tính cho Thymeleaf
        model.addAttribute("hosts", "hostList");
        model.addAttribute("activePage", "hosts");
        model.addAttribute("pageTitle", "Quản lý Host");

        // Trả về tên của tệp HTML (không có .html)
        return "admin-dashboard";
    }
    @PostMapping("/register")
    public String handleRegister(@ModelAttribute RegisterRequest registerRequest, Model model) {
        try {
            userService.registerHost(registerRequest);
            model.addAttribute("success", "Đăng ký thành công!");
            return "login"; // chuyển sang trang login
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }

}
