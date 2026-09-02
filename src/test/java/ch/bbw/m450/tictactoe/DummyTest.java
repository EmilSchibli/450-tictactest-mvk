package ch.bbw.m450.tictactoe;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class DummyTest implements WithAssertions {

	@Test
	void givenTrue_whenJunitAssertTrue_thenPasses() {
		assertTrue(true);
	}

	@Test
	void givenTrue_whenAssertJAssertTrue_thenPasses() {
		assertThat(true).isTrue();
	}
}
