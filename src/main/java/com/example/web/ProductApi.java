package com.example.web;

import com.example.soap.ProductClient;
import com.example.soap.product.ProductModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductApi {

    private final DiscoveryClient discoveryClient;
    private final ProductClient productClient;

    @Autowired
    public ProductApi(DiscoveryClient discoveryClient, ProductClient productClient) {
        this.discoveryClient = discoveryClient;
        this.productClient = productClient;
    }

    @ApiOperation(
            value = "Retrieves a fully product",
            notes = "Retrieves fully product given product id")
    @GetMapping(
            value = "/products/{productId}",
            produces = "application/json; charset=utf-8")
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
