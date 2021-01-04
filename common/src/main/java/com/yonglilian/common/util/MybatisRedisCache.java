package com.yonglilian.common.util;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.*;
import java.util.concurrent.Callable;

public class MybatisRedisCache implements Cache {
	private RedisTemplate<Object, Object> template;
	private String name = "";

	public RedisTemplate<Object, Object> getTemplate() {
		return template;
	}

	public void setTemplate(RedisTemplate<Object, Object> template) {
		this.template = template;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Object getNativeCache() {
		return this.template;
	}

	@Override
	public ValueWrapper get(Object key) {
		Object object = null;
		object = template.opsForValue().get(SerializeUtil.serialize(key));
		if (object == null) {
			return null;
		}
		object = toObject((byte[]) object);
		return (object != null ? new SimpleValueWrapper(object) : null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Callable<T> type) {
		Object object = null;
		object = template.opsForValue().get(SerializeUtil.serialize(key));
		if (object == null) {
			return null;
		} else {
			try {
				T t_object = (T) object;
				return t_object;
			} catch (Exception e) {
				return null;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Class<T> type) {
		Object object = null;
		object = template.opsForValue().get(SerializeUtil.serialize(key));
		if (object == null) {
			return null;
		} else {
			try {
				T t_object = (T) object;
				return t_object;
			} catch (Exception e) {
				return null;
			}
		}
	}

	@Override
	public void put(Object key, Object value) {
//		final long liveTime = 86400;
		template.opsForValue().set(SerializeUtil.serialize(key), toByteArray(value));
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		Object existingValue = template.opsForValue().get(SerializeUtil.serialize(key));
		if (existingValue == null) {
			template.opsForValue().setIfAbsent(SerializeUtil.serialize(key), toByteArray(value));
			return null;
		} else {
			return new SimpleValueWrapper(existingValue);
		}
	}

	@Override
	public void evict(Object key) {
		template.delete(SerializeUtil.serialize(key));
	}

	@Override
	public void clear() {
		template.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return "ok";
			}
		});
	}

	/**
	 * 描述 : <byte[]转Object>. <br>
	 * <p>
	 * <使用方法说明>
	 * </p>
	 * @param bytes
	 * @return
	 */
	private Object toObject(byte[] bytes) {
		Object obj = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			obj = ois.readObject();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return obj;
	}

	/**
	 * 描述 : <Object转byte[]>. <br>
	 * <p>
	 * <使用方法说明>
	 * </p>
	 * @param obj
	 * @return
	 */
	private byte[] toByteArray(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bytes;
	}
}

/**
 * 序列化工具类
 */
class SerializeUtil {
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static Object unserialize(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bais != null) {
				try {
					bais.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}