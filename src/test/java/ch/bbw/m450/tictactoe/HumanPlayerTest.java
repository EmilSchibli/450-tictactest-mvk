package ch.bbw.m450.tictactoe;

import static ch.bbw.m450.tictactoe.BoardFixtures.EMPTY;
import static ch.bbw.m450.tictactoe.BoardTestHelper.toBoard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.bbw.m450.tictactoe.TicTacToePlayer.Stone;
import ch.bbw.m450.tictactoe.players.HumanPlayer;

class HumanPlayerTest implements WithAssertions {

	private final InputStream originalIn = System.in;
	private final PrintStream originalOut = System.out;
	private final ByteArrayOutputStream out = new ByteArrayOutputStream();

	@BeforeEach
	void captureOut() {
		System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
	}

	@AfterEach
	void restore() {
		System.setIn(originalIn);
		System.setOut(originalOut);
	}

	private static void input(String text) {
		System.setIn(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)));
	}

	@Test
	void givenNumberInput_whenPlay_thenReturnsNumber() {
		input("4\n");
		assertThat(new HumanPlayer().play(toBoard(EMPTY), Stone.CROSS)).isEqualTo(4);
		assertThat(out.toString(StandardCharsets.UTF_8)).contains("where to to put the next CROSS?");
	}

	@Test
	void givenTextInput_whenPlay_thenThrows() {
		input("abc\n");
		assertThatExceptionOfType(NumberFormatException.class)
				.isThrownBy(() -> new HumanPlayer().play(toBoard(EMPTY), Stone.CROSS));
	}
}
