package tdc.vn.managementhotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.entity.User;
import tdc.vn.managementhotel.model.CustomUserDetails;
import tdc.vn.managementhotel.repository.UserRepository;


//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
//
//        // lấy role name (ví dụ: "ROLE_ADMIN")
//        String roleName = user.getRole().getName();
//
//        return org.springframework.security.core.userdetails.User
//                .withUsername(user.getUsername())
//                .password(user.getPassword()) // password đã BCrypt
//                .roles(roleName.replace("ROLE_", ""))
//                // ⚠️ chú ý: .roles() tự động thêm prefix "ROLE_"
//                // Nếu roleName trong DB đã là "ROLE_ADMIN" thì ta cần bỏ prefix để không bị "ROLE_ROLE_ADMIN"
//                .build();
//    }
//}
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // ✅ Trả về CustomUserDetails thay vì User mặc định
        return new CustomUserDetails(user);
    }
}
