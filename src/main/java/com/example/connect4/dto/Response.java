package com.example.connect4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
	private Game game;

	private String message;
	
	public Response(Game game) {
	    this.game=game;
	  }
	
	

}
