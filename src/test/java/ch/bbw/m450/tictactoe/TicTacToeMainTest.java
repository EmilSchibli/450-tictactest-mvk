package ch.bbw.m450.tictactoe;

import static ch.bbw.m450.tictactoe.BoardFixtures.MAIN_DIAGONAL_CROSS_WIN;
import static ch.bbw.m450.tictactoe.BoardFixtures.MIDDLE_COLUMN_CROSS_WIN;
import static ch.bbw.m450.tictactoe.BoardFixtures.NO_WINNER;
import static ch.bbw.m450.tictactoe.BoardFixtures.TOP_ROW_CIRCLE_WIN;
import static ch.bbw.m450.tictactoe.BoardFixtures.TOP_ROW_CROSS_WIN;
import static ch.bbw.m450.tictactoe.BoardTestHelper.isWin;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import ch.bbw.m450.tictactoe.TicTacToePlayer.Stone;

class TicTacToeMainTest implements WithAssertions {

	@Test
	void givenTopRowWin_whenIsWinCross_thenTrue() {
		assertThat(isWin(TOP_ROW_CROSS_WIN, Stone.CROSS)).isTrue();
	}

	@Test
	void givenMiddleColumnWin_whenIsWinCross_thenTrue() {
		assertThat(isWin(MIDDLE_COLUMN_CROSS_WIN, Stone.CROSS)).isTrue();
	}

	@Test
	void givenMainDiagonalWin_whenIsWinCross_thenTrue() {
		assertThat(isWin(MAIN_DIAGONAL_CROSS_WIN, Stone.CROSS)).isTrue();
	}

	@Test
	void givenNoWinner_whenIsWinBoth_thenFalse() {
		assertThat(isWin(NO_WINNER, Stone.CROSS)).isFalse();
		assertThat(isWin(NO_WINNER, Stone.CIRCLE)).isFalse();
	}

	@Test
	void givenCircleTopRow_whenIsWin_thenCrossFalseCircleTrue() {
		assertThat(isWin(TOP_ROW_CIRCLE_WIN, Stone.CROSS)).isFalse();
		assertThat(isWin(TOP_ROW_CIRCLE_WIN, Stone.CIRCLE)).isTrue();
	}
}
