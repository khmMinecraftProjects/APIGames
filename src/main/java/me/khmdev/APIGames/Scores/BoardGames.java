package me.khmdev.APIGames.Scores;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import me.khmdev.APIAuxiliar.ScoreBoard.Board;
import me.khmdev.APIAuxiliar.ScoreBoard.FunctorString;
import me.khmdev.APIGames.Partidas.IPartida;

public abstract class BoardGames extends Board {
	protected Scoreboard board = Bukkit.getScoreboardManager()
			.getNewScoreboard();

	public BoardGames(final IPartida p) {
		super(p.getName());

		title = new FunctorString() {

			@Override
			public String getString(Player pl) {
				if (p.getTime() > 0) {
					long sec = p.getTimeFalta() / 1000;
					String s = p.getName() + " " + ((int) sec / 60) + ":"
							+ ((int) sec % 60);

					s = s.length() > 16 ? s.substring(s.length() - 16) : s;
					return s.substring(0, Math.min(15, s.length()));
				} else {
					return p.getName();
				}
			}
		};

	}

	public void addScoreBoard(Player pl) {
		pl.setScoreboard(board);
	}

	public void DeSpawnBoard() {
		board.clearSlot(DisplaySlot.SIDEBAR);
	}

}
