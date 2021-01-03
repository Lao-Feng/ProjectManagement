package com.yonglilian.service;
//package com.zrpower.zrweb.service;
//
//import java.io.Serializable;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.ListOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.SetOperations;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.data.redis.core.ZSetOperations;
//import org.springframework.stereotype.Service;
//
//import com.zrpower.zrweb.common.util.LoggerUtil;
//
///**
// * Redis缓存服务类
// * @author lwk
// *
// */
//@Service
//public class RedisService {
//	@SuppressWarnings("rawtypes")
//	@Autowired
//	private RedisTemplate redisTemplate;
//
//	/**
//	 * 写入缓存<key, value>
//	 * @param key
//	 * @param value
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public boolean set(final String key, Object value) {
//		boolean result = false;
//		try {
//			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
//			operations.set(key, value);
//			result = true;
//		} catch (Exception e) {
//			LoggerUtil.LOGGER.error("RedisService.set(key, value) Exception:\n", e);
//		}
//		return result;
//	}
//
//	/**
//	 * 写入缓存设置时效时间<key, value, expireTime>
//	 * @param key
//	 * @param value
//	 * @param expireTime
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public boolean set(final String key, Object value, Long expireTime) {
//		boolean result = false;
//		try {
//			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
//			operations.set(key, value);
//			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
//			result = true;
//		} catch (Exception e) {
//			LoggerUtil.LOGGER.error("RedisService.set(key, value, expireTime) Exception:\n", e);
//		}
//		return result;
//	}
//
//	/**
//	 * 根据keys批量删除对应的value
//	 * @param keys
//	 */
//	public void remove(final String... keys) {
//		for (String key : keys) {
//			remove(key);
//		}
//	}
//
//	/**
//	 * 根据pattern模式的keys批量删除value
//	 * @param pattern
//	 */
//	@SuppressWarnings("unchecked")
//	public void removePattern(final String pattern) {
//		Set<Serializable> keys = redisTemplate.keys(pattern);
//		if (keys.size() > 0) {
//			redisTemplate.delete(keys);
//		}
//	}
//
//	/**
//	 * 根据key删除对应的value
//	 * @param key
//	 */
//	@SuppressWarnings("unchecked")
//	public void remove(final String key) {
//		if (exists(key)) {
//			redisTemplate.delete(key);
//		}
//	}
//
//	/**
//	 * 判断缓存中是否有key对应的value
//	 * @param key
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public boolean exists(final String key) {
//		return redisTemplate.hasKey(key);
//	}
//
//	/**
//	 * 根据key读取缓存value
//	 * @param key
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public Object get(final String key) {
//		Object result = null;
//		ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
//		result = operations.get(key);
//		return result;
//	}
//
//	/**
//	 * 哈希添加value
//	 * @param key
//	 * @param hashKey
//	 * @param value
//	 */
//	@SuppressWarnings("unchecked")
//	public void hmSet(String key, Object hashKey, Object value) {
//		HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
//		hash.put(key, hashKey, value);
//	}
//
//	/**
//	 * 哈希获取value
//	 * @param key
//	 * @param hashKey
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public Object hmGet(String key, Object hashKey) {
//		HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
//		return hash.get(key, hashKey);
//	}
//
//	/**
//	 * 列表添加<key, value>
//	 * @param key
//	 * @param value
//	 */
//	@SuppressWarnings("unchecked")
//	public void lPush(String key, Object value) {
//		ListOperations<String, Object> list = redisTemplate.opsForList();
//		list.rightPush(key, value);
//	}
//
//	/**
//	 * 列表获取
//	 * @param key
//	 * @param arg1
//	 * @param arg2
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public List<Object> lRange(String key, long arg1, long arg2) {
//		ListOperations<String, Object> list = redisTemplate.opsForList();
//		return list.range(key, arg1, arg2);
//	}
//
//	/**
//	 * 集合添加
//	 * @param key
//	 * @param value
//	 */
//	@SuppressWarnings("unchecked")
//	public void add(String key, Object value) {
//		SetOperations<String, Object> set = redisTemplate.opsForSet();
//		set.add(key, value);
//	}
//
//	/**
//	 * 集合获取
//	 * @param key
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public Set<Object> setMembers(String key) {
//		SetOperations<String, Object> set = redisTemplate.opsForSet();
//		return set.members(key);
//	}
//
//	/**
//	 * 有序集合添加
//	 * @param key
//	 * @param value
//	 * @param scoure
//	 */
//	@SuppressWarnings("unchecked")
//	public void zAdd(String key, Object value, double scoure) {
//		ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
//		zset.add(key, value, scoure);
//	}
//
//	/**
//	 * 有序集合获取
//	 * @param key
//	 * @param score1
//	 * @param score2
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public Set<Object> rangeByScore(String key, double score1, double score2) {
//		ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
//		return zset.rangeByScore(key, score1, score2);
//	}
//}
