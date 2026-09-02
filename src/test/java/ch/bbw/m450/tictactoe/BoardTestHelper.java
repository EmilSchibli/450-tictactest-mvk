package ch.bbw.m450.tictactoe;

import ch.bbw.m450.tictactoe.TicTacToePlayer.Stone;

class BoardTestHelper {

	static Stone[] toBoard(String layout) {
		if (layout.length() != TicTacToeMain.BOARD_SIZE) {
			throw new IllegalArgumentException("layout must have 9 chars");
		}
		var board = new Stone[TicTacToeMain.BOARD_SIZE];
		for (var i = 0; i < layout.length(); i++) {
			board[i] = switch (layout.charAt(i)) {
				case 'X' -> Stone.CROSS;
				case 'O' -> Stone.CIRCLE;
				case '.' -> null;
				default -> throw new IllegalArgumentException("bad char: " + layout.charAt(i));
			};
		}
		return board;
	}

	static boolean isWin(String layout, Stone color) {
		return TicTacToeMain.isWin(toBoard(layout), color);
	}
}
