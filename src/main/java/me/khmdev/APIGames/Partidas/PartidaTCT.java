package me.khmdev.APIGames.Partidas;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.APIBase.API;
import me.khmdev.APIBase.Auxiliar.Auxiliar;
import me.khmdev.APIGames.Auxiliar.Control;
import me.khmdev.APIGames.Auxiliar.IJugador;
import me.khmdev.APIGames.Auxiliar.Jugador;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Equipo;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Estado;
import me.khmdev.APIGames.Games.IGame;
import me.khmdev.APIGames.Scores.BoardGames;
import me.khmdev.APIGames.Scores.BoardTCT;
import me.khmdev.APIGames.lang.Lang;
import me.khmdev.APIMaps.Auxiliar.Zona;

public abstract class PartidaTCT extends Partida{

	protected boolean inicio = false;
	public PartidaTCT(String s, IGame game2, API ap) {
		super(s, game2, ap);
		ReSp = new LinkedList<Location>();
		actualizarSign();
	}

	protected void setEquipo(Jugador j){
		j.setEquipo(Equipo.Ninguno);
	}
	
	public String Datos() {
		String s = "";
		s += ok(mapa) + "Mapa: " + dat(mapa) + "\n";
		s += "Opcional: \n";
		s += ok(sign) + "Sign: " + dat(sign) + "\n";
		return s;
	}

	public boolean Funcional() {
		return !(mapa == null);
	}

	public Location spawnZone(Jugador j) {
		return SpawnZone().clone().add(0.5, 0, 0.5);
	}

	public Location SpawnZone() {
		return ReSp.get((int) (Math.random() * ReSp.size()));
	}

	@Override
	public void iniControl() {
		control = new Control(this, game);
	}


	public void JugadorAbandona(Jugador j) {
		super.JugadorAbandona(j);
	}

	public boolean nuevoJugador(Player p) {

		if (Max <= jugadores.size()) {
			return false;
		}
		Jugador jug = newIJ();
		jug.setEquipo(Equipo.Ninguno);

		jug.setPlayer(p);
		jug.setPartida(this);
		JugadorEntra(jug);
		spawnZS(p);
		return true;
	}



	int numS = 100;
	boolean fin = false;

	public boolean SpawnCompleto() {
		return ReSp.size() > numS || fin;
	}

	public boolean SpawnFuncional() {
		return ReSp.size() != 0;
	}

	public void creaSpawners() {
		long ini = System.currentTimeMillis();
		while (!(SpawnCompleto())
				&& (ini + timeO) - System.currentTimeMillis() > 0) {
			fin = getFreeZone();

		}
	}

	protected List<Location> ReSp;
	private int pMax = 5;

	public boolean getFreeZone() {
		Zona zn = mapa.getZona();
		World w = mapa.getWorld();
		Location l = new Location(w, 0, 0, 0);
		if (ReSp.size() != 0) {
			Location bb = ReSp.get(ReSp.size() - 1);
			l.setX(bb.getX());
			l.setY(bb.getY());
			l.setZ(bb.getZ() + 1);
		} else {

			l.setX(zn.minX());
			l.setY(zn.minY());
			l.setZ(zn.minZ());

		}

		for (int x = (int) l.getX(); x < zn.maxX(); x++) {
			for (int y = (int) l.getY(); y < zn.maxY(); y++) {
				for (int z = (int) l.getZ(); z < zn.maxZ(); z++) {
					if (AuxPlayer.isPossibleSpawn(w, x, y, z)) {
						ReSp.add(new Location(w, x, y, z));
						return false;
					}
				}
				l.setZ(zn.minZ());
			}
			l.setY(zn.minY());
		}
		return true;
	}

	@Override
	public Jugador newIJ() {
		return new Jugador();
	}

	public void ganador(Jugador j) {

		if (j.getPuntuacion() >= pMax) {
			sendAll(Lang.get("fin_winnerPlayer").replace("%Winner%", j.toString()));
			gana(j);
			finalizar();

		}
	}
	
	public void ganadores(List<IJugador> j) {
		Enumeration<IJugador> it=getJugadores();

		while(it.hasMoreElements()){
			IJugador jug=it.nextElement();
			if(j.contains(jug)){
				gana((Jugador) jug);
			}else{
				pierde((Jugador)jug);
			}
		}
		if(j.size()==0){
			return;
		}else if(j.size()==1){
			sendAll(Lang.get("fin_winnerPlayer").replace("%Winner%", j.get(0).toString()));

		}else {
			Iterator<IJugador> ite=j.iterator();
			IJugador jug=ite.next();
			String s=jug.getPlayer().getName();

			while(ite.hasNext()){
				jug=ite.next();
				s+=", "+jug.getPlayer().getName();
				gana((Jugador) jug);
			}
			sendAll(Lang.get("fin_winnerPlayer").replace("%Winner%", s));
		}

		
	}
	public void clearJugadores() {
		Enumeration<IJugador> j = jugadores.elements();
		while (j.hasMoreElements()) {

			Jugador jj = (Jugador) j.nextElement();
			JugadorAbandona((Jugador) jj);
			jj.setGanador(0);

		}
	}

	@Override
	public BoardGames getScore() {
		return new BoardTCT(this);
	}

	@Override
	public void forzarFin() {
		Enumeration<IJugador> it=getJugadores();
		List<IJugador> list=new LinkedList<IJugador>();
		int p=0;
		while(it.hasMoreElements()){
			IJugador j=it.nextElement();
			if(j.getPuntuacion()==p){
				list.add(j);
			}else if(j.getPuntuacion()>p){
				list.clear();
				list.add(j);
			}
		}
		ganadores(list);
		this.setEstado(Estado.Finalizada);
	}
	@Override
	public void cargaConf(ConfigurationSection section){
		super.cargaConf(section);
		int max = Auxiliar.getNatural(section.getString("Puntuacion_Maxima"), -1);
		if (max != -1) {
			pMax=max;
		}
		section.set("Puntuacion_Maxima", pMax);
	}
	

}
