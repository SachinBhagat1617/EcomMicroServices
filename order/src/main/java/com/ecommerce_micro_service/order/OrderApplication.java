package com.ecommerce_micro_service.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD
import java.util.TimeZone;

@SpringBootApplication
public class OrderApplication {
    public static void main(String[] args) {
        // Set the default timezone before Spring Boot starts
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        SpringApplication.run(OrderApplication.class, args);
    }
}
=======

@SpringBootApplication
public class OrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

}
>>>>>>> 45ce038add230c33c78ad8d28ef5fcb717f61b0c
