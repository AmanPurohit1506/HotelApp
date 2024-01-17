package com.example.hotelBooking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.example.hotelBooking.entity.HotelBookingRequest;

/**
 * Configuration class for Redis-related beans and cache management.
 */
@Configuration
public class RedisConfig {

	@Autowired
	private CacheManager cacheManager;

	/**
	 * Configures a ReactiveRedisOperations bean with custom serialization for
	 * HotelBookingRequest.
	 *
	 * @param factory The ReactiveRedisConnectionFactory to be used.
	 * @return ReactiveRedisOperations bean for HotelBookingRequest.
	 */
	@Bean
	public ReactiveRedisOperations<String, HotelBookingRequest> reactiveRedisOperations(
			ReactiveRedisConnectionFactory factory) {
		Jackson2JsonRedisSerializer<HotelBookingRequest> serializer = new Jackson2JsonRedisSerializer<>(
				HotelBookingRequest.class);
		RedisSerializationContext<String, HotelBookingRequest> context = RedisSerializationContext
				.<String, HotelBookingRequest>newSerializationContext(new StringRedisSerializer()).value(serializer)
				.build();
		return new ReactiveRedisTemplate<>(factory, context);
	}

	/**
	 * Clears the "hotelBookings" cache at the startup of the application.
	 *
	 * @return Null, as it does not return any specific value.
	 */
	@Bean
	public Object clearAllCachesAtStartup() {
		Cache cache = cacheManager.getCache("hotelBookings");
		if (cache != null) {
			cache.clear();
		}
		return null;
	}
}
