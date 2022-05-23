package com.irongroup.teamproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CollectionsBean {
    @Autowired
    private List<String> nameList;

    public void printStyles(){
        System.out.println(nameList);
    }
}
