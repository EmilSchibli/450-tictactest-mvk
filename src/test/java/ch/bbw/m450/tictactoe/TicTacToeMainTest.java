package ch.bbw.m450.tictactoe;

import static ch.bbw.m450.tictactoe.BoardTestHelper.toBoard;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import ch.bbw.m450.tictactoe.TicTacToePlayer.Stone;

class TicTacToeMainTest implements WithAssertions {

	@Test
	void isWin_detectsWinningTopRow() {
		var board = toBoard("XXX......");

		assertThat(TicTacToeMain.isWin(board, Stone.CROSS)).isTrue();
	}

	@Test
	void isWin_detectsWinningMiddleColumn() {
		var board = toBoard(".X..X..X.");

		assertThat(TicTacToeMain.isWin(board, Stone.CROSS)).isTrue();
	}

	@Test
	void isWin_detectsWinningMainDiagonal() {
		var board = toBoard("X...X...X");

		assertThat(TicTacToeMain.isWin(board, Stone.CROSS)).isTrue();
	}

	@Test
	void isWin_returnsFalseWhenNoThreeInALine() {
		var board = toBoard("XOX.OX...");

		assertThat(TicTacToeMain.isWin(board, Stone.CROSS)).isFalse();
		assertThat(TicTacToeMain.isWin(board, Stone.CIRCLE)).isFalse();
	}

	@Test
	void isWin_returnsFalseForWrongColorDespiteWinningLine() {
		var board = toBoard("OOO......");

		assertThat(TicTacToeMain.isWin(board, Stone.CROSS)).isFalse();
		assertThat(TicTacToeMain.isWin(board, Stone.CIRCLE)).isTrue();
	}
}
