package org.dziem.clothesarserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest(properties = {
        "spring.liquibase.enabled=false",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.data.redis.host=localhost",
        "spring.data.redis.port=6379"
})
@MockBean(StringRedisTemplate.class)
class ClothesARServerTests {

    @Test
    void contextLoads() {
    }

}
