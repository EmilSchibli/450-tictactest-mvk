package ch.bbw.m450.tictactoe;

import ch.bbw.m450.tictactoe.TicTacToePlayer.Stone;

/**
 * Test-scoped helper for building tic-tac-toe boards from compact strings.
 * <p>
 * Board indices:
 * <pre>
 *  0 | 1 | 2
 * ---+---+---
 *  3 | 4 | 5
 * ---+---+---
 *  6 | 7 | 8
 * </pre>
 * Use {@code X} for cross, {@code O} for circle, and {@code .} for empty fields.
 */
class BoardTestHelper {

	static Stone[] toBoard(String layout) {
		if (layout.length() != TicTacToeMain.BOARD_SIZE) {
			throw new IllegalArgumentException("layout must have exactly 9 characters");
		}
		var board = new Stone[TicTacToeMain.BOARD_SIZE];
		for (var i = 0; i < layout.length(); i++) {
			board[i] = switch (layout.charAt(i)) {
				case 'X' -> Stone.CROSS;
				case 'O' -> Stone.CIRCLE;
				case '.' -> null;
				default -> throw new IllegalArgumentException("invalid character at index " + i + ": " + layout.charAt(i));
			};
		}
		return board;
	}
}
