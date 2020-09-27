package com.example.connect4.service;

import org.springframework.stereotype.Service;

import com.example.connect4.dto.Game;
import com.example.connect4.enums.DiskColor;
import com.example.connect4.exceptions.InvalidGamePlayException;

@Service
public class GameService {

	public DiskColor getLastPlayedDisc(Game game) {
		if (game.getLastPlayedX() == -1 && game.getLastPlayedY() == -1) {
			return game.getPlayer2().getDiskColor();
		}
		return game.getBoard()[game.getLastPlayedX()][game.getLastPlayedY()];
	}

	public boolean calculateWinner(Game game) {
		return validateInRow(game) || validateInColumn(game) || validateInForwardDiagonal(game) || validateInReverseDiagonal(game);
	}

	private boolean validateInRow(Game game) {
		int start = (game.getLastPlayedY() - 3) > 0 ? game.getLastPlayedY() - 3 : 0;
		int end = (game.getLastPlayedY() + 3) < 6 ? game.getLastPlayedY() + 3 : 6;
		int count = 0;
		for (int i = start; i <= end; i++) {
			if (getLastPlayedDisc(game).equals(game.getBoard()[game.getLastPlayedY()][i])) {
				count++;
				if (count == 4) {
					return true;
				}
			} else {
				count = 0;
			}
		}
		return false;
	}

	private boolean validateInColumn(Game game) {
		int start = (game.getLastPlayedX() - 3) > 0 ? game.getLastPlayedX() - 3 : 0;
		int end = (game.getLastPlayedX() + 3) < 6 ? game.getLastPlayedX() + 3 : 5;
		int count = 0;
		for (int i = start; i <= end; i++) {
			if (getLastPlayedDisc(game).equals(game.getBoard()[i][game.getLastPlayedX()])) {
				count++;
				if (count == 4) {
					return true;
				}
			} else {
				count = 0;
			}
		}
		return false;
	}

	private boolean validateInForwardDiagonal(Game game) {
		int startX = game.getLastPlayedX();
		int startY = game.getLastPlayedY();
		for (int i = 0; i < 3; i++) {
			if (startX == 0 || startY == 0) {
				break;
			}
			startX--;
			startY--;
		}

		int endX = game.getLastPlayedX();
		int endY = game.getLastPlayedY();
		for (int i = 0; i < 3; i++) {
			if (endX == 5 || endY == 6) {
				break;
			}
			endX++;
			endY++;
		}

		int count = 0;
		for (int i = startX, j = startY; i <= endX || j <= endY; i++, j++) {
			if (getLastPlayedDisc(game).equals(game.getBoard()[i][j])) {
				count++;
				if (count == 4) {
					return true;
				}
			} else {
				count = 0;
			}
		}
		return false;
	}

	private boolean validateInReverseDiagonal(Game game) {
		int startX = game.getLastPlayedX();
		int startY = game.getLastPlayedY();
		for (int i = 0; i < 3; i++) {
			if (startX == 0 || startY == 6) {
				break;
			}
			startX--;
			startY++;
		}

		int endX = game.getLastPlayedX();
		int endY = game.getLastPlayedY();
		for (int i = 0; i < 3; i++) {
			if (endX == 5 || endY == 0) {
				break;
			}
			endX++;
			endY--;
		}

		int count = 0;
		for (int i = startX, j = startY; i <= endX || j >= endY; i++, j--) {
			if (getLastPlayedDisc(game).equals(game.getBoard()[i][j])) {
				count++;
				if (count == 4) {
					return true;
				}
			} else {
				count = 0;
			}
		}
		return false;
	}

	public void putDisc(DiskColor DiskColor, int column, Game game) {
		if (game.getBoard()[0][column] != null) {
			throw new InvalidGamePlayException("No space for given column");
		}
		for (int i = 5; i >= 0; i--) {
			if (game.getBoard()[i][column] == null) {
				game.getBoard()[i][column] = DiskColor;
				game.setLastPlayedX(i);
				game.setLastPlayedY(column);
				break;
			}
		}
	}
	
	public void validateColor(String color) {
	    try {
	      DiskColor.valueOf(color);
	    } catch (Exception e) {
	      throw new IllegalArgumentException(
	          "valid values of color are " + DiskColor.RED + " and " + DiskColor.YELLOW);
	    }
	  }
}
