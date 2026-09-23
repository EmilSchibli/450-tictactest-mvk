package ch.bbw.m450.tictactoe;

import static ch.bbw.m450.tictactoe.BoardFixtures.EMPTY;
import static ch.bbw.m450.tictactoe.BoardFixtures.FULL_DRAW;
import static ch.bbw.m450.tictactoe.BoardTestHelper.toBoard;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ch.bbw.m450.tictactoe.TicTacToePlayer.Stone;
import ch.bbw.m450.tictactoe.players.GreedyPlayer;

class GreedyPlayerTest implements WithAssertions {

	@ParameterizedTest
	@CsvSource({ EMPTY + ", 0", "XO......., 2", "XOXOXOXO., 8", ".OXOXOXOX, 0" })
	void givenBoard_whenPlay_thenFirstFreeField(String layout, int expected) {
		assertThat(new GreedyPlayer().play(toBoard(layout), Stone.CROSS)).isEqualTo(expected);
	}

	@Test
	void givenFullBoard_whenPlay_thenThrows() {
		assertThatIllegalStateException().isThrownBy(() -> new GreedyPlayer().play(toBoard(FULL_DRAW), Stone.CROSS));
	}
}
