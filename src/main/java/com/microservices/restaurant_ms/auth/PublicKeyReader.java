package com.microservices.restaurant_ms.auth;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class PublicKeyReader {
  public static PublicKey readPublicKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

    ClassPathResource resource = new ClassPathResource(filename);
    byte[] keyBytes = Files.readAllBytes(Paths.get(resource.getURI()));
    String publicKeyContent = new String(keyBytes)
      .replace("-----BEGIN PUBLIC KEY-----", "")
      .replace("-----END PUBLIC KEY-----", "")
      .replaceAll("\\s+", "");

    byte[] decodedKey = Base64.getDecoder().decode(publicKeyContent);

    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);

    KeyFactory keyFactory = KeyFactory.getInstance("RSA");


    return keyFactory.generatePublic(keySpec);

  }
}
