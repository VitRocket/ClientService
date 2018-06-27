package com.example.soap;

import com.example.soap.product.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductClient {

    private final static String CONTEXT_PATH = "com.example.soap.product";

    private final DiscoveryClient discoveryClient;
    private WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

    @PostConstruct
    private void init() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(CONTEXT_PATH);

        webServiceTemplate.setMarshaller(marshaller);
        webServiceTemplate.setUnmarshaller(marshaller);

        Wss4jSecurityInterceptor wss4jSecurityInterceptor = new Wss4jSecurityInterceptor();
        wss4jSecurityInterceptor.setSecurementActions("Timestamp UsernameToken");
        wss4jSecurityInterceptor.setSecurementUsername("admin");
        wss4jSecurityInterceptor.setSecurementPassword("secret");
        webServiceTemplate.setInterceptors(new ClientInterceptor[]{wss4jSecurityInterceptor});
    }

    private String getUri() {
        List<ServiceInstance> instances = discoveryClient.getInstances("soapservice");
        ServiceInstance instance = instances.stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("not found service"));
        String defaultUri = instance.getUri().toString() + "/ws";
        log.info("Request from: " + defaultUri);
        return defaultUri;
    }

    public ProductModel getProductById(Integer id) {
        webServiceTemplate.setDefaultUri(getUri());

        ObjectFactory factory = new ObjectFactory();

        GetProductByIdRequest request = factory.createGetProductByIdRequest();
        request.setId(id);
        GetProductByIdResponse response = (GetProductByIdResponse) webServiceTemplate.marshalSendAndReceive(request);

        return response.getProduct();
    }

    public List<ProductModel> getAllProducts() {
        webServiceTemplate.setDefaultUri(getUri());

        ObjectFactory factory = new ObjectFactory();
        GetAllProductsRequest request = factory.createGetAllProductsRequest();
        GetAllProductsResponse response = (GetAllProductsResponse) webServiceTemplate.marshalSendAndReceive(request);

        return response.getProducts();
    }

    public ProductModel addProduct(ProductModel productModel) throws Exception {
        webServiceTemplate.setDefaultUri(getUri());

        ObjectFactory factory = new ObjectFactory();
        AddProductRequest request = factory.createAddProductRequest();
        request.setName(productModel.getName());
        request.setDescription(productModel.getDescription());
        AddProductResponse response = (AddProductResponse) webServiceTemplate.marshalSendAndReceive(request);

        ServiceStatus status = response.getServiceStatus();
        if ("SUCCESS".equals(status.getStatusCode())) {
            return response.getProduct();
        } else if ("FAIL".equals(status.getStatusCode())) {
            throw new Exception(status.getMessage());
        }
        throw new Exception("Sorry. Unknown exception.");
    }

    public ProductModel updateProduct(ProductModel productModel) throws Exception {
        webServiceTemplate.setDefaultUri(getUri());

        ObjectFactory factory = new ObjectFactory();
        UpdateProductRequest request = factory.createUpdateProductRequest();
        request.setProduct(productModel);
        UpdateProductResponse response = (UpdateProductResponse) webServiceTemplate.marshalSendAndReceive(request);

        ServiceStatus status = response.getServiceStatus();
        if ("SUCCESS".equals(status.getStatusCode())) {
            return productModel;
        } else if ("FAIL".equals(status.getStatusCode())) {
            throw new Exception(status.getMessage());
        }
        throw new Exception("Sorry. Unknown exception.");
    }
}
