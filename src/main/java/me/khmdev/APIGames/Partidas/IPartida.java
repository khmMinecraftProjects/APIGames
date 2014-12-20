package me.khmdev.APIGames.Partidas;

import java.util.Enumeration;

import me.khmdev.APIAuxiliar.Inventory.InventoryBase;
import me.khmdev.APIBase.API;
import me.khmdev.APIGames.Auxiliar.IControl;
import me.khmdev.APIGames.Auxiliar.IEnabler;
import me.khmdev.APIGames.Auxiliar.IJugador;
import me.khmdev.APIGames.Auxiliar.Jugador;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Equipo;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Estado;
import me.khmdev.APIGames.Books.Ventajas.GestorDeVentajas;
import me.khmdev.APIGames.Games.IGame;
import me.khmdev.APIGames.Scores.BoardGames;
import me.khmdev.APIMaps.Auxiliar.Map;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

public interface IPartida extends IEnabler {

	public IGame getGame();

	public boolean iniciar();

	public void comprobaciones();

	public void spawnJugadores();

	public void spawnJugador(Jugador jj);

	public void setEquipo(IJugador j,Equipo e);
	
	public void clearJugadores();

	public GestorDeVentajas getGestor();
	
	public void sendAll(String s);

	public void sendEquipo(String s, Equipo e);

	public String dat(Object obj);
	
	public void actualizarSign(SignChangeEvent event);
	
	public void actualizarSign();

	public void Gofinalizar();

	public void Cerrar();

	public boolean Funcional();
	
	public Map getMapa();

	public void setMapa(Map map);

	public int getNJug();

	public Estado getEstado();

	public void setEstado(Estado estad);

	public boolean JugadorEsta(String n);

	public Jugador getJugador(String n);

	public Enumeration<IJugador> getJugadores();

	public void JugadorEntra(Jugador s);

	public void JugadorGoAbandona(Jugador j);

	public boolean nuevoGoJugador(Player p);

	public Sign getSign();

	public void setSign(Sign sig);

	public IControl getControl() ;

	public String getName();

	public void iniciada();
	
	public String Datos();

	public API getApi();

	public void creaSpawners();

	public boolean SpawnCompleto();
	
	public boolean SpawnFuncional();

	public void setSegura(Map m);

	public boolean setSigS(Location a);

	public boolean isIn(Sign s);
	
	public void cargarEquipacion(Jugador j);

	InventoryBase getInventory();
	
	public int getMax();
	
	public void setMax(int max);

	public long getTime();

	public void forzarFin();

	public void cargaConf(ConfigurationSection configurationSection);

	public void setTime(long time);

	public BoardGames getScore();
	
	public void gana(Jugador j);
	
	public void pierde(Jugador j);
	
	public void kill(Jugador killer,Jugador death);

	public long getTimeFalta();

	public void sendAsToAll(Jugador j, String message);

	public void sendAsToTeam(Jugador j, String message);

	public void Equipar(Jugador j);

	public void JugadorGoRendirse(Jugador j);

	public void finalizar();
}
