package com.sofiiaivanytska.goaltrackerdemo.controller;

import com.sofiiaivanytska.goaltrackerdemo.dto.CategoryCreateDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController // @Controller + @ResponseBody
//@Controller
@RequestMapping("/api")
public class ApiController {

    //@GetMapping("/{category_id}")
    @GetMapping("/")
    //@ResponseBody
    public String sayHello(@PathVariable("category_id") Long category_id) {
        return "Hello category id, " + category_id;
    }

    @GetMapping("/categories")
    //@ResponseBody
    public CategoryCreateDto getCategories() {
        return CategoryCreateDto.builder()
                .name ("My category")
                .build();

    }

    @PostMapping
    public String handlePost() {
        return "redirect:/"; // POST -> REDIRECT -> GET
    }

}
