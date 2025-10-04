package com.ecommerce_micro_service.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class FallbackController {
    //@GetMapping("/fallback/products")
    @RequestMapping(value = "/fallback/products", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<List<String>> productFallback(){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Collections.singletonList("Product service is unavailable, Please try after some time"));
    }
//    @GetMapping("/fallback/users")
    @RequestMapping(value = "/fallback/users", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<List<String>> userFallback(){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Collections.singletonList("User Service is unavailable, Please try after some time"));
    }
    //@GetMapping("/fallback/orders")
    @RequestMapping(value = "/fallback/orders", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<List<String>> orderFallback(){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Collections.singletonList("Order Service is unavailable, Please try after some time"));
    }
    //@GetMapping("/fallback/eureka-server")
    @RequestMapping(value = "/fallback/eureka-server", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<List<String>> eurekaFallback(){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Collections.singletonList("Eureka Server is unavailable, Please try after some time"));
    }
}
