package ch.bbw.m450.tictactoe;

import static ch.bbw.m450.tictactoe.BoardFixtures.EMPTY;
import static ch.bbw.m450.tictactoe.BoardTestHelper.toBoard;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import ch.bbw.m450.tictactoe.TicTacToePlayer.Stone;
import ch.bbw.m450.tictactoe.players.HumanPlayer;

class HumanPlayerTest implements WithAssertions {

	@Test
	@StdIo({ "4" })
	void givenStdIn_whenHumanPlays_thenMoveAndPrompt(StdOut out) {
		assertThat(new HumanPlayer().play(toBoard(EMPTY), Stone.CROSS)).isEqualTo(4);
		assertThat(out.capturedString()).contains("where to to put the next CROSS?");
	}

	@Test
	@StdIo({ "abc" })
	void givenTextInput_whenHumanPlays_thenThrows() {
		assertThatExceptionOfType(NumberFormatException.class)
				.isThrownBy(() -> new HumanPlayer().play(toBoard(EMPTY), Stone.CROSS));
	}
}
