package com.yiyan.study;

import com.fasterxml.jackson.core.JsonProcessingException;
// import com.yiyan.study.model.DesensitizationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 启动类
 */
@SpringBootApplication
@Slf4j
@RestController
public class DesensitizationApplication {
    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(DesensitizationApplication.class, args);
    }
}
