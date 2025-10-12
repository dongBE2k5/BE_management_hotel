package tdc.vn.managementhotel;

import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(scanBasePackages = {
		"tdc.vn.managementhotel",
})
@EnableJpaAuditing
@EnableScheduling

public class ManagementHotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementHotelApplication.class, args);
    }

}
