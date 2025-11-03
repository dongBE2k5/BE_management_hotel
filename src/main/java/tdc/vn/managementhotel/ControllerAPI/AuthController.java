package tdc.vn.managementhotel.ControllerAPI;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.JwtAuthenticationResponse;
import tdc.vn.managementhotel.dto.LoginRequest;
import tdc.vn.managementhotel.dto.RegisterRequest;
import tdc.vn.managementhotel.dto.UserDTO.*;
import tdc.vn.managementhotel.entity.Role;
import tdc.vn.managementhotel.entity.User;
import tdc.vn.managementhotel.model.CustomUserDetails;
import tdc.vn.managementhotel.repository.UserRepository;
import tdc.vn.managementhotel.service.CustomUserDetailsService;
import tdc.vn.managementhotel.service.JwtBlacklistService;
import tdc.vn.managementhotel.service.UserService;
import tdc.vn.managementhotel.util.JwtUtil;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {
            UserResponse created = userService.register(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/register-employee")
    public ResponseEntity<?> registerEmployee(@RequestBody RegisterRequest req) {
        try {
            UserResponse created = userService.registerEmployee(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(authentication);

            return ResponseEntity.ok(new JwtAuthenticationResponse(userDetails.getId(), userDetails.getUsername(), userDetails.getRole(), token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<?> findUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.find(id));
    }
    @Autowired
    private JwtBlacklistService jwtBlacklistService;

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // bỏ "Bearer "
                jwtBlacklistService.blacklistToken(token);
            }
            return ResponseEntity.ok("Logout thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout thất bại!");
        }
    }
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }

        String token = authHeader.substring(7);

        if(jwtBlacklistService.isTokenBlacklisted(token)) { // kiểm tra token đã logout chưa
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token revoked");
        }

        if(!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String username = jwtUtil.getUsernameFromJWT(token);
        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);


        return ResponseEntity.ok(userDetails); // trả về thông tin user
    }


    @PutMapping("/change-password/{userId}")
    public ResponseEntity<?> changePassword(
            @PathVariable Long userId,
            @RequestBody ChangePasswordRequest req) {
        try {
            UserResponse response = userService.changePassword(userId, req.getOldPassword(), req.getNewPassword());
            return ResponseEntity.ok(Map.of("success", true, "message", "Đổi mật khẩu thành công!", "data", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }



    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        userService.sendOtpToEmail(email);
        return ResponseEntity.ok("OTP đã gửi đến email");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody ForgotPasswordRequest req) {
        boolean valid = userService.verifyOtp(req.getEmail(), req.getOtp());
        if (!valid) {
            return ResponseEntity.badRequest().body("OTP không hợp lệ");
        }
        userService.resetPassword(req.getEmail(), req.getNewPassword());
        return ResponseEntity.ok("Đặt lại mật khẩu thành công");
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO dto) {
        Optional<User> optionalUser = userRepository.findById(id); //
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setCccd(dto.getCccd());
        user.setGender(dto.getGender());
        user.setBirthDate(dto.getBirthDate());
        user.setAddress(dto.getAddress());

        userRepository.save(user); //
        return ResponseEntity.ok(user);
    }

}