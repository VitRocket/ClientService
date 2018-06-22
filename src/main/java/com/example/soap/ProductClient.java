package com.example.soap;

import com.example.soap.product.ObjectFactory;
import com.example.soap.product.ProductModel;
import com.example.soap.product.ProductRequest;
import com.example.soap.product.ProductResponse;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.annotation.PostConstruct;

@Component
public class ProductClient {

    private final static String CONTEXT_PATH = "com.example.soap.product";

    private WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

    @PostConstruct
    private void init() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(CONTEXT_PATH);

        webServiceTemplate.setMarshaller(marshaller);
        webServiceTemplate.setUnmarshaller(marshaller);
    }


    public ProductModel getProductById(Integer id, String defaultUri) {
        ObjectFactory factory = new ObjectFactory();

        ProductRequest request = factory.createProductRequest();
        request.setId(id);

        webServiceTemplate.setDefaultUri(defaultUri);
        ProductResponse response = (ProductResponse) webServiceTemplate.marshalSendAndReceive(request);

        return response.getProduct();
    }

}
