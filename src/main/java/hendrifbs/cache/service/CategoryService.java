package hendrifbs.cache.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hendrifbs.cache.entity.Category;
import hendrifbs.cache.model.CategoryResponse;
import hendrifbs.cache.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final StringRedisTemplate stringRedisTemplate;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public List<CategoryResponse> findAll() {

        // check if categories is cached in redis
        String json = stringRedisTemplate.opsForValue().get("categories");
        if (json != null) {
            List<CategoryResponse> responses = objectMapper.readValue(json, new TypeReference<>() {
            });
            return responses;
        }

        // find all parent categories
        List<Category> parents = categoryRepository.findAllForAPI();
        List<CategoryResponse> responses = parents.stream().map(category -> {

            List<Category> children = category.getChildren();
            List<CategoryResponse> childrenResponses = children.stream().map(child -> {
                return CategoryResponse.builder().id(child.getId()).name(child.getName()).build();
            }).toList();

            return CategoryResponse.builder().id(category.getId()).name(category.getName()).children(childrenResponses).build();
        }).toList();

        json = objectMapper.writeValueAsString(responses);
        stringRedisTemplate.opsForValue().set("categories", json, Duration.ofHours(1));

        return responses;
    }

}