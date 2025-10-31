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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    private final Map<String, String> otpStorage = new HashMap<>();
    private final Map<String, LocalDateTime> otpExpiry = new HashMap<>();

    /**
     * Đăng ký user mới với role mặc định ROLE_USER
     */
    // ✅ Regex cho username & password
    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^(?=.{6,}$)(?=.*[A-Z])[A-Za-z][A-Za-z0-9_.\\-@!]*$");
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.{6,}$)(?=.*[A-Z])(?=.*[^A-Za-z0-9])\\S+$");
    private static final Pattern GMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@gmail\\.com$");

    public UserResponse register(RegisterRequest req) {
        if (req.getFullName() == null || req.getFullName().trim().isEmpty()) {
            throw new RuntimeException("Họ tên không được để trống.");
        }
        if (req.getUsername() == null || req.getUsername().trim().isEmpty()) {
            throw new RuntimeException("User name không được để trống.");
        }
        if (!USERNAME_PATTERN.matcher(req.getUsername()).matches()) {
            throw new RuntimeException(" Username không hợp lệ! " +
                    "Phải bắt đầu bằng chữ, có ít nhất 6 ký tự, chứa 1 chữ in hoa, không có khoảng trắng.");
        }
        if (req.getEmail() == null || req.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email không được để trống.");
        }
        if (!GMAIL_PATTERN.matcher(req.getEmail()).matches()) {
            throw new RuntimeException(" Email không hợp lệ! " +
                    "Chỉ chấp nhận email có đuôi @gmail.com và không chứa khoảng trắng.");
        }
        if (req.getPhone() == null || req.getPhone().trim().isEmpty()) {
            throw new RuntimeException("Số điện thoại không được để trống.");
        }
        if (req.getPassword() == null || req.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Mật khẩu không được để trống.");
        }
        // ⚙️ Kiểm tra password hợp lệ
        if (!PASSWORD_PATTERN.matcher(req.getPassword()).matches()) {
            throw new RuntimeException(" Mật khẩu chua " +
                    "Phải có ít nhất 6 ký tự, 1 chữ in hoa và 1 ký tự đặc biệt, không có khoảng trắng.");
        }


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
                user.getRole(),
                user.getGender(),
                user.getBirthDate(),
                user.getAddress()
        );
    }
    //ham doi mat khau
    public UserResponse changePassword(Long userId, String oldPassword, String newPassword) {
        // Tìm user theo ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với ID: " + userId));

        // Kiểm tra mật khẩu cũ có khớp không
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không đúng!");
        }

        // Không cho phép dùng lại mật khẩu cũ
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new RuntimeException("Mật khẩu mới không được trùng với mật khẩu cũ!");
        }

        // Kiểm tra định dạng mật khẩu mới
        if (!PASSWORD_PATTERN.matcher(newPassword).matches()) {
            throw new RuntimeException("Mật khẩu mới không hợp lệ! " +
                    "Phải có ít nhất 6 ký tự, 1 chữ in hoa và 1 ký tự đặc biệt, không có khoảng trắng.");
        }

        // Mã hóa mật khẩu mới
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return mapEntityToResponse(user);
    }
    //
    public String resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản với email: " + email));

        if (!PASSWORD_PATTERN.matcher(newPassword).matches()) {
            throw new RuntimeException("Mật khẩu mới không hợp lệ! " +
                    "Phải có ít nhất 6 ký tự, 1 chữ in hoa và 1 ký tự đặc biệt, không có khoảng trắng.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "Đặt lại mật khẩu thành công!";
    }
    public void sendOtpToEmail(String email) {
        // Kiểm tra email có tồn tại trong hệ thống không
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với email: " + email));

        // Tạo mã OTP ngẫu nhiên 6 chữ số
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Lưu OTP và thời gian hết hạn (5 phút)
        otpStorage.put(email, otp);
        otpExpiry.put(email, LocalDateTime.now().plusMinutes(5));

        // Gửi mail
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Mã OTP xác thực từ Management Hotel");
        message.setText("Xin chào " + user.getFullName() + ",\n\n"
                + "Mã OTP của bạn là: " + otp + "\n"
                + "Mã này sẽ hết hạn sau 5 phút.\n\n"
                + "Trân trọng,\nĐội ngũ Management Hotel");

        mailSender.send(message);
    }
    public boolean verifyOtp(String email, String otp) {
        if (!otpStorage.containsKey(email)) {
            return false;
        }

        String storedOtp = otpStorage.get(email);
        LocalDateTime expiry = otpExpiry.get(email);

        // Kiểm tra đúng mã và chưa hết hạn
        if (storedOtp.equals(otp) && expiry.isAfter(LocalDateTime.now())) {
            // Xóa OTP sau khi dùng
            otpStorage.remove(email);
            otpExpiry.remove(email);
            return true;
        }

        return false;
    }

}
