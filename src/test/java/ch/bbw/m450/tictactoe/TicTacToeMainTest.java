package ch.bbw.m450.tictactoe;

import static ch.bbw.m450.tictactoe.BoardFixtures.DIAG_X;
import static ch.bbw.m450.tictactoe.BoardFixtures.MID_COL_X;
import static ch.bbw.m450.tictactoe.BoardFixtures.NO_WIN;
import static ch.bbw.m450.tictactoe.BoardFixtures.TOP_ROW_O;
import static ch.bbw.m450.tictactoe.BoardFixtures.TOP_ROW_X;
import static ch.bbw.m450.tictactoe.BoardTestHelper.isWin;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import ch.bbw.m450.tictactoe.TicTacToePlayer.Stone;

class TicTacToeMainTest implements WithAssertions {

	static Stream<Arguments> boards() {
		return Stream.of(
				arguments(TOP_ROW_X, Stone.CROSS, true),
				arguments(MID_COL_X, Stone.CROSS, true),
				arguments(DIAG_X, Stone.CROSS, true),
				arguments(TOP_ROW_O, Stone.CIRCLE, true),
				arguments(NO_WIN, Stone.CROSS, false),
				arguments(NO_WIN, Stone.CIRCLE, false),
				arguments(TOP_ROW_O, Stone.CROSS, false));
	}

	@ParameterizedTest
	@MethodSource("boards")
	void isWin(String layout, Stone color, boolean expected) {
		assertThat(isWin(layout, color)).isEqualTo(expected);
	}
}
