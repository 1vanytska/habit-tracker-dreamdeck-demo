package com.htdd.habittrackerdreamdeckdemo.category;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getCategoriesForUser_Positive_ShouldReturnList() {
        UUID userId = UUID.randomUUID();
        Category cat1 = new Category();
        Category cat2 = new Category();
        when(categoryRepository.findByUserId(userId)).thenReturn(List.of(cat1, cat2));

        List<Category> result = categoryService.getCategoriesForUser(userId);

        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getCategoriesForUser_Negative_ShouldReturnEmptyListWhenNoCategories() {
        UUID userId = UUID.randomUUID();
        when(categoryRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        List<Category> result = categoryService.getCategoriesForUser(userId);

        assertTrue(result.isEmpty());
        verify(categoryRepository, times(1)).findByUserId(userId);
    }

    @Test
    void createCategory_ShouldSetUserId() {
        UUID userId = UUID.randomUUID();
        Category category = new Category();
        category.setName("Health");

        when(categoryRepository.save(category)).thenAnswer(invocation -> invocation.getArgument(0));

        Category result = categoryService.createCategory(category, userId);

        assertEquals(userId, result.getUserId());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void deleteCategory_Positive_ShouldDeleteWhenOwnedByUser() {
        UUID userId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        Category category = new Category();
        category.setId(categoryId);
        category.setUserId(userId);

        when(categoryRepository.findByIdAndUserId(categoryId, userId)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(categoryId, userId);

        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void deleteCategory_Negative_ShouldThrowWhenNotFound() {
        UUID userId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        when(categoryRepository.findByIdAndUserId(categoryId, userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoryService.deleteCategory(categoryId, userId));
        verify(categoryRepository, never()).delete(any());
    }
}
