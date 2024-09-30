package com.microservices.restaurant_ms.ingredients;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {

  Optional<Ingredient> findByNameAndDishId(String name, UUID dishId);

  List<Ingredient> findByDishId(UUID dishId);

  void deleteByDishIdAndNameNotIn(UUID dishId, List<String> names);

}
