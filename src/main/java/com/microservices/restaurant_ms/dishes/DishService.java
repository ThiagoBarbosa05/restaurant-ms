package com.microservices.restaurant_ms.dishes;

import com.microservices.restaurant_ms.attachments.Attachment;
import com.microservices.restaurant_ms.categories.Category;
import com.microservices.restaurant_ms.dishes.dto.CreateDishDTO;
import com.microservices.restaurant_ms.dishes.dto.UpdateDishDTO;
import com.microservices.restaurant_ms.exceptions.ResourceNotFoundException;
import com.microservices.restaurant_ms.storage.services.R2StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DishService {

  @Autowired
  private DishRepository dishRepository;

  @Autowired
  private R2StorageService r2StorageService;

  public Dish create(
    CreateDishDTO createDishDTO,
    Attachment attachment,
    Category category
  ) {

    double priceInCents = createDishDTO.price() * 100;
    int price = (int) priceInCents;

    Dish dish =  Dish.builder()
                     .name(createDishDTO.name())
                     .description(createDishDTO.description())
                     .price(price)
                     .attachment(attachment)
                     .category(category)
                     .build();


    return this.dishRepository.save(dish);
  }

  public Page<Dish> fetchAll(String query, int pageNumber, int pageSize) {

    Pageable pageable = PageRequest.of(pageNumber, pageSize);

    if(query != null) {
      return this.dishRepository.searchDishes(query, pageable);
    }

    return this.dishRepository.findAll(pageable);
  }

  public Dish getById(UUID dishId) {
    return this.dishRepository.findById(dishId).orElseThrow(() -> new ResourceNotFoundException(
      "Prato não encontrado"));
  }

  public Dish update(
    UpdateDishDTO updateDishDTO,
    Dish dish,
    Attachment attachment,
    Category category
    ) {

    double priceInCents = updateDishDTO.price() * 100;
    int price = (int) priceInCents;

    if(dish.getAttachment().getId() != updateDishDTO.attachment_id()) {
      this.r2StorageService.delete(dish.getAttachment());
    }

    dish.setName(updateDishDTO.name());
    dish.setDescription(updateDishDTO.description());
    dish.setPrice(price);
    dish.setAttachment(attachment);
    dish.setCategory(category);

    this.dishRepository.save(dish);

    return dish;
  }

  public void delete(Dish dish) {
    this.dishRepository.delete(dish);
  }
}
