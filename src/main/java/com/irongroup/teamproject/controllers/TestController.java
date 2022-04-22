package com.irongroup.teamproject.controllers;

import com.irongroup.teamproject.model.Test;
import com.irongroup.teamproject.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @Autowired
    private TestRepository testRepository;

    @GetMapping("/test")
    public String test(Model model){
        Iterable<Test> data = testRepository.findAll();
        model.addAttribute("data", data);
        return "test";
    }
}
