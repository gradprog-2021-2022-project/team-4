package com.irongroup.teamproject.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CollectionConfig {

    @Bean
    public List<String> nameList(){
        return Arrays.asList("Preppy","Street style","Classic","Comfy","Streetwear","Casual","Y2K","Cute");
    }


    @Bean
    public List<String> typeList(){ return Arrays.asList("Broek","Sokken","Trui","T-shirt","Sieraad","Schoenen");}
}
