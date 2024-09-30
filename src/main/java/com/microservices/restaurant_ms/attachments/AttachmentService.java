package com.microservices.restaurant_ms.attachments;

import com.microservices.restaurant_ms.exceptions.ResourceNotFoundException;
import com.microservices.restaurant_ms.storage.services.R2StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttachmentService {

  @Autowired
  private R2StorageService r2StorageService;

  @Autowired
  private AttachmentRepository attachmentRepository;

  public UUID upload(MultipartFile file) throws IOException {
    String key = UUID.randomUUID() + "-" + file.getOriginalFilename();

    r2StorageService.upload(file, key);

    Attachment attachment = Attachment.builder()
                                      .name(file.getOriginalFilename())
                                      .key(key)
                                      .build();

    Attachment attachmentSaved = attachmentRepository.save(attachment);
    return attachmentSaved.getId();
  }

  public Attachment findById(UUID id) {
    Optional<Attachment> attachment = this.attachmentRepository.findById(id);

    if(attachment.isEmpty()) {
      throw new ResourceNotFoundException("Imagem não encontrada");
    }

    return attachment.get();
  }

  public void deleteAfterDishUpdated(Attachment attachment, UUID dishAttachmentId) {
    if(attachment.getId() != dishAttachmentId)
      this.attachmentRepository.deleteById(attachment.getId());
      this.r2StorageService.delete(attachment);
  }
}
