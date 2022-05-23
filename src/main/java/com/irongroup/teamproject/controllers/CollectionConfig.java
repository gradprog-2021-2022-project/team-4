package com.irongroup.teamproject.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CollectionConfig {

    @Bean
    public List<String> nameList(){
        return Arrays.asList("Modern","Klassiek","Goth");
    }

}
