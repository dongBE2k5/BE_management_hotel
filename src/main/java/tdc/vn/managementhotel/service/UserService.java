package tdc.vn.managementhotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.LoginRequest;
import tdc.vn.managementhotel.dto.RegisterRequest;
import tdc.vn.managementhotel.dto.RoomDTO.RoomResponseDTO;
import tdc.vn.managementhotel.dto.UserDTO.UserResponse;
import tdc.vn.managementhotel.entity.Role;
import tdc.vn.managementhotel.entity.Room;
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
    public UserResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }
        if (req.getEmail() != null && userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
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
//        user.setCccd(req.getCccd());
        user.setRole(defaultRole);

        return mapEntityToResponse(userRepository.save(user));
    }

    public UserResponse find(Long id) {
        return userRepository.findById(id).map(this::mapEntityToResponse).orElse(null);
    }


    private UserResponse mapEntityToResponse(User user) {
//        List<ImageRoomResponseDTO> listRoom = new ArrayList<>();
//        room.getImageRoom().stream().map(imageRoom -> {
//            ImageRoomResponseDTO imageRoomResponseDTO = new ImageRoomResponseDTO();
//            imageRoomResponseDTO.setId(imageRoom.getId());
//            imageRoomResponseDTO.setImage(imageRoom.getName());
//            listRoom.add(imageRoomResponseDTO);
//            return listRoom;
//        }).toList();
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getCccd()
        );
    }
}
