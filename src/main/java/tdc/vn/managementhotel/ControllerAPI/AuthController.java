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
import tdc.vn.managementhotel.dto.UserDTO.UserLoginResponse;
import tdc.vn.managementhotel.dto.UserDTO.UserResponse;
import tdc.vn.managementhotel.entity.Role;
import tdc.vn.managementhotel.model.CustomUserDetails;
import tdc.vn.managementhotel.service.CustomUserDetailsService;
import tdc.vn.managementhotel.service.JwtBlacklistService;
import tdc.vn.managementhotel.service.UserService;
import tdc.vn.managementhotel.util.JwtUtil;

import java.util.Map;

@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/auth")

public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserService userService;

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



}