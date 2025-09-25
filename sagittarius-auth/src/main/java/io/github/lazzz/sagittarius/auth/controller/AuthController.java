package io.github.lazzz.sagittarius.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @className AuthController 
 * @description TODO 
 * @author Lazzz 
 * @date 2025/09/19 20:15
**/
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @GetMapping("/test")
    public String test() {
        return "Server is running";
    }

}

