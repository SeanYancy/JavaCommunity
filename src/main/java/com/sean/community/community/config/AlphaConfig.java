package com.sean.community.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class AlphaConfig {

    @Bean
    public SimpleDateFormat implDateFormat(){
        return new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
    }
}
