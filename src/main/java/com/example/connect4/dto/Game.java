package com.example.connect4.dto;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;

import com.example.connect4.enums.DiskColor;
import com.example.connect4.enums.GameStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Game implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4725174442460963069L;

	public static final String OBJECT_KEY = "GAME";

	private String id;

	private Player player1;

	private Player player2;

	private DiskColor[][] board = new DiskColor[6][7];

	@JsonIgnore
	private int lastPlayedX = -1;

	@JsonIgnore
	private int lastPlayedY = -1;

	private GameStatus status = GameStatus.CREATED;

	@JsonIgnore
	private transient Lock lock;

}
