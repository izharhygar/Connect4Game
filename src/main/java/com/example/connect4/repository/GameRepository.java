package com.example.connect4.repository;

import java.util.concurrent.locks.Lock;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Component;

import com.example.connect4.dto.Game;
import com.example.connect4.exceptions.GameNotFoundException;

@Component
public class GameRepository {

	@Autowired
	private RedisTemplate<String, Game> template;

	private LockRegistry redisLockRegistry;

	public void save(Game game) {
		template.opsForHash().put(game.getId(), Game.OBJECT_KEY, game);
	}

	public Game getGame(String id) {
		Game game = (Game) template.opsForHash().get(id, Game.OBJECT_KEY);
		if (game == null) {
			throw new GameNotFoundException("Game with id " + id + " is not found");
		}
		return game;
	}

	public Game lockAndGet(String gameId) {
		Lock lock = redisLockRegistry.obtain(gameId);
		lock.lock();
		Game game = getGame(gameId);
		game.setLock(lock);
		return game;
	}

	public void unlock(Game game) {
		if (game != null && game.getLock() != null) {
			game.getLock().unlock();
		}
	}
	
	@PostConstruct
	public void initRegistry() {
		this.redisLockRegistry = new RedisLockRegistry(template.getConnectionFactory(), Game.OBJECT_KEY);
	}

	public void setRedisLockRegistry(LockRegistry redisLockRegistry) {
		this.redisLockRegistry = redisLockRegistry;
	}
}
