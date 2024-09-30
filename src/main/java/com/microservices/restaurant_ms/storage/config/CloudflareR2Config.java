package com.microservices.restaurant_ms.storage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class CloudflareR2Config {

  @Value("${aws.access.key.id}")
  private String accessKeyId;

  @Value("${aws.secret.access.key}")
  private String secretAccessKey;

  @Value("${cloudflare.r2.endpoint}")
  private String cloudflareR2Endpoint;

  @Bean
  public S3Client client() {

    AwsBasicCredentials credentials = AwsBasicCredentials.create(
      accessKeyId,
      secretAccessKey
    );

    return S3Client.builder()
      .endpointOverride(URI.create(cloudflareR2Endpoint))
      .credentialsProvider(StaticCredentialsProvider.create(credentials))
      .region(Region.of("auto"))
      .build();
  }
}
