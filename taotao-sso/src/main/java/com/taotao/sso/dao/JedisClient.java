package com.taotao.sso.dao;

public interface JedisClient {

	public String get(String key);
	public String set(String key,String value);
	public long hset(String hkey,String key,String value);
	public String hget(String hkey,String key);
	public long incr(String key);
	public long expire(String key,int second);
	public long ttl(String key);
	public long deleteKey(String key);
	public long delHashKey(String hkey,String key);
}
