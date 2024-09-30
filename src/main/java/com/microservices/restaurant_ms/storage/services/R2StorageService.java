package com.microservices.restaurant_ms.storage.services;

import com.microservices.restaurant_ms.attachments.Attachment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class R2StorageService {

  private final S3Client client;

  public R2StorageService(S3Client client) {
    this.client = client;
  }

  @Async
  public void upload(MultipartFile file, String key) throws IOException {

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
      .bucket("restaurant-microservice")
      .key(key)
      .contentType(file.getContentType())
      .build();

      client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
  }

  @Async
  public void delete(Attachment attachment) {

    DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                                                                 .bucket("restaurant-microservice")
                                                                 .key(attachment.getKey())
                                                                 .build();

    client.deleteObject(deleteObjectRequest);
  }

}
