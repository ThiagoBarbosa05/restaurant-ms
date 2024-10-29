package com.microservices.restaurant_ms.ingredients;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservices.restaurant_ms.dishes.Dish;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "ingredients")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "dish_id")
  @JsonBackReference
  private Dish dish;

}
