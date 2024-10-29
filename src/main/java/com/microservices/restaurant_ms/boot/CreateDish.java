package com.microservices.restaurant_ms.boot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.restaurant_ms.attachments.Attachment;
import com.microservices.restaurant_ms.attachments.AttachmentRepository;
import com.microservices.restaurant_ms.categories.Category;
import com.microservices.restaurant_ms.categories.CategoryRepository;
import com.microservices.restaurant_ms.dishes.Dish;
import com.microservices.restaurant_ms.dishes.DishRepository;
import com.microservices.restaurant_ms.ingredients.Ingredient;
import com.microservices.restaurant_ms.ingredients.IngredientRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Order(3)
public class CreateDish implements CommandLineRunner {

  @Autowired
  private AttachmentRepository attachmentRepository;

  @Autowired
  private DishRepository dishRepository;

  @Autowired
  private IngredientRepository ingredientRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Override
  public void run(String... args) throws Exception {

    if(this.dishRepository.count() == 0) {

      try {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<DishToCreate>> typeReference = new TypeReference<List<DishToCreate>>() {};

        InputStream inputStream = getClass().getResourceAsStream("/data/dishes.json");

        List<DishToCreate> dishes = mapper.readValue(inputStream, typeReference);

        dishes.forEach(dish -> {
          Optional<Category> category = this.categoryRepository.findByNameContainingIgnoreCase(dish.getCategory());
          Optional<Attachment> attachment = this.attachmentRepository.findByNameContainingIgnoreCase(dish.getName().replace(" ", "-"));
          Dish dishEntity = Dish.builder()
            .name(dish.getName())
            .description(dish.getDescription())
            .price(dish.getPrice())
            .category(category.get())
            .attachment(attachment.get()).build();

          List<Ingredient> ingredients = new ArrayList<>();

          for (String ingredient : dish.getIngredients()) {
            ingredients.add(
              Ingredient.builder()
                        .name(ingredient)
                        .dish(dishEntity)
                        .build()
            );
          }

          this.dishRepository.save(dishEntity);
          this.ingredientRepository.saveAll(ingredients);
        });




      } catch (IOException e) {
        System.out.println(e);
      }
    }
  }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class DishToCreate {
  private String name;
  private int price;
  private String description;
  List<String> ingredients;
  private String category;
}

