package com.example;

import com.example.soap.ProductClient;
import com.example.soap.product.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EnableDiscoveryClient
@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

}

@RestController
class ServiceInstanceRestController {

    private final DiscoveryClient discoveryClient;
    private final ProductClient productClient;

    @Autowired
    public ServiceInstanceRestController(DiscoveryClient discoveryClient, ProductClient productClient) {
        this.discoveryClient = discoveryClient;
        this.productClient = productClient;
    }

    @RequestMapping("/actuator/info")
    public String actuatorInfo() {
        return "Hello I am clientservice!";
    }

    @RequestMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(
            @PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }

    @RequestMapping("/products/{productId}")
    public ProductModel getProduct(
            @PathVariable Integer productId) {

        List<ServiceInstance> instances
                = discoveryClient.getInstances("soapservice");
        ServiceInstance instance
                = instances.stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("not found"));

        String defaultUri = instance.getUri().toString() + "/ws";
        System.out.println("Request from: " + defaultUri);


        ProductModel productModel = productClient.getProductById(productId, defaultUri);


        return productModel;
    }
}
