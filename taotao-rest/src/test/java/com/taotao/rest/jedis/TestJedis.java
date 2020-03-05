package com.taotao.rest.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class TestJedis {

	@Test
	public void testJedisSingle() {
		Jedis jedis = new Jedis("192.168.44.129", 6379);
		jedis.set("key1","jedis Test");
		String string = jedis.get("key1");
		System.out.println(string);
		jedis.close();
	}
	
	@Test
	public void testJedisPool() {
		JedisPool jedisPool = new JedisPool("192.168.44.129",6379);
		Jedis resource = jedisPool.getResource();
		resource.set("key2","jedisPool Test");
		String string = resource.get("key2");
		System.out.println(string);
		resource.close();
		jedisPool.close();
	}
	
	@Test
	public void testJedisCluster() {
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("192.168.44.129", 7001));
		nodes.add(new HostAndPort("192.168.44.129", 7002));
		nodes.add(new HostAndPort("192.168.44.129", 7003));
		nodes.add(new HostAndPort("192.168.44.129", 7004));
		nodes.add(new HostAndPort("192.168.44.129", 7005));
		nodes.add(new HostAndPort("192.168.44.129", 7006));
		JedisCluster jedisCluster = new JedisCluster(nodes );
		jedisCluster.set("key1","100");
		String string = jedisCluster.get("key1");
		System.out.println(string);
		jedisCluster.close();
	}
	@Test
	public void testSpringJedisSingle() {
		String xmlPath = "classpath:spring/applicationContext-jedis.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);
		JedisPool jedisPool = (JedisPool) applicationContext.getBean("redisClient");
		Jedis jedis = jedisPool.getResource();
		jedis.set("key3","hello");
		String string = jedis.get("key3");
		System.out.println(string);
		jedis.close();
		jedisPool.close();
	}
	
}
