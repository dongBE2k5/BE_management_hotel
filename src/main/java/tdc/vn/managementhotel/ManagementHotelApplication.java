package tdc.vn.managementhotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {
		"tdc.vn.managementhotel",
})
@EnableJpaAuditing


public class ManagementHotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementHotelApplication.class, args);
	}

}
