package com.microservices.restaurant_ms.boot;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.restaurant_ms.categories.CategoryRepository;
import com.microservices.restaurant_ms.categories.Category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@Order(1)
@Slf4j
public class CreateCategories implements CommandLineRunner {

  @Autowired
  private CategoryRepository categoriesRepository;

  @Override
  public void run(String... args) throws Exception {
    if(categoriesRepository.count() == 0) {
      try {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Category>> typeReference = new TypeReference<List<Category>>(){};

        InputStream inputStream = getClass().getResourceAsStream("/data/categories.json");

        List<Category> categories = mapper.readValue(inputStream, typeReference);

        categories.stream().forEach(category  -> {
            category.setName(category.getName());
            categoriesRepository.save(category);
        });
        log.info(">>>" + categories.size() + "Categories saved!");

      } catch (IOException e) {
        log.info(">>> Unable to import categories" + e.getMessage());
      }
    }
  }
}





