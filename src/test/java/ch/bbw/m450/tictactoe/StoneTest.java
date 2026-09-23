package ch.bbw.m450.tictactoe;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ch.bbw.m450.tictactoe.TicTacToePlayer.Stone;

class StoneTest implements WithAssertions {

	@ParameterizedTest
	@CsvSource({ "CROSS, CIRCLE", "CIRCLE, CROSS" })
	void givenStone_whenOpponent_thenOtherStone(Stone stone, Stone expected) {
		assertThat(stone.opponent()).isEqualTo(expected);
	}
}
