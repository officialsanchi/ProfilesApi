package com.dinmaly.ProfilesApiApplication.config;

import java.security.SecureRandom;
import java.util.UUID;

public class UuidV7 {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate() {
        long timestamp = System.currentTimeMillis();

        long msb = (timestamp & 0xFFFFFFFFFFFFL) << 16;

        msb |= 0x7000L;

        msb |= (RANDOM.nextLong() & 0x0FFFL);

        long lsb = (RANDOM.nextLong() & 0x3FFFFFFFFFFFFFFFL) | 0x8000000000000000L;

        return new UUID(msb, lsb).toString();
    }
}
