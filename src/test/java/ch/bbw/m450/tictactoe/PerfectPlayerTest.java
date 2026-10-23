package ch.bbw.m450.tictactoe;

import static ch.bbw.m450.tictactoe.BoardFixtures.EMPTY;
import static ch.bbw.m450.tictactoe.BoardFixtures.FULL_DRAW;
import static ch.bbw.m450.tictactoe.BoardTestHelper.toBoard;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ch.bbw.m450.tictactoe.TicTacToePlayer.Stone;
import ch.bbw.m450.tictactoe.players.GreedyPlayer;
import ch.bbw.m450.tictactoe.players.PerfectPlayer;

class PerfectPlayerTest implements WithAssertions {

	private final PrintStream originalOut = System.out;
	private final ByteArrayOutputStream out = new ByteArrayOutputStream();

	@BeforeEach
	void captureOut() {
		System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
	}

	@AfterEach
	void restoreOut() {
		System.setOut(originalOut);
	}

	@ParameterizedTest
	@CsvSource({
			".OXXX.OO., CROSS, 5",
			".OXXX.OO., CIRCLE, 8",
			"XX.OO...., CROSS, 2",
			EMPTY + ", CROSS, 0",
			"XOXXOOOX., CROSS, 8"
	})
	void givenBoard_whenPlay_thenBestMove(String layout, Stone color, int expected) {
		var board = toBoard(layout);
		var before = board.clone();
		assertThat(new PerfectPlayer().play(board, color)).isEqualTo(expected);
		assertThat(board).isEqualTo(before);
	}

	@Test
	void givenFullBoard_whenPlay_thenThrows() {
		assertThatIllegalStateException()
				.isThrownBy(() -> new PerfectPlayer().play(toBoard(FULL_DRAW), Stone.CROSS))
				.withMessage("cannot play at all");
	}

	@Test
	void givenTwoPerfectPlayers_whenPlay_thenDraw() {
		assertThat(TicTacToeMain.play(new PerfectPlayer(), new PerfectPlayer())).isNull();
		assertThat(out.toString(StandardCharsets.UTF_8)).contains("it's a draw!");
	}

	@Test
	void givenPerfectAgainstGreedy_whenPlay_thenPerfectWins() {
		assertThat(TicTacToeMain.play(new PerfectPlayer(), new GreedyPlayer())).isEqualTo(Stone.CROSS);
		assertThat(TicTacToeMain.play(new GreedyPlayer(), new PerfectPlayer())).isEqualTo(Stone.CIRCLE);
	}
}
