package com.sofiiaivanytska.goaltrackerdemo.controller;

import com.sofiiaivanytska.goaltrackerdemo.dto.CategoryCreateDto;
import com.sofiiaivanytska.goaltrackerdemo.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SsrController { // for http request handling

    private MyService myService;

    @Autowired
    public SsrController(MyService myService) {
        this.myService = myService;
    }

    @GetMapping
    public String index(Model model) {

        CategoryCreateDto categories = myService.getCategories();

        model.addAttribute("categories", categories);

        return "index";
    }
}
