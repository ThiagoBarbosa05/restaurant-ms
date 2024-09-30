package com.microservices.restaurant_ms.exceptions;

public class DishWithSameAttachmentException extends RuntimeException{
  public DishWithSameAttachmentException(String message) {
    super(message);
  }
}
