//package zr.zrpower.config;
//
//import java.lang.reflect.Method;
//
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.interceptor.KeyGenerator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
///**
// * Redis缓存服务配置
// * @author lwk
// *
// */
//@Configuration
//@EnableCaching
//public class RedisConfig extends CachingConfigurerSupport {
//	/**
//	 * 生成key的策略
//	 * @return
//	 */
//	@Bean
//	public KeyGenerator keyGenerator() {
//		return new KeyGenerator() {
//			@Override
//			public Object generate(Object target, Method method, Object... params) {
//				StringBuilder builder = new StringBuilder();
//				builder.append(target.getClass().getName());
//				builder.append(method.getName());
//				for (Object obj : params) {
//					builder.append(obj.toString());
//				}
//				return builder.toString();
//			}
//		};
//	}
//
//	/**
//	 * 管理缓存
//	 */
//	@SuppressWarnings("rawtypes")
//	@Bean
//	public CacheManager cacheManager(RedisTemplate redisTemplate) {
//		RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
//		return rcm;
//	}
//
//	/**
//	 * RedisTemplate配置
//	 */
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@Bean
//	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
//		StringRedisTemplate redisTemplate = new StringRedisTemplate(factory);
//		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//		jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//		redisTemplate.afterPropertiesSet();
//		return redisTemplate;
//	}
//}