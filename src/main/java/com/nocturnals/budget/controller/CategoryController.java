package com.nocturnals.budget.controller;

import com.nocturnals.budget.db.dao.CategoryService;
import com.nocturnals.budget.db.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/category")
public class CategoryController {
    CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Category category) {
        try {
            return ResponseEntity.ok(categoryService.save(category));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Category name must be unique");
        }
    }

    @GetMapping
    public ResponseEntity<Object> findByIdOrName(@RequestParam(value = "category-id", required = false) Long id,
                                                 @RequestParam(value = "category-name", required = false) String name) {
        Category category;
        if(id != null) {
            category = categoryService.findById(id);
            if(category != null && name != null) {
                if(!name.equalsIgnoreCase(category.getName())) {
                    return ResponseEntity.badRequest().body("Category name and id does not match");
                }
            }
        } else if (name != null) {
            category = categoryService.findByName(name);
        } else {
            return ResponseEntity.badRequest().body("Category id or name must be provided");
        }
        if(category == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(category);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        try {
            categoryService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
