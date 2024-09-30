package com.microservices.restaurant_ms.dishes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DishRepository extends JpaRepository<Dish, UUID> {
  Page<Dish> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
      String name,
      String description,
      Pageable pageable
  );
}
