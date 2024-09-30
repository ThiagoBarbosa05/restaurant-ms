package com.microservices.restaurant_ms.ingredients.dto;

import java.util.List;

public record CreateIngredientsDTO(
  List<String> name
) {

}
