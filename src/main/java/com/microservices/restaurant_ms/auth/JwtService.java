package com.microservices.restaurant_ms.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

@Service
public class JwtService {

  public DecodedJWT validate(String token) {
    try {
      PublicKey publicKey = PublicKeyReader.readPublicKey("public.key");

      Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey);

      return JWT.require(algorithm).build().verify(token);

    } catch (JWTVerificationException e) {
      e.printStackTrace();
      return null;
    } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new RuntimeException(e);
    }
  }
}
