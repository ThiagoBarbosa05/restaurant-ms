package com.microservices.restaurant_ms.global.dto;

public record HttpPageableResponseDTO(
  Object content,
  PageMetadataDTO metadata
) {
}
