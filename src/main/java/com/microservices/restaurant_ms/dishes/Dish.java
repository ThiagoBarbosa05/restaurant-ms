package com.microservices.restaurant_ms.dishes;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.microservices.restaurant_ms.attachments.Attachment;
import com.microservices.restaurant_ms.categories.Category;
import com.microservices.restaurant_ms.ingredients.Ingredient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dishes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dish {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private int price;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToMany(mappedBy = "dish", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<Ingredient> ingredients;

  @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.DETACH, CascadeType.REFRESH}, orphanRemoval = true)
  @JoinColumn(name = "attachment_id")
  private Attachment attachment;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
