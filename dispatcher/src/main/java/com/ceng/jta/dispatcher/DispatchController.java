package com.ceng.jta.dispatcher;

import com.ceng.jta.dispatcher.dto.RequestEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/dispatch")
public class DispatchController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @PostMapping("/")
    @Transactional
    public ResponseEntity dispatch(@RequestBody RequestEntity request) {
        RestTemplate restTemplate = new RestTemplate();

        String userServiceUrl = getUri("user-service");
        ResponseEntity<JsonNode> createUserResponse = restTemplate.postForEntity(userServiceUrl + "/api/v1/user/", request.getBody(), JsonNode.class);
        String userId = createUserResponse.getBody().get("id").asText();

        String productServiceUrl = getUri("product-service");
        ((ObjectNode) request.getBody()).put("createdBy", userId);
        ResponseEntity<JsonNode> createProductResponse = restTemplate.postForEntity(productServiceUrl + "/api/v1/product/", request.getBody(), JsonNode.class);
        String productId = createProductResponse.getBody().get("id").asText();

        ResponseEntity<JsonNode> user = restTemplate.getForEntity(userServiceUrl+ "/api/v1/user/" + userId, JsonNode.class);
        ResponseEntity<JsonNode> product = restTemplate.getForEntity(productServiceUrl+ "/api/v1/product/" + productId, JsonNode.class);

        return product;
    }

    @GetMapping("/{productId}")
    public ResponseEntity get(@PathVariable String productId){
        RestTemplate restTemplate = new RestTemplate();
        String productServiceUrl = getUri("product-service");

        ResponseEntity<JsonNode> product = restTemplate.getForEntity(productServiceUrl+ "/api/v1/product/" + productId, JsonNode.class);
        return product;
    }

    private String getUri(String serviceId) {
        return discoveryClient.getInstances(serviceId).get(0).getUri().toString();
    }

}
