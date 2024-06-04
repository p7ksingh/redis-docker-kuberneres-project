package com.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import redis.clients.jedis.Jedis;

@RestController
public class RedisDataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(RedisDataInitializer.class);
    private final Jedis jedis;
    private boolean flag = false; 

    public RedisDataInitializer(Jedis jedis) {
        this.jedis = jedis;
    //    initializeRedisData();
    }
    
    @GetMapping("/start")
	public String startMessage() {
		flag = true; // Set flag to true to start message production
		initializeRedisData();
		return "Message production started.";
	}

	@GetMapping("/stop")
	public String stopMessage() {
		flag = false; // Set flag to false to stop message production
		initializeRedisData();
		return "Message production stopped.";
	}

    private void initializeRedisData() {
        try {
            // Generate random key and value
            while (flag) {
                String key = "key_" + Math.random();
                String value = "value_" + Math.random();

                // Save to Redis
                jedis.set(key, value);

                // Retrieve from Redis to verify
                String retrievedValue = jedis.get(key);
                logger.info("Retrieved from Redis - Key: " + key + ", Value: " + retrievedValue);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Error in initializing Redis data: " + e.getMessage());
        }
    }
}
