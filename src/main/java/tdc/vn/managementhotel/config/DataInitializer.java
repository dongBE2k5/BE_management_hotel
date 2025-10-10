package tdc.vn.managementhotel.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tdc.vn.managementhotel.entity.Role;
import tdc.vn.managementhotel.repository.RoleRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
//            if (!roleRepository.existsByName("ROLE_SUPERADMIN")) {
//                roleRepository.save(new Role(1L, "ROLE_SUPERADMIN"));
//            }
//            if (!roleRepository.existsByName("ROLE_ADMIN")) {
//                roleRepository.save(new Role(2L, "ROLE_ADMIN"));
//            }
//            if (!roleRepository.existsByName("ROLE_ADMIN")) {
//                roleRepository.save(new Role(3L, "ROLE_ADMIN"));
//            }
//            if (!roleRepository.existsByName("ROLE_USER")) {
//                roleRepository.save(new Role(4L, "ROLE_USER"));
//            }
            if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
                Role adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }

            if (roleRepository.findByName("ROLE_USER").isEmpty()) {
                Role userRole = new Role();
                userRole.setName("ROLE_USER");
                roleRepository.save(userRole);
            }if (roleRepository.findByName("ROLE_EMPLOYEE").isEmpty()) {
                Role userRole = new Role();
                userRole.setName("ROLE_EMPLOYEE");
                roleRepository.save(userRole);
            }
        };
    }
}
