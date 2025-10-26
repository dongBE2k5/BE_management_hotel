package tdc.vn.managementhotel.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.entity.PasswordResetToken;
import tdc.vn.managementhotel.entity.User;
import tdc.vn.managementhotel.repository.PasswordResetTokenRepository;
import tdc.vn.managementhotel.repository.UserRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PasswordResetService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    // Gửi mã OTP qua email
    public String sendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email: " + email));

        // Tạo OTP ngẫu nhiên 6 chữ số
        String otpCode = String.format("%06d", new Random().nextInt(999999));

        // Lưu OTP vào DB
        PasswordResetToken token = tokenRepository.findByEmail(email).orElse(new PasswordResetToken());
        token.setEmail(email);
        token.setOtpCode(otpCode);
        token.setExpiryTime(LocalDateTime.now().plusMinutes(5)); // Hết hạn sau 5 phút
        tokenRepository.save(token);

        // Gửi email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Mã OTP đặt lại mật khẩu");
        message.setText("Mã OTP của bạn là: " + otpCode + "\nCó hiệu lực trong 5 phút.");
        mailSender.send(message);

        return "Đã gửi mã OTP đến email " + email;
    }

    // Xác minh OTP
    public String verifyOtp(String email, String otp) {
        PasswordResetToken token = tokenRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu đặt lại mật khẩu cho email này."));

        if (token.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Mã OTP đã hết hạn.");
        }
        if (!token.getOtpCode().equals(otp)) {
            throw new RuntimeException("Mã OTP không chính xác.");
        }

        return "Xác minh OTP thành công!";
    }
}
