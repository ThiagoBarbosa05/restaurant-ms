package com.microservices.restaurant_ms.dishes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateDishDTO(

  @NotBlank(message = "É preciso informar um nome para o prato")
  String name,

  @NotBlank(message = "É preciso informar a descrição do prato")
  String description,

  @NotNull(message = "É preciso informar o preço do prato")
  Double price,

  @NotNull(message = "É preciso informar o id da categoria do prato")
  UUID category_id,

  @NotNull(message = "É preciso informar o id da imagem do prato")
  UUID attachment_id,

  @NotEmpty(message = "Insira ao menos um ingrediente do prato")
  List<String> ingredients
) {
}
