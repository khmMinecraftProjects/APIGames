package me.khmdev.APIGames.Games;


import java.util.HashMap;
import java.util.List;

import me.khmdev.APIGames.Auxiliar.IEnabler;
import me.khmdev.APIGames.Auxiliar.Jugador;
import me.khmdev.APIGames.Books.IBook;
import me.khmdev.APIGames.Books.Ventajas.GestorDeVentajas;
import me.khmdev.APIGames.MarcadoresSQL.SQLGame;
import me.khmdev.APIGames.Partidas.IPartida;
import me.khmdev.APIMaps.Auxiliar.Map;

import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public interface IGame extends IEnabler{

	public void listen();
	
	public boolean nuevaPartida(String key,  
			Map mapa);

	public IPartida getPartida(String name);
	
	public Jugador getJugador(String name);
	
	public List<Player> getPlayers();

	public IPartida getSPartida(Sign sign);

	public GestorDeVentajas getGestorVentaja();

	public void addSenal(Sign sign, IPartida partida);
		
	public int getMax();
	
	public void setMax(int num);
	
	public SQLGame getSql();

	public void addPartida(IPartida partida);
	
	public boolean containPartida(String string);

	public void removeSenal(Sign sign);

	public boolean containSPartida(Sign s);

	public void removePartida(IPartida partida);
	
	public String help();

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args);

	public String getName();
	
	public IPartida NPartida(String s);

	public boolean containsJugador(String displayName);

	HashMap<String, IPartida> getPartidas();

	public String getAlias();
	
	public void cargaConf(ConfigurationSection gam);

	public Long getTime();

	public void setTime(long time);
	
	public IBook initBook();
	
	public IBook getBook();
}
