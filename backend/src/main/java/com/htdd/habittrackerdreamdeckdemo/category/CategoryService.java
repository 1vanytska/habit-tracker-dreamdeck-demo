package com.htdd.habittrackerdreamdeckdemo.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getCategoriesForUser(UUID userId) {
        return categoryRepository.findByUserId(userId);
    }

    public Category createCategory(Category category, UUID userId) {
        category.setUserId(userId);
        return categoryRepository.save(category);
    }

    public void deleteCategory(UUID id, UUID userId) {
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Категорію не знайдено"));
        categoryRepository.delete(category);
    }
}
