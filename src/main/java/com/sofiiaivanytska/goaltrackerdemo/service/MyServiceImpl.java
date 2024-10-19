package com.sofiiaivanytska.goaltrackerdemo.service;

import com.sofiiaivanytska.goaltrackerdemo.dto.CategoryCreateDto;
import org.springframework.stereotype.Service;

@Service
public class MyServiceImpl implements MyService { // for business logic

    public CategoryCreateDto getCategories() {

        return CategoryCreateDto.builder()
                .name ("My category")
                .build();

    }
}
