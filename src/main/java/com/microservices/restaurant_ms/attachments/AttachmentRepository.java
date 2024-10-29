package com.microservices.restaurant_ms.attachments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
  Optional<Attachment> findByNameContainingIgnoreCase(String name);
}
