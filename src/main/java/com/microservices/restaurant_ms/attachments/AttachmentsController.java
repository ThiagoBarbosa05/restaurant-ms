package com.microservices.restaurant_ms.attachments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
public class AttachmentsController {

  @Autowired
  private AttachmentService attachmentService;

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/upload")
  public ResponseEntity<Object> uploadImage(@RequestParam("file") MultipartFile file) {
    try {
      UUID attachmentId = attachmentService.upload(file);
      return ResponseEntity.ok(attachmentId);
    } catch (IOException e) {
      return ResponseEntity.status(500).body("Erro");
    }
  }

}
