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

import java.util.regex.Pattern;

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
    // ✅ Regex cho username & password
//    private static final Pattern USERNAME_PATTERN =
//            Pattern.compile("^(?=.{6,}$)(?=.*[A-Z])[A-Za-z][A-Za-z0-9_.\\-@!]*$");
//    private static final Pattern PASSWORD_PATTERN =
//            Pattern.compile("^(?=.{6,}$)(?=.*[A-Z])(?=.*[^A-Za-z0-9])\\S+$");
//    private static final Pattern GMAIL_PATTERN =
//            Pattern.compile("^[A-Za-z0-9._%+-]+@gmail\\.com$");

    public UserResponse register(RegisterRequest req) {
//        if (req.getFullName() == null || req.getFullName().trim().isEmpty()) {
//            throw new RuntimeException("Họ tên không được để trống.");
//        }
//        if (req.getUsername() == null || req.getUsername().trim().isEmpty()) {
//            throw new RuntimeException("User name không được để trống.");
//        }
//        if (!USERNAME_PATTERN.matcher(req.getUsername()).matches()) {
//            throw new RuntimeException(" Username không hợp lệ! " +
//                    "Phải bắt đầu bằng chữ, có ít nhất 6 ký tự, chứa 1 chữ in hoa, không có khoảng trắng.");
//        }
//        if (req.getEmail() == null || req.getEmail().trim().isEmpty()) {
//            throw new RuntimeException("Email không được để trống.");
//        }
//        if (!GMAIL_PATTERN.matcher(req.getEmail()).matches()) {
//            throw new RuntimeException(" Email không hợp lệ! " +
//                    "Chỉ chấp nhận email có đuôi @gmail.com và không chứa khoảng trắng.");
//        }
//        if (req.getPhone() == null || req.getPhone().trim().isEmpty()) {
//            throw new RuntimeException("Số điện thoại không được để trống.");
//        }
//        if (req.getPassword() == null || req.getPassword().trim().isEmpty()) {
//            throw new RuntimeException("Mật khẩu không được để trống.");
//        }
//        // ⚙️ Kiểm tra password hợp lệ
//        if (!PASSWORD_PATTERN.matcher(req.getPassword()).matches()) {
//            throw new RuntimeException(" Mật khẩu không hợp lệ! " +
//                    "Phải có ít nhất 6 ký tự, 1 chữ in hoa và 1 ký tự đặc biệt, không có khoảng trắng.");
//        }
//

        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }
        if (req.getEmail() != null && userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ROLE_USER."));

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEmail(req.getEmail());
        user.setFullName(req.getFullName());
        user.setPhone(req.getPhone());
        user.setRole(defaultRole);

        return mapEntityToResponse(userRepository.save(user));
    }


    public UserResponse registerEmployee(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }
        if (req.getEmail() != null && userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        Role role = roleRepository.findByName("ROLE_EMPLOYEE")
                .orElseThrow(() -> new RuntimeException("Default role ROLE_EMPLOYEE not found."));

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword())); // BCrypt encode
        user.setEmail(req.getEmail());
        user.setFullName(req.getFullName());
        user.setPhone(req.getPhone());
//        user.setCccd(req.getCccd());
        user.setRole(role);

        return mapEntityToResponse(userRepository.save(user));
    }

    public UserResponse registerHost(RegisterRequest req){
        return mapEntityToResponse(userRepository.save(creteUser(req,"ROLE_HOST")));
    }


    public User creteUser(RegisterRequest req,String roleNew){
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }
        if (req.getEmail() != null && userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        Role role = roleRepository.findByName(roleNew)
                .orElseThrow(() -> new RuntimeException("Default role "+ roleNew+ " not found."));
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword())); // BCrypt encode
        user.setEmail(req.getEmail());
        user.setFullName(req.getFullName());
        user.setPhone(req.getPhone());
//        user.setCccd(req.getCccd());
        user.setRole(role);
        return user;
    };
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
                user.getCccd(),
                user.getRole()
        );
    }
}
