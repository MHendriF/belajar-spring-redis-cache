package hendrifbs.cache.controller;

import hendrifbs.cache.model.CategoryResponse;
import hendrifbs.cache.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping(value = "/api/categories", produces = "application/json")
    public List<CategoryResponse> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/")
    public String hello() {
        return "Hello Spring Boot!";
    }
}