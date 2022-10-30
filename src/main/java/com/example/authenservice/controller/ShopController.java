package com.example.authenservice.controller;


import com.example.authenservice.producer.ShopAdminService;
import com.example.commonapi.model.*;
import org.hibernate.usertype.DynamicParameterizedType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
@RequestMapping(value = "admin/api")
public class ShopController {
    @Autowired
    private ShopAdminService shopAdminService;

    @Value("${shop.backend.url}")
    private String url;

    @Autowired
    private HttpSession httpSession;
    @Autowired
    private RestTemplate restTemplate;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "creatOrUpdateCategory",method = RequestMethod.POST)
    public ResultMessage createOrUpdateCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return shopAdminService.createOrUpdateCategory(categoryDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "creatOrUpdateProduct" ,method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON)
    public ResultMessage createOrUpdate(@RequestBody @Valid ProductDto productDto) {
        return shopAdminService.createOrUpdateProduct(productDto);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CUSTOMER')")
    @RequestMapping(value = "showAllCart" ,method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON)
    public ResultMessage<List<CartDto>> showAllOrder(@RequestBody CartRequest cartRequest) {
        cartRequest.setSession(httpSession.getId());
        return restTemplate.postForObject(url+"/endUser/api/addToCart",cartRequest, ResultMessage.class);
    }

}
