package tdc.vn.managementhotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
		"tdc.vn.managementhotel",
})
@EnableScheduling
public class ManagementHotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementHotelApplication.class, args);
	}

}
