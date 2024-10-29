package com.microservices.restaurant_ms.boot;

import com.microservices.restaurant_ms.attachments.Attachment;
import com.microservices.restaurant_ms.attachments.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
@Order(2)
public class UploadAttachments implements CommandLineRunner {

  private final S3Client client;

  public UploadAttachments(S3Client client) {
    this.client = client;
  }

  @Autowired
  private ResourceLoader resourceLoader;

  @Autowired
  private AttachmentRepository attachmentRepository;

  @Override
  public void run(String... args) throws Exception {

    var attachments = this.attachmentRepository.findAll();

    if(attachments.isEmpty()) {
      String[] imageNames = {
        "aperol.png",
        "cosmopolitan.jpg",
        "Macarons.svg",
        "Peachy-pastrie.svg",
        "prugna-pie.svg",
        "sallada-ravanello.svg",
        "Spaguetti-Gambe.svg",
        "suco-maracuja.svg",
        "Tedautunno.svg",
        "Torradas-de-Parma.svg",
      };

      for(String imageName: imageNames) {
        Resource resource = resourceLoader.getResource("classpath:data/images/" + imageName);

        if(resource.exists()) {
                    Path path = Path.of(resource.getURI());
                    byte[] fileData = Files.readAllBytes(path);

                    String mimeType = Files.probeContentType(path);


                    var key = UUID.randomUUID() + "-" + imageName;


                    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                                        .bucket("restaurant-microservice")
                                                                        .key(key)
                                                                        .contentType(mimeType != null ? mimeType : "application/octet-stream")
                                                                        .build();

                    client.putObject(putObjectRequest, RequestBody.fromBytes(fileData));
          Attachment attachment = Attachment.builder().key(key).name(imageName).build();
          this.attachmentRepository.save(attachment);
        }
      }
    }
  }
}
