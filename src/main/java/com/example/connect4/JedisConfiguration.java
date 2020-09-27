package com.example.connect4;

import java.io.IOException;

import org.msgpack.MessagePack;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.example.connect4.dto.Game;
import com.example.connect4.exceptions.GameException;

@Configuration
public class JedisConfiguration {
	
	@SuppressWarnings("deprecation")
	@Bean
	  public JedisConnectionFactory connectionFactory() {
	    JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
	    jedisConnectionFactory.setHostName("redis-18657.c44.us-east-1-2.ec2.cloud.redislabs.com");
	    jedisConnectionFactory.setPort(18657);
	    jedisConnectionFactory.setPassword("5jZ5Xt5w1ELg32acYbqGvFVdZ3DXIHJZ");
	    return jedisConnectionFactory;
	  }
	
	@Bean
	  public RedisTemplate<String, Game> redisTemplate() {
	    RedisTemplate<String, Game> template = new RedisTemplate<String, Game>();
	    template.setConnectionFactory(connectionFactory());
	    template.setKeySerializer(new StringRedisSerializer());
	    
	    //http://msgpack.org/ this serialize to minimum size data.
	    template.setValueSerializer(new RedisSerializer<Game>() {
	      @Override
	      public byte[] serialize(Game game) throws SerializationException {
	        MessagePack msgpack = new MessagePack();
	        try {
	          return msgpack.write(game);
	        } catch (IOException e) {
	          throw new GameException(e);
	        }
	      }

	      @Override
	      public Game deserialize(byte[] bytes) throws SerializationException {
	        MessagePack msgpack = new MessagePack();
	        try {
	          return msgpack.read(bytes, Game.class);
	        } catch (IOException e) {
	          throw new GameException(e);
	        }
	      }
	    });
	    return template;
	  }
	}

