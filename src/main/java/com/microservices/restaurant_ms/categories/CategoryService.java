package com.microservices.restaurant_ms.categories;

import com.microservices.restaurant_ms.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;

  public Category findById(UUID id) {

    Optional<Category> category = this.categoryRepository.findById(id);

    if(category.isEmpty()) {
      throw new ResourceNotFoundException("Categoria não encontrada");
    }

    return category.get();
  }

}
