package com.example.connect4.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.connect4.dto.Game;
import com.example.connect4.dto.Player;
import com.example.connect4.enums.DiskColor;
import com.example.connect4.enums.GameStatus;
import com.example.connect4.exceptions.GameNotFoundException;
import com.example.connect4.exceptions.InvalidGamePlayException;
import com.example.connect4.exceptions.InvalidGameStatusException;
import com.example.connect4.exceptions.NoPlayerSpaceException;
import com.example.connect4.repository.GameRepository;

@Component
public class Connect4Service {

	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private GameService gameService;

	public Game createNewGame(String userId, String color) {
		Player player = new Player();
		player.setUserId(userId);
		player.setDiskColor(DiskColor.valueOf(color));
		Game game = new Game();
		game.setId(UUID.randomUUID().toString());
		game.setPlayer1(player);
		gameRepository.save(game);
		return game;
	}

	public Game getGame(String gameId) {
		Game game = gameRepository.getGame(gameId);
		return game;
	}

	public Game joinGame(String gameId, String userId) {
		Game game = null;
		try {
			// lock for race condition for same game
			game = gameRepository.lockAndGet(gameId);
			if (game.getPlayer2() != null) {
				throw new NoPlayerSpaceException("Game does not have room for new player");
			}
			if (game.getPlayer1().getUserId().equals(userId)) {
				throw new NoPlayerSpaceException("Player already exists for the game");
			}
			Player player = new Player();
			player.setUserId(userId);
			DiskColor disColorForPlayer2 = game.getPlayer1().equals(DiskColor.RED) ? DiskColor.YELLOW : DiskColor.RED;
			player.setDiskColor(disColorForPlayer2);
			game.setPlayer2(player);
			game.setStatus(GameStatus.STARTED);
			gameRepository.save(game);
		} finally {
			gameRepository.unlock(game);
		}
		return game;
	}

	public Game play(String gameId, String userId, int column) {
		Game game = null;
		try {
			// lock for race condition for same game
			game = gameRepository.lockAndGet(gameId);
			if (GameStatus.CREATED.equals(game.getStatus())) {
				throw new InvalidGameStatusException("Game does not have enough players");
			}
			if (!(game.getPlayer1().getUserId().equals(userId) || game.getPlayer2().getUserId().equals(userId))) {
				throw new GameNotFoundException("No game found for given userId");
			}
			if (GameStatus.COMPLETED.equals(game.getStatus())) {
				throw new InvalidGameStatusException("Game is already completed");
			}
			Player player = game.getPlayer1().getUserId().equals(userId) ? game.getPlayer1() : game.getPlayer2();
			if (player.getDiskColor().equals(gameService.getLastPlayedDisc(game))) {
				throw new InvalidGamePlayException("Game should be played by other player");
			}
			gameService.putDisc(player.getDiskColor(), column, game);
			if (gameService.calculateWinner(game)) {
				game.setStatus(GameStatus.COMPLETED);
			}
			gameRepository.save(game);
		} finally {
			gameRepository.unlock(game);
		}
		return game;
	}

}
