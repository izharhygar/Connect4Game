package com.example.connect4.controller;

import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.connect4.dto.Game;
import com.example.connect4.dto.Response;
import com.example.connect4.enums.GameStatus;
import com.example.connect4.service.Connect4Service;
import com.example.connect4.service.GameService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/connect4")
public class Connect4Controller {

	@Autowired
	private Connect4Service service;

	@Autowired
	private GameService gameService;

	@Operation(summary= "start a new game by entering your preffered userId and your color as Request parameters")
	@GetMapping("/newgame")
	public Response startNewGame(@RequestParam @NotNull String userId, @RequestParam @NotNull String color) {
		gameService.validateColor(color);
		Game game = service.createNewGame(userId, color);
		Response gameResponse = new Response(game);
		gameResponse.setMessage("Game is Created successfully");
		return gameResponse;
	}

	@Operation(summary = "get a preexisting game by entering the GameId as path variable")
	@GetMapping("/getgame/{gameId}")
	public Response getGame(@PathVariable @NotNull String gameId) {
		Game game = service.getGame(gameId);
		Response gameResponse = new Response(game);
		gameResponse.setMessage("Game is Retrieved");
		return gameResponse;
	}

	@Operation(summary = "join the game by second player by entering preexisting gameId and preffered userId for player2")
	@RequestMapping(path = "/joingame/{gameId}/{userId}", method = RequestMethod.PUT)
	public Response joinGame(@PathVariable @NotNull String gameId, @PathVariable @NotNull String userId) {
		Game game = service.joinGame(gameId, userId);
		Response gameResponse = new Response(game);
		gameResponse.setMessage(
				userId + " joined the game " + gameId + " with disc color " + game.getPlayer2().getDiskColor());
		return gameResponse;
	}

	@Operation(summary = "make a move by entering preexisting gameId, your UserId and number of column of your choice between 0 and 6")
	@RequestMapping(path = "/playgame/{gameId}/{userId}/{column}", method = RequestMethod.PUT)
	public Response playGame(@PathVariable @NotNull String gameId, @PathVariable @NotNull String userId,
			@PathVariable int column) {
		if (column < 0 || column > 6) {
			throw new IllegalArgumentException("Column value should be between 0 and 6");
		}
		Game game = service.play(gameId, userId, column);
		Response gameResponse = new Response(game);
		gameResponse.setMessage(userId + " put disc on column " + column + " for the game " + gameId);
		if (GameStatus.COMPLETED.equals(game.getStatus())) {
			gameResponse.setMessage(gameResponse.getMessage() + " User " + userId + " won the game");
		}
		return gameResponse;
	}

}
