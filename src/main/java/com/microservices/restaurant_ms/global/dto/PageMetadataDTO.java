package com.microservices.restaurant_ms.global.dto;

public record PageMetadataDTO(
  int size,
  int page,
  long total,
  int totalPages
) {

}
