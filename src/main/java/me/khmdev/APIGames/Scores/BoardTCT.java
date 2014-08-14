package me.khmdev.APIGames.Scores;

import java.util.Enumeration;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import me.khmdev.APIGames.Auxiliar.IJugador;
import me.khmdev.APIGames.Partidas.PartidaTCT;


public class BoardTCT extends BoardGames {
	PartidaTCT pTCT;
	public BoardTCT(PartidaTCT p){
		super(p);
		pTCT=p;
	}
	protected void add2Board(Player p) {
		if(board==null){return;}
		if(title==null||title==null){return;}
		Objective objective = board.getObjective(title.getString(p));
		if (objective != null) {
			objective.unregister();	
		}
		objective = board.registerNewObjective(title.getString(p), title.getString(p));
		Enumeration<IJugador> jugadores=pTCT.getJugadores();
		while (jugadores.hasMoreElements()) {
			IJugador j=jugadores.nextElement();
			Score scr = objective
					.getScore(j.getPlayer().getName());
							scr.setScore(j.getPuntuacion());

		}
		
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

	}
}
