//package tdc.vn.managementhotel.security;
//
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//
//@Configuration
//public class Security {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable()) // Tắt CSRF để test POST từ Postman
//            .authorizeHttpRequests(auth -> auth
//                .anyRequest().permitAll() // Mở toàn bộ endpoint
//            );
//        return http.build();
//    }
//
////	@Bean
////	public PasswordEncoder passwordEncoder() {
////	    return new BCryptPasswordEncoder();
////	}
////	@Bean
////	public UserDetailsService userDetailsService (PasswordEncoder encoder) {
////		UserDetails admin = User.builder()
////				.username("admin")
////				.password(encoder.encode("123456"))
////				.roles("ADMIN").build();
////		UserDetails user = User.builder()
////				.username("user")
////				.password(encoder.encode("123456"))
////				.roles("USER").build();
////		UserDetails staff = User.builder()
////				.username("staff")
////				.password(encoder.encode("123456"))
////				.roles("STAFF").build();
////
////		UserDetails host = User.builder()
////				.username("Host")
////				.password(encoder.encode("123456"))
////				.roles("HOST").build();
////
////		return new InMemoryUserDetailsManager(admin,user,staff,host);
////	}
//}
