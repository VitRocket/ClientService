package com.example.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/actuator")
public class ActuatorApi {

    private final DiscoveryClient discoveryClient;

    @Autowired
    public ActuatorApi(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @RequestMapping("/info")
    public String actuatorInfo() {
        return "Hello I am clientservice!";
    }

    @RequestMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(
            @PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }

}
