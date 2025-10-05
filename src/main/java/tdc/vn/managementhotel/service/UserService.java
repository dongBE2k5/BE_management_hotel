package tdc.vn.managementhotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.RegisterRequest;
import tdc.vn.managementhotel.entity.Role;
import tdc.vn.managementhotel.entity.User;
import tdc.vn.managementhotel.repository.RoleRepository;
import tdc.vn.managementhotel.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Đăng ký user mới với role mặc định ROLE_USER
     */
    public User register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        if (req.getEmail() != null && userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        // Tìm role mặc định
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role ROLE_USER not found."));

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword())); // BCrypt encode
        user.setEmail(req.getEmail());
        user.setFullName(req.getFullName());
        user.setPhone(req.getPhone());
        user.setCccd(req.getCccd());
        user.setRole(defaultRole);

        return userRepository.save(user);
    }
}
