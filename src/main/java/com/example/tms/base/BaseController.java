package com.example.tms.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface BaseController {

    BaseService getService();

    @GetMapping("/hello")
    default ResponseEntity<String> hello(HttpServletRequest request) {
        return new ResponseEntity<>("Hello " + request.getRequestURI(), HttpStatus.OK);        
    }
}
