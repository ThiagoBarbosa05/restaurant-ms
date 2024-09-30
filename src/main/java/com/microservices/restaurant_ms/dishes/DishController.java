package com.microservices.restaurant_ms.dishes;

import com.microservices.restaurant_ms.attachments.Attachment;
import com.microservices.restaurant_ms.attachments.AttachmentService;
import com.microservices.restaurant_ms.categories.Category;
import com.microservices.restaurant_ms.categories.CategoryService;
import com.microservices.restaurant_ms.dishes.dto.CreateDishDTO;
import com.microservices.restaurant_ms.dishes.dto.UpdateDishDTO;
import com.microservices.restaurant_ms.global.dto.HttpPageableResponseDTO;
import com.microservices.restaurant_ms.global.dto.PageMetadataDTO;
import com.microservices.restaurant_ms.ingredients.IngredientService;
import com.microservices.restaurant_ms.ingredients.dto.CreateIngredientsDTO;

import com.microservices.restaurant_ms.ingredients.dto.UpdateIngredientsDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/dishes")
public class DishController {

  @Autowired
  private CategoryService categoryService;

  @Autowired
  private AttachmentService attachmentService;

  @Autowired
  private DishService dishService;

  @Autowired
  private IngredientService ingredientService;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Object> create(@Valid @RequestBody CreateDishDTO createDishDTO) {

    Category category = this.categoryService.findById(createDishDTO.category_id());
    Attachment attachment = this.attachmentService.findById(createDishDTO.attachment_id());
    Dish dish = this.dishService.create(createDishDTO, attachment, category);

    CreateIngredientsDTO createIngredientsDTO =  new CreateIngredientsDTO(createDishDTO.ingredients());
    this.ingredientService.create(createIngredientsDTO, dish);

    return new ResponseEntity<>(dish, new HttpHeaders(), HttpStatus.CREATED);

  }

  @GetMapping
  public ResponseEntity<Object> fetchAll(
    @RequestParam(defaultValue = "0") int number,
    @RequestParam(defaultValue = "3") int size,
    @RequestParam(required = false) String search
  ) {

    Page<Dish> dishes = this.dishService.fetchAll(search, number, size);

    List<Dish> dishesContent = dishes.getContent();

    PageMetadataDTO pageMetadataDTO = new PageMetadataDTO(
      dishes.getSize(),
      dishes.getNumber(),
      dishes.getTotalElements(),
      dishes.getTotalPages()
      );

    return new ResponseEntity<>(
      new HttpPageableResponseDTO(dishesContent, pageMetadataDTO),
      HttpStatus.OK
    );
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getById(
    @PathVariable String id
  ) {

    Dish dish = this.dishService.getById(UUID.fromString(id));

    return new ResponseEntity<>(dish, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Object> update(
    @Valid @RequestBody UpdateDishDTO updateDishDTO,
    @PathVariable String id
    ) {
      Category category = this.categoryService.findById(updateDishDTO.category_id());
      Attachment attachment = this.attachmentService.findById(updateDishDTO.attachment_id());
      Dish dishToUpdate = this.dishService.getById(UUID.fromString(id));

      UpdateIngredientsDTO updateIngredientsDTO = new UpdateIngredientsDTO(updateDishDTO.ingredients());

      this.ingredientService.updateIngredientsForDish(updateIngredientsDTO, dishToUpdate);

      Dish dish = this.dishService.update(
        updateDishDTO,
        dishToUpdate,
        attachment,
        category
      );

      return new ResponseEntity<>(dish, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Object> delete(
    @PathVariable String id
  ) {
    Dish dish = this.dishService.getById(UUID.fromString(id));
    this.dishService.delete(dish);

    return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
  }
}
