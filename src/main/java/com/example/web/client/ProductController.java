package com.example.web.client;

import com.example.soap.ProductClient;
import com.example.soap.product.ProductModel;
import com.example.web.form.ProductForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductClient productClient;

    @GetMapping({"/product"})
    public String index(Model model) {
        try {
            List<ProductModel> products = productClient.getAllProducts();
            model.addAttribute("productsDto", products);
            model.addAttribute("productForm", new ProductForm());
        } catch (RuntimeException e) {
            model.addAttribute("productsDto", Collections.emptyList());
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "product-all";
    }

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable("id") Integer id, Model model) {
        ProductModel productModel = productClient.getProductById(id);
        ProductForm productForm = new ProductForm();
        log.info(productModel.toString());
        BeanUtils.copyProperties(productModel, productForm);
        log.info(productForm.toString());
        model.addAttribute("productForm", productForm);
        return "product-form-update";
    }

    @GetMapping("/product/new")
    public String getProductNew(Model model) {
        ProductForm productForm = new ProductForm();
        model.addAttribute("productForm", productForm);
        return "product-form-add";
    }

    @PostMapping("/product")
    public String addProduct(Model model, ProductForm productForm) {
        ProductModel productModel = new ProductModel();
        productModel.setName(productForm.getName());
        productModel.setDescription(productForm.getDescription());
        try {
            productModel = productClient.addProduct(productModel);
            productForm.setId(productModel.getId());
            model.addAttribute("productForm", productForm);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "product-form-add";
    }

    @PutMapping("/product")
    public String updateProduct(Model model, ProductForm productForm) {
        log.info(productForm.toString());
        ProductModel productModel = new ProductModel();
        BeanUtils.copyProperties(productForm, productModel);
        try {
            productModel = productClient.updateProduct(productModel);
            productForm.setId(productModel.getId());
            model.addAttribute("productForm", productForm);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "product-form-update";
    }

}
