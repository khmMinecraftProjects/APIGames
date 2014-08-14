package me.khmdev.APIGames.Scores;

import org.bukkit.entity.Player;

import me.khmdev.APIAuxiliar.ScoreBoard.Functor;
import me.khmdev.APIAuxiliar.ScoreBoard.ObjetiveData;
import me.khmdev.APIAuxiliar.ScoreBoard.getStringConst;
import me.khmdev.APIGames.Auxiliar.Variables;
import me.khmdev.APIGames.Partidas.PartidaEquipos2;

public class BoardE2 extends BoardGames {
	protected PartidaEquipos2 p2;

	public BoardE2(PartidaEquipos2 p) {
		super(p);
		p2=p;
		
		objs.add(new ObjetiveData(
				new getStringConst(Variables.A.chat+
						Variables.A.name),new Functor() {
			
			@Override
			public int getInt(Player p) {
				return p2.getpA();
			}
		}));
		objs.add(new ObjetiveData(
				new getStringConst(Variables.B.chat+
						Variables.B.name),new Functor() {
			
			@Override
			public int getInt(Player p) {
				return p2.getpB();
			}
		}));
	
	}

}
