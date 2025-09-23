package tdc.vn.managementhotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"tdc.vn.managementhotel",
})

public class ManagementHotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementHotelApplication.class, args);
	}

}
