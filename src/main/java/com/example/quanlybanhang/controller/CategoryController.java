package com.example.quanlybanhang.controller;

import com.example.quanlybanhang.models.Category;
import com.example.quanlybanhang.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping("/get-all-category")
    public ResponseEntity<Object> getAllCategory() {
        List<Category> list = categoryRepository.findAll();
        if (CollectionUtils.isEmpty(list)) list = new ArrayList<>();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/create-category")
    public ResponseEntity<Object> createCategory(@RequestBody Category category) {
        categoryRepository.save(category);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-category")
    public ResponseEntity<Object> deleteCategory(@RequestParam Long idCategory) {
        categoryRepository.deleteById(idCategory);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update-category")
    public ResponseEntity<Object> updateCategory(@RequestParam Long idCategory, @RequestBody Category category) {
        Optional<Category> categoryOptional = categoryRepository.findById(idCategory);
        if (categoryOptional.isPresent()) {
            categoryOptional.get().setContent(category.getContent());
            categoryOptional.get().setUpdatedAt(new Date());
            categoryRepository.save(categoryOptional.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
