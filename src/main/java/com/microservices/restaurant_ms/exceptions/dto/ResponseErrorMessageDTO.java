package com.microservices.restaurant_ms.exceptions.dto;

import java.time.LocalDateTime;

public record ResponseErrorMessageDTO(
  String message,
  String status,
  Integer statusCode,
  String field,
  LocalDateTime time
) {

}
