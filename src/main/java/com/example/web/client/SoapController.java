package com.example.web.client;

import com.example.soap.ProductClient;
import com.example.soap.product.ProductModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SoapController {

    private final DiscoveryClient discoveryClient;
    private final ProductClient productClient;

    @GetMapping({"/soap"})
    public String index(Model model) {
        try{
            List<ProductModel> products = productClient.getAllProducts();
            log.info(products.toString());
            model.addAttribute("productsDto", products);
            return "soap";
        }catch (RuntimeException e){
            model.addAttribute("productsDto", Collections.emptyList());
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "soap";
        }
    }
}
