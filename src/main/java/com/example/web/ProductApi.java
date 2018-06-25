package com.example.web;

import com.example.soap.ProductClient;
import com.example.soap.product.ProductModel;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductApi {

    private final ProductClient productClient;

    @ApiOperation(
            value = "Retrieves a fully product",
            notes = "Retrieves fully product given product id")
    @GetMapping(
            value = "/products/{productId}",
            produces = "application/json; charset=utf-8")
    public ProductModel getProduct(
            @PathVariable Integer productId) {

        ProductModel productModel = productClient.getProductById(productId);
        log.info(productModel.toString());
        return productModel;
    }


}
