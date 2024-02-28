package com.nocturnals.budget;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nocturnals.budget.controller.CategoryController;
import com.nocturnals.budget.db.dao.CategoryService;
import com.nocturnals.budget.db.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void CategoryController_save_returnSaved() throws Exception {
        Category category = new Category();
        category.setName("test");
        Mockito.when(categoryService.save(Mockito.any(Category.class))).thenReturn(category);
        String body = objectMapper.writeValueAsString(category);
        mockMvc.perform(post("/api/v1/category")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"));

        Mockito.verify(categoryService, Mockito.times(1)).save(Mockito.any(Category.class));

        Mockito.reset(categoryService);
    }

    // Finding a category by name that does not exist in the database should return null.
    @Test
    public void CategoryController_findByName_returnNull() throws Exception {
        String categoryName = "nonexistent";
        Mockito.when(categoryService.findByName(categoryName)).thenReturn(null);
        mockMvc.perform(get("/api/v1/category")
                        .param("category-name", categoryName))
                .andExpect(status().isNotFound());

        Mockito.verify(categoryService, Mockito.times(1)).findByName(categoryName);

        Mockito.reset(categoryService);
    }

    // Finding a category by id that does not exist in the database should return null.
    @Test
    public void CategoryController_findByNonExistingId_returnNull() throws Exception {
        Long id = 1L;
        Mockito.when(categoryService.findById(Mockito.any(Long.class))).thenReturn(null);
        mockMvc.perform(get("/api/v1/category")
                        .param("category-id", id.toString()))
                .andExpect(status().isNotFound());

        Mockito.verify(categoryService, Mockito.times(1)).findById(Mockito.any(Long.class));

        Mockito.reset(categoryService);
    }

    // Finding a category by name that exists in the database should return the corresponding category.
    @Test
    public void CategoryController_findByName_returnExistingCategory() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("test");
        Mockito.when(categoryService.findByName(Mockito.anyString())).thenReturn(category);
        mockMvc.perform(get("/api/v1/category")
                        .param("category-name", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test"));

        Mockito.verify(categoryService, Mockito.times(1)).findByName(Mockito.anyString());

        Mockito.reset(categoryService);
    }

    // Finding a category by id that exists in the database should return the corresponding category.
    @Test
    public void CategoryController_findById_returnExistingCategory() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("test");
        Mockito.when(categoryService.findById(Mockito.anyLong())).thenReturn(category);
        mockMvc.perform(get("/api/v1/category")
                        .param("category-id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test"));

        Mockito.verify(categoryService, Mockito.times(1)).findById(Mockito.anyLong());

        Mockito.reset(categoryService);
    }

    // Saving an invalid Category object should return an error.
    @Test
    public void testSaveInvalidCategory() throws Exception {
        Category category = new Category();
        // Set invalid data for the category object
        category.setName(null);
        Mockito.when(categoryService.save(Mockito.any(Category.class))).thenThrow(DataIntegrityViolationException.class);
        String body = objectMapper.writeValueAsString(category);
        mockMvc.perform(post("/api/v1/category")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());

        Mockito.verify(categoryService, Mockito.times(1)).save(Mockito.any(Category.class));

        Mockito.reset(categoryService);
    }

    // Saving a valid Category object with a non-existing name should return the saved category.
    @Test
    public void testSaveValidCategoryWithNonExistingName() throws Exception {
        Category category = new Category();
        category.setName("test");
        Mockito.when(categoryService.save(Mockito.any(Category.class))).thenReturn(category);
        String body = objectMapper.writeValueAsString(category);
        mockMvc.perform(post("/api/v1/category")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"));

        Mockito.verify(categoryService, Mockito.times(1)).save(Mockito.any(Category.class));

        Mockito.reset(categoryService);
    }

    // When finding a category by ID, the CategoryController should return the category with the matching ID and a status code of 200
    @Test
    public void CategoryController_findById_returnCategory() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("test");
        Mockito.when(categoryService.findById(Mockito.anyLong())).thenReturn(category);
        mockMvc.perform(get("/api/v1/category")
                        .param("category-id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test"));

        Mockito.verify(categoryService, Mockito.times(1)).findById(Mockito.anyLong());

        Mockito.reset(categoryService);
    }
}
