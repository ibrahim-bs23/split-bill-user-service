package com.brainstation23.skeleton.presenter.rest.api;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeResource extends BaseResource {

    @GetMapping
    public String index() {
        return "Brain Station 23 Ltd. Skeleton service is running...!";
    }

}
