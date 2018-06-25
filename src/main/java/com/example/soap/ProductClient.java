package com.example.soap;

import com.example.soap.product.GetProductByIdRequest;
import com.example.soap.product.GetProductByIdResponse;
import com.example.soap.product.ObjectFactory;
import com.example.soap.product.ProductModel;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;

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

        Wss4jSecurityInterceptor wss4jSecurityInterceptor = new Wss4jSecurityInterceptor();
        wss4jSecurityInterceptor.setSecurementActions("Timestamp UsernameToken");
        wss4jSecurityInterceptor.setSecurementUsername("admin");
        wss4jSecurityInterceptor.setSecurementPassword("secret");
        webServiceTemplate.setInterceptors(new ClientInterceptor[]{wss4jSecurityInterceptor});
    }


    public ProductModel getProductById(Integer id, String defaultUri) {
        ObjectFactory factory = new ObjectFactory();

        GetProductByIdRequest request = factory.createGetProductByIdRequest();
        request.setId(id);

        webServiceTemplate.setDefaultUri(defaultUri);
        GetProductByIdResponse response = (GetProductByIdResponse) webServiceTemplate.marshalSendAndReceive(request);

        return response.getProduct();
    }

}
