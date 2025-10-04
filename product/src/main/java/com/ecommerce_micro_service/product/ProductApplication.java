package com.ecommerce_micro_service.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

<<<<<<< HEAD
import java.util.TimeZone;

@SpringBootApplication
public class ProductApplication {


	public static void main(String[] args) {
// Set the default timezone before Spring Boot starts
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        SpringApplication.run(ProductApplication.class, args);
=======
@SpringBootApplication
public class ProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
>>>>>>> 45ce038add230c33c78ad8d28ef5fcb717f61b0c
	}

}
