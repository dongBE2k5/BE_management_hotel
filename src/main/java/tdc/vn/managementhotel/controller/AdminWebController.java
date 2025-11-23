package tdc.vn.managementhotel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.Repository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.HostHotelDTO.HostHotelResponseDTO;
import tdc.vn.managementhotel.dto.HotelDTO.HotelDTO;
import tdc.vn.managementhotel.dto.LocationDTO.LocationResponseDTO;
import tdc.vn.managementhotel.dto.LoginRequest;
import tdc.vn.managementhotel.dto.RegisterRequest;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.Location;
import tdc.vn.managementhotel.enums.HostHotelStatus;
import tdc.vn.managementhotel.model.CustomUserDetails;
import tdc.vn.managementhotel.repository.HotelRepository;
import tdc.vn.managementhotel.repository.LocationRepository;
import tdc.vn.managementhotel.service.HostHotelService;
import tdc.vn.managementhotel.service.HotelService;
import tdc.vn.managementhotel.service.LocationService;
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
    private final HotelRepository hotelRepository;
    private final LocationService locationService;
    private final HotelService hotelService;

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

           return "redirect:hosts";
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
        return "admin/admin-dashboard";
    }

    @GetMapping("/hosts/{idUser}/hotelList")
    public String listHotel(Model model, @PathVariable Long idUser) {

        List<Hotel> hotels = hotelRepository.findByUserId(idUser);
        model.addAttribute("hotels", hotels);
        model.addAttribute("idUser", idUser);

        // Rất quan trọng: Trả về tên của file fragment, không phải "admin-dashboard"
        return "admin/hotel-list-fragment"; // Ví dụ: 'templates/admin/hotel-list-fragment.html'
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
            model.addAttribute("loginRequest", new LoginRequest());
            return "login"; // chuyển sang trang login
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }

    @GetMapping("/admin/hosts/{hostID}/hotels/new")
    public String showNewHotel(@PathVariable Long hostID, Model model) {
        List<LocationResponseDTO> locationList =  locationService.findAll();
        model.addAttribute("hostID", hostID);
        model.addAttribute("locationList", locationList);
        return "admin/add-hotel";
    }

    @PostMapping("/admin/hosts/{hostID}/hotels")
    public String createHotel(
            @PathVariable Long hostID,
            @ModelAttribute("hotel") HotelDTO dto,
            Model model) {
        System.out.println("Vao ham ADD");
        try {
            dto.setUserId(hostID); // gán hostID vào DTO
            hotelService.createHotel(dto);
            return "redirect:/hosts";

        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "admin/add-hotel"; // quay lại form nếu lỗi
        }
    }

}
