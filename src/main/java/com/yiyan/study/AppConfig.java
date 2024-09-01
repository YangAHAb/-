package com.yiyan.study;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.yiyan.study.database.opengauss.OpengaussHelper;

@Configuration
public class AppConfig {

    @Bean
    public OpengaussHelper opengaussHelper() {
        return new OpengaussHelper();
    }
}
