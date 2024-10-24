package com.loser.backend.club.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;


@Slf4j
public class GateWaySignUtil {

    final static public String ACCESS_KEY = "trading-gateway-test-spec";
    final static public String SECRET_KEY = "Cc1zlTe1fDKiZQpNyJF536JvlPJswS";



    public static String sing(HttpMethod method, String uri) {
        String message = method.name() + uri + ACCESS_KEY + Instant.now().toEpochMilli();
        try {
            Mac hasher = Mac.getInstance("HmacSHA256");
            hasher.init(new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256"));
            byte[] hash = hasher.doFinal(message.getBytes());
            // to lowercase hexits
            DatatypeConverter.printHexBinary(hash);
            // to base64
            return DatatypeConverter.printBase64Binary(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
           log.error("Generate header signing error, {}", e.getMessage(), e);
            throw new RuntimeException("Generate header signing error");
        }
    }

}
