package tdc.vn.managementhotel;

import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< Updated upstream
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
=======
import java.util.Base64;
import java.security.SecureRandom;
>>>>>>> Stashed changes

@SpringBootApplication(scanBasePackages = {
		"tdc.vn.managementhotel",
})
@EnableJpaAuditing


public class ManagementHotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementHotelApplication.class, args);
    }

}
