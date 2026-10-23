package ch.bbw.m450.tictactoe;

import static ch.bbw.m450.tictactoe.BoardFixtures.EMPTY;
import static ch.bbw.m450.tictactoe.BoardTestHelper.toBoard;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import ch.bbw.m450.tictactoe.TicTacToePlayer.Stone;
import ch.bbw.m450.tictactoe.players.GreedyPlayer;
import ch.bbw.m450.tictactoe.players.HumanPlayer;

class PioneerConsoleTest implements WithAssertions {

	@Test
	@StdIo({ "4" })
	void givenStdIn_whenHumanPlays_thenMoveAndPrompt(StdOut out) {
		assertThat(new HumanPlayer().play(toBoard(EMPTY), Stone.CROSS)).isEqualTo(4);
		assertThat(out.capturedString()).contains("where to to put the next CROSS?");
	}

	@Test
	@StdIo
	void givenTwoGreedyPlayers_whenPlay_thenWinnerOnStdOut(StdOut out) {
		assertThat(TicTacToeMain.play(new GreedyPlayer(), new GreedyPlayer())).isEqualTo(Stone.CROSS);
		assertThat(out.capturedString()).contains("winner is: CROSS");
	}
}
