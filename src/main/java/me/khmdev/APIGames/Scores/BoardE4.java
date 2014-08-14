package me.khmdev.APIGames.Scores;

import org.bukkit.entity.Player;

import me.khmdev.APIAuxiliar.ScoreBoard.Functor;
import me.khmdev.APIAuxiliar.ScoreBoard.ObjetiveData;
import me.khmdev.APIAuxiliar.ScoreBoard.getConstant;
import me.khmdev.APIAuxiliar.ScoreBoard.getStringConst;
import me.khmdev.APIGames.Auxiliar.Variables;
import me.khmdev.APIGames.Partidas.PartidaEquipos4;

public class BoardE4 extends BoardE2{
	protected PartidaEquipos4 p2;
	public BoardE4(PartidaEquipos4 p) {
		super(p);
		p2=p;
		objs.add(new ObjetiveData(null,new getConstant(-5)));
		
		objs.add(new ObjetiveData(
				new getStringConst(Variables.C.chat+
						Variables.C.name),new Functor() {
			
			@Override
			public int getInt(Player p) {
				return p2.getpC();
			}
		}));
	
		objs.add(new ObjetiveData(
				new getStringConst(Variables.D.chat+
						Variables.D.name),new Functor() {
			
			@Override
			public int getInt(Player p) {
				return p2.getpD();
			}
		}));
	}


}
