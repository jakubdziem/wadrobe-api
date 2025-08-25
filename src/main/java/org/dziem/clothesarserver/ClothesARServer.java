package org.dziem.clothesarserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ClothesARServer {

    public static void main(String[] args) {
        SpringApplication.run(ClothesARServer.class, args);
    }

}
