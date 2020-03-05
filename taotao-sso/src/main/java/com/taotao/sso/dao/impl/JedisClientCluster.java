package com.taotao.sso.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.sso.dao.JedisClient;

import redis.clients.jedis.JedisCluster;


public class JedisClientCluster implements JedisClient{

	@Autowired
	private JedisCluster jedisCluster;
	@Override
	public String get(String key) {
		
		String string = jedisCluster.get(key);	
		return string;
	}

	@Override
	public String set(String key, String value) {
		
		String set = jedisCluster.set(key,value);
		return set;
	}

	@Override
	public long hset(String hkey, String key, String value) {
		
		Long hset = jedisCluster.hset(hkey, key, value);
		return hset;
	}

	@Override
	public String hget(String hkey, String key) {
		
		String hget = jedisCluster.hget(hkey, key);
		return hget;
	}

	@Override
	public long incr(String key) {
		
		Long incr = jedisCluster.incr(key);
		return incr;
	}

	@Override
	public long expire(String key, int second) {
		
		Long expire = jedisCluster.expire(key, second);
		return expire;
	}

	@Override
	public long ttl(String key) {
		
		Long ttl = jedisCluster.ttl(key);
		return ttl;
	}

	@Override
	public long deleteKey(String key) {
		Long del = jedisCluster.del(key);
		return del;
	}

	@Override
	public long delHashKey(String hkey, String key) {
		
		Long hdel = jedisCluster.hdel(hkey, key);
		return hdel;
	}

}
