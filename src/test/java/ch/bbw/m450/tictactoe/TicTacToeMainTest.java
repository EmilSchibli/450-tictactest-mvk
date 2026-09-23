package ch.bbw.m450.tictactoe;

import static ch.bbw.m450.tictactoe.BoardFixtures.ANTI_DIAG_O;
import static ch.bbw.m450.tictactoe.BoardFixtures.BOTTOM_ROW_X;
import static ch.bbw.m450.tictactoe.BoardFixtures.DIAG_X;
import static ch.bbw.m450.tictactoe.BoardFixtures.EMPTY;
import static ch.bbw.m450.tictactoe.BoardFixtures.FULL_DRAW;
import static ch.bbw.m450.tictactoe.BoardFixtures.LEFT_COL_O;
import static ch.bbw.m450.tictactoe.BoardFixtures.MID_COL_X;
import static ch.bbw.m450.tictactoe.BoardFixtures.MID_ROW_O;
import static ch.bbw.m450.tictactoe.BoardFixtures.NO_WIN;
import static ch.bbw.m450.tictactoe.BoardFixtures.RIGHT_COL_O;
import static ch.bbw.m450.tictactoe.BoardFixtures.TOP_ROW_O;
import static ch.bbw.m450.tictactoe.BoardFixtures.TOP_ROW_X;
import static ch.bbw.m450.tictactoe.BoardTestHelper.isWin;
import static ch.bbw.m450.tictactoe.BoardTestHelper.toBoard;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import ch.bbw.m450.tictactoe.TicTacToePlayer.Stone;
import ch.bbw.m450.tictactoe.players.GreedyPlayer;

class TicTacToeMainTest implements WithAssertions {

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

	private String output() {
		return out.toString(StandardCharsets.UTF_8);
	}

	static Stream<Arguments> boards() {
		return Stream.of(
				arguments(TOP_ROW_X, Stone.CROSS, true),
				arguments(MID_ROW_O, Stone.CIRCLE, true),
				arguments(BOTTOM_ROW_X, Stone.CROSS, true),
				arguments(LEFT_COL_O, Stone.CIRCLE, true),
				arguments(MID_COL_X, Stone.CROSS, true),
				arguments(RIGHT_COL_O, Stone.CIRCLE, true),
				arguments(DIAG_X, Stone.CROSS, true),
				arguments(ANTI_DIAG_O, Stone.CIRCLE, true),
				arguments(TOP_ROW_O, Stone.CIRCLE, true),
				arguments(NO_WIN, Stone.CROSS, false),
				arguments(NO_WIN, Stone.CIRCLE, false),
				arguments(TOP_ROW_O, Stone.CROSS, false),
				arguments(EMPTY, Stone.CROSS, false),
				arguments("XX.......", Stone.CROSS, false),
				arguments("......OO.", Stone.CIRCLE, false),
				arguments(FULL_DRAW, Stone.CROSS, false),
				arguments(FULL_DRAW, Stone.CIRCLE, false));
	}

	@ParameterizedTest
	@MethodSource("boards")
	void givenBoard_whenIsWin_thenExpected(String layout, Stone color, boolean expected) {
		assertThat(isWin(layout, color)).isEqualTo(expected);
	}

	@Test
	void givenEmptyBoard_whenToString_thenShowsAllIndices() {
		var text = TicTacToeMain.toString(toBoard(EMPTY));
		for (var i = 0; i < TicTacToeMain.BOARD_SIZE; i++) {
			assertThat(text).contains(String.valueOf(i));
		}
		assertThat(text).doesNotContain("X", "O").hasLineCount(3);
	}

	@Test
	void givenCross_whenToString_thenShowsX() {
		var text = TicTacToeMain.toString(toBoard("X........"));
		assertThat(text).contains("\033[1mX\033[0m").doesNotContain("O", "\033[37m0");
	}

	@Test
	void givenCircle_whenToString_thenShowsO() {
		var text = TicTacToeMain.toString(toBoard("O........"));
		assertThat(text).contains("\033[1mO\033[0m").doesNotContain("X", "\033[37m0");
	}

	@Test
	void givenTwoGreedyPlayers_whenPlay_thenCrossWins() {
		assertThat(TicTacToeMain.play(new GreedyPlayer(), new GreedyPlayer())).isEqualTo(Stone.CROSS);
		assertThat(output()).contains("winner is: CROSS");
	}

	@Test
	void givenDrawMoves_whenPlay_thenNull() {
		var xMoves = List.of(0, 2, 3, 7, 8).iterator();
		var oMoves = List.of(1, 4, 5, 6).iterator();
		TicTacToePlayer x = (board, color) -> xMoves.next();
		TicTacToePlayer o = (board, color) -> oMoves.next();
		assertThat(TicTacToeMain.play(x, o)).isNull();
		assertThat(output()).contains("it's a draw!");
	}

	@Test
	void givenCircleMoves_whenPlay_thenCircleWins() {
		var xMoves = List.of(0, 1, 6).iterator();
		var oMoves = List.of(3, 4, 5).iterator();
		TicTacToePlayer x = (board, color) -> xMoves.next();
		TicTacToePlayer o = (board, color) -> oMoves.next();
		assertThat(TicTacToeMain.play(x, o)).isEqualTo(Stone.CIRCLE);
	}

	@Test
	void givenPlayers_whenPlay_thenEachGetsOwnColor() {
		TicTacToePlayer x = (board, color) -> {
			assertThat(color).isEqualTo(Stone.CROSS);
			return new GreedyPlayer().play(board, color);
		};
		TicTacToePlayer o = (board, color) -> {
			assertThat(color).isEqualTo(Stone.CIRCLE);
			return new GreedyPlayer().play(board, color);
		};
		TicTacToeMain.play(x, o);
	}

	@Test
	void givenSamePlayer_whenPlay_thenThrows() {
		var player = new GreedyPlayer();
		assertThatIllegalArgumentException().isThrownBy(() -> TicTacToeMain.play(player, player));
	}

	@ParameterizedTest
	@ValueSource(ints = { -1, 9 })
	void givenOutOfBoardMove_whenPlay_thenThrows(int move) {
		TicTacToePlayer x = (board, color) -> move;
		assertThatIllegalStateException().isThrownBy(() -> TicTacToeMain.play(x, new GreedyPlayer()))
				.withMessageContaining(String.valueOf(move));
		assertThat(output()).isEqualTo(TicTacToeMain.toString(toBoard(EMPTY)) + System.lineSeparator());
	}

	@Test
	void givenOccupiedField_whenPlay_thenThrows() {
		TicTacToePlayer x = (board, color) -> 0;
		TicTacToePlayer o = (board, color) -> 0;
		assertThatIllegalStateException().isThrownBy(() -> TicTacToeMain.play(x, o));
	}
}
