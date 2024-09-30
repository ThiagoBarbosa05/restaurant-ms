package com.microservices.restaurant_ms.exceptions;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.microservices.restaurant_ms.exceptions.dto.ResponseErrorMessageDTO;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

  private final MessageSource messageSource;

  public GlobalControllerExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<List<ResponseErrorMessageDTO>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
    List<ResponseErrorMessageDTO> responseErrorMessageDTOList = new ArrayList<>();

    for (FieldError error : exception.getBindingResult().getFieldErrors()) {
      String message = this.messageSource.getMessage(
        error,
        LocaleContextHolder.getLocale()
      );

      ResponseErrorMessageDTO responseErrorMessageDTO = new ResponseErrorMessageDTO(
        message,
        "Error",
        HttpStatus.BAD_REQUEST.value(),
        error.getField(),
        LocalDateTime.now()
      );

      responseErrorMessageDTOList.add(responseErrorMessageDTO);
    }

    return new ResponseEntity<>(responseErrorMessageDTOList, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {DishWithSameAttachmentException.class})
  public ResponseEntity<Object> handleDishWithSameAttachmentException(DishWithSameAttachmentException exception) {
    ResponseErrorMessageDTO responseErrorMessageDTO = new ResponseErrorMessageDTO(
      exception.getMessage(),
      "Error",
      HttpStatus.CONFLICT.value(),
      null,
      LocalDateTime.now()
    );

    return new ResponseEntity<>(responseErrorMessageDTO, new HttpHeaders(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(value = {ResourceNotFoundException.class})
  public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException exception) {
    ResponseErrorMessageDTO responseErrorMessageDTO = new ResponseErrorMessageDTO(
      exception.getMessage(),
      "Error",
      HttpStatus.NOT_FOUND.value(),
      null,
      LocalDateTime.now()
    );

    return new ResponseEntity<>(responseErrorMessageDTO, new HttpHeaders(), HttpStatus.NOT_FOUND);
  }

}
