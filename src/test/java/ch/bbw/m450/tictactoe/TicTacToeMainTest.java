package ch.bbw.m450.tictactoe;

import static ch.bbw.m450.tictactoe.BoardTestHelper.toBoard;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import ch.bbw.m450.tictactoe.TicTacToePlayer.Stone;

class TicTacToeMainTest implements WithAssertions {

	@Test
	void givenTopRowWin_whenIsWinCross_thenTrue() {
		var board = toBoard("XXX......");

		assertThat(TicTacToeMain.isWin(board, Stone.CROSS)).isTrue();
	}

	@Test
	void givenMiddleColumnWin_whenIsWinCross_thenTrue() {
		var board = toBoard(".X..X..X.");

		assertThat(TicTacToeMain.isWin(board, Stone.CROSS)).isTrue();
	}

	@Test
	void givenMainDiagonalWin_whenIsWinCross_thenTrue() {
		var board = toBoard("X...X...X");

		assertThat(TicTacToeMain.isWin(board, Stone.CROSS)).isTrue();
	}

	@Test
	void givenNoWinner_whenIsWinBoth_thenFalse() {
		var board = toBoard("XOX.OX...");

		assertThat(TicTacToeMain.isWin(board, Stone.CROSS)).isFalse();
		assertThat(TicTacToeMain.isWin(board, Stone.CIRCLE)).isFalse();
	}

	@Test
	void givenCircleTopRow_whenIsWin_thenCrossFalseCircleTrue() {
		var board = toBoard("OOO......");

		assertThat(TicTacToeMain.isWin(board, Stone.CROSS)).isFalse();
		assertThat(TicTacToeMain.isWin(board, Stone.CIRCLE)).isTrue();
	}
}
