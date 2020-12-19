package com.example.tms.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseControllerImpl implements BaseController {
    @Override
    public ResponseEntity<String> hello(HttpServletRequest request) {
        
        log.info("[REQUEST]" + request.getRequestURI());

        return new ResponseEntity<>("Hello " + request.getRequestURI(), HttpStatus.OK);
    }
}
