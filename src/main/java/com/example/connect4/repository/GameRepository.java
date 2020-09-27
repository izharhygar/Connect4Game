package com.example.connect4.repository;

import com.example.connect4.dto.Game;

public interface GameRepository {

	void save(Game game);

	Game getGame(String id);

	Game lockAndGet(String gameId);

	void unlock(Game game);

}
