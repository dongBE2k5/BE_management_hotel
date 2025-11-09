package tdc.vn.managementhotel.controller;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.Host;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tdc.vn.managementhotel.dto.HostHotelDTO.HostHotelResponseDTO;
import tdc.vn.managementhotel.dto.JwtAuthenticationResponse;
import tdc.vn.managementhotel.dto.LoginRequest;
import tdc.vn.managementhotel.dto.RegisterRequest;
import tdc.vn.managementhotel.enums.HostHotelStatus;
import tdc.vn.managementhotel.model.CustomUserDetails;
import tdc.vn.managementhotel.service.HostHotelService;
import tdc.vn.managementhotel.service.UserService;
import tdc.vn.managementhotel.util.JwtUtil;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminWebController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final HostHotelService hostHotelService;

    @GetMapping("login")
    public String login(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }
    @PostMapping("/login")
    public String handleLogin(@ModelAttribute LoginRequest req,Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(authentication);

           return "admin-dashboard";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "login";
        }
    }

    @GetMapping("register")
    public String register(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }
    @GetMapping("/hosts")
    public String showHostManagement(Model model) {

        // Lấy danh sách host từ service
        List<HostHotelResponseDTO> hostList = hostHotelService.getAll();

        // Thêm các thuộc tính cho Thymeleaf
        model.addAttribute("hosts", hostList);


        // Trả về tên của tệp HTML (không có .html)
        return "admin-dashboard";
    }
    @PutMapping("/hosts/{id}")
    public String updateHostStatus(@PathVariable Long id, @RequestParam String status, RedirectAttributes redirectAttrs) {
        HostHotelStatus hotelStatus = HostHotelStatus.valueOf(status);
        hostHotelService.updateStatus(id, hotelStatus);
        redirectAttrs.addFlashAttribute("success", "Cập nhật trạng thái thành công!");
        return "redirect:/hosts";
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
