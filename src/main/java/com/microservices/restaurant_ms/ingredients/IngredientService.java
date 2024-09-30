package com.microservices.restaurant_ms.ingredients;

import com.microservices.restaurant_ms.dishes.Dish;
import com.microservices.restaurant_ms.ingredients.dto.CreateIngredientsDTO;
import com.microservices.restaurant_ms.ingredients.dto.UpdateIngredientsDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IngredientService {

  @Autowired
  private IngredientRepository ingredientRepository;

  public List<Ingredient> create(CreateIngredientsDTO createIngredientsDTO, Dish dish) {

    List<Ingredient> ingredients = new ArrayList<>();

    for (String ingredient : createIngredientsDTO.name()) {
      ingredients.add(
        Ingredient.builder()
                  .name(ingredient)
                  .dish(dish)
                  .build()
      );
    }

    return this.ingredientRepository.saveAll(ingredients).stream().toList();
  }

  @Async
  @Transactional
  public void updateIngredientsForDish(
    UpdateIngredientsDTO updateIngredientsDTO,
    Dish dish
  ) {

    List<Ingredient> newIngredients = updateIngredientsDTO.name()
                                                          .stream()
                                                          .map((name) -> Ingredient.builder()
                                                                                   .name(name)
                                                                                   .dish(dish)
                                                                                   .build())
                                                          .toList();

    for(Ingredient newIngredient : newIngredients) {
      this.ingredientRepository.findByNameAndDishId(newIngredient.getName(), dish.getId())
        .orElseGet(() -> this.ingredientRepository.save(newIngredient));
    }

    this.ingredientRepository.deleteByDishIdAndNameNotIn(dish.getId(), updateIngredientsDTO.name());

  }
}
