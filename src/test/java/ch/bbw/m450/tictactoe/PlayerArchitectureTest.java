package ch.bbw.m450.tictactoe;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.importer.ClassFileImporter;

class PlayerArchitectureTest implements WithAssertions {

	@Test
	void givenPlayerPackage_whenChecked_thenClassesImplementTicTacToePlayer() {
		var imported = new ClassFileImporter().importPackages("ch.bbw.m450.tictactoe.players");
		assertThat(imported).hasSize(3);
		classes()
				.that().resideInAPackage("ch.bbw.m450.tictactoe.players")
				.should().implement(TicTacToePlayer.class)
				.check(imported);
	}
}
