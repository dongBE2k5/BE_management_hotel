package tdc.vn.managementhotel.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tdc.vn.managementhotel.entity.HotelPaymentType;
import tdc.vn.managementhotel.entity.PaymentTypes;
import tdc.vn.managementhotel.entity.Role;
import tdc.vn.managementhotel.enums.PaymentType;
import tdc.vn.managementhotel.repository.HotelPaymentTypeRepository;
import tdc.vn.managementhotel.repository.PaymentTypeRepository;
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
            if (roleRepository.findByName("ROLE_CLEANING").isEmpty()) {
                Role userRole = new Role();
                userRole.setName("ROLE_CLEANING");
                roleRepository.save(userRole);
            } if (roleRepository.findByName("ROLE_HOST").isEmpty()) {
                Role userRole = new Role();
                userRole.setName("ROLE_HOST");
                roleRepository.save(userRole);
            }
        };
    }
    @Bean
    CommandLineRunner initPaymentType(PaymentTypeRepository paymentTypeRepository, HotelPaymentTypeRepository hotelPaymentTypeRepository) {
        return args -> {
//
            if (paymentTypeRepository.findByPaymentType(PaymentType.FULL).isEmpty()) {
                PaymentTypes paymentType = new PaymentTypes();
                paymentType.setPaymentType(PaymentType.FULL);
                paymentTypeRepository.save(paymentType);
            }

            if (paymentTypeRepository.findByPaymentType(PaymentType.DEPOSIT).isEmpty()) {
                PaymentTypes paymentType = new PaymentTypes();
                paymentType.setPaymentType(PaymentType.DEPOSIT);
                paymentTypeRepository.save(paymentType);
            }
            PaymentTypes paymentType =  paymentTypeRepository.findById(1L).orElse(null);
            if (hotelPaymentTypeRepository.findById(1L).isEmpty() && paymentType != null) {
                HotelPaymentType hotelPaymentType = new HotelPaymentType();
                hotelPaymentType.setDepositPercent(100.0);
                hotelPaymentType.setPaymentType(paymentType);
                hotelPaymentTypeRepository.save(hotelPaymentType);
            }

        };
    }

    @Bean
    CommandLineRunner initHotelPaymentType(HotelPaymentTypeRepository hotelPaymentTypeRepository, PaymentTypeRepository paymentTypeRepository) {
        return args -> {

        };
    }
}
