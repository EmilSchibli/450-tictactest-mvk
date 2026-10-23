package ch.bbw.m450.tictactoe.players;

import java.util.Arrays;

import ch.bbw.m450.tictactoe.TicTacToeMain;
import ch.bbw.m450.tictactoe.TicTacToePlayer;

/**
 * Plays a perfect game. Same score: the lowest index.
 */
public class PerfectPlayer implements TicTacToePlayer {

	@Override
	public int play(Stone[] board, Stone colorToPlay) {
		var bestMove = -1;
		var bestScore = Integer.MIN_VALUE;
		for (var i = 0; i < TicTacToeMain.BOARD_SIZE; i++) {
			if (board[i] != null) {
				continue;
			}
			var next = Arrays.copyOf(board, board.length);
			next[i] = colorToPlay;
			var value = score(next, colorToPlay.opponent(), colorToPlay);
			if (value > bestScore) {
				bestScore = value;
				bestMove = i;
			}
		}
		if (bestMove < 0) {
			throw new IllegalStateException("cannot play at all");
		}
		return bestMove;
	}

	private int score(Stone[] board, Stone toMove, Stone me) {
		if (TicTacToeMain.isWin(board, me)) {
			return 1;
		}
		if (TicTacToeMain.isWin(board, me.opponent())) {
			return -1;
		}
		var best = toMove == me ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		var played = false;
		for (var i = 0; i < TicTacToeMain.BOARD_SIZE; i++) {
			if (board[i] != null) {
				continue;
			}
			played = true;
			var next = Arrays.copyOf(board, board.length);
			next[i] = toMove;
			var value = score(next, toMove.opponent(), me);
			best = toMove == me ? Math.max(best, value) : Math.min(best, value);
		}
		if (!played) {
			return 0;
		}
		return best;
	}
}
