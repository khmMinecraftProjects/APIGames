package me.khmdev.APIGames.Auxiliar;

import me.khmdev.APIAuxiliar.Players.IGetPlayer;
import me.khmdev.APIAuxiliar.ScoreBoard.IBoard;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Equipo;
import me.khmdev.APIGames.Partidas.IPartida;
import me.khmdev.APIGames.Partidas.Partida;

import org.bukkit.entity.Player;

public interface IJugador extends IGetPlayer{

	public IPartida getPartida();
	
	public void setPartida(Partida partida);
	
	public Equipo getEquipo();
	public void setEquipo(Equipo equipo);
	
	public String toString();

	public void setPlayer(Player p);

	public int getPuntuacion();

	public void setPuntuacion(int puntuacion);
	
	public IBoard getLastBoard();
	
	public void setLastBoard(IBoard b);
	
	public boolean abandonado();

	public void abandona();

	public void setTag(String s);
	
	public String getTag();

	public void setTab(String s);
	
	public String getTab();
}
