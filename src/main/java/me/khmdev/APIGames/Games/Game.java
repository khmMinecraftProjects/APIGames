package me.khmdev.APIGames.Games;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CItems;
import me.khmdev.APIBase.API;
import me.khmdev.APIBase.Almacenes.Almacen;
import me.khmdev.APIBase.Almacenes.Datos;
import me.khmdev.APIBase.Almacenes.local.LocAlmacen;
import me.khmdev.APIGames.APIG;
import me.khmdev.APIGames.Auxiliar.ConfigGames;
import me.khmdev.APIGames.Auxiliar.CustomSignGames;
import me.khmdev.APIGames.Auxiliar.IJugador;
import me.khmdev.APIGames.Auxiliar.Jugador;
import me.khmdev.APIGames.Auxiliar.ListenerGames;
import me.khmdev.APIGames.Auxiliar.NewCSGames;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Estado;
import me.khmdev.APIGames.Books.IBook;
import me.khmdev.APIGames.Books.SelectorGame;
import me.khmdev.APIGames.Books.Kits.GestorKit;
import me.khmdev.APIGames.Books.Ventajas.GestorDeVentajas;
import me.khmdev.APIGames.Books.Ventajas.InitBook;
import me.khmdev.APIGames.MarcadoresSQL.SQLGame;
import me.khmdev.APIGames.Partidas.IPartida;
import me.khmdev.APIMaps.APIM;
import me.khmdev.APIMaps.Auxiliar.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public abstract class Game implements IGame, Datos {
	protected API api;
	protected ListenerGames listener;
	protected HashMap<String, IPartida> partidas;
	protected HashMap<Location, IPartida> senal;
	protected String name = "",alias="";
	protected String iniciar = "iniciar", rA = "RA", rB = "RB", mp = "map",
			inf = "info";
	protected int max;
	protected boolean enable = false;
	protected SQLGame sql;
	protected Long time=(long) 0;
	protected GestorDeVentajas gestorV;
	protected GestorKit gestorK;
	protected IBook book;

	public Game(API ap,String nam, String string) {
		name=nam;
		alias=string;
		api = ap;
		partidas = new HashMap<String, IPartida>();
		senal = new HashMap<Location, IPartida>();
		max = 2;
		CItems.addNewsSign(new NewCSGames(this));
		sql=new SQLGame(this);
		initVentajas();
		initKits();
		book= initBook();
	}
	
	public IBook initBook(){
		return new InitBook(this);
	}
	
	public GestorDeVentajas getGestorVentaja(){
		return gestorV;
	}
	
	public GestorKit getGestorKit(){
		return gestorK;
	}

	public IBook getBook(){
		return book;
	}
	
	protected void initVentajas(){
		gestorV=SelectorGame.getGV(this,Material.BONE);
	}
	protected void initKits(){
		gestorK=SelectorGame.getGK(this,Material.ANVIL);
	}
	public void setEnable(boolean e) {
		if (e && !enable) {
			iniListen();
			changeEnablePartidas(e);
			api.getLogger().info(name + " activado");
			APIG.getInstance().getConf().saveConfig();
		} else if (!e && enable) {
			disableListen();
			api.getLogger().info(name + " desactivado");
			changeEnablePartidas(e);
			APIG.getInstance().getConf().saveConfig();
		}

		enable = e;
	}

	private void disableListen() {
		HandlerList.unregisterAll(listener);
	}

	public boolean isEnable() {
		return enable;
	}

	private void changeEnablePartidas(boolean b) {
		Iterator<Entry<String, IPartida>> iter = partidas.entrySet().iterator();
		while (iter.hasNext()) {
			iter.next().getValue().setEnable(b);
		}
	}

	public abstract void iniListen();

	public void listen() {
		api.getServer().getPluginManager().registerEvents(listener, api);
	}

	public boolean nuevaPartida(String key,  Map mapa) {
		if (partidas.containsKey(key)) {
			return false;
		}
		IPartida par = NPartida(key);
		par.setMapa(mapa);

		partidas.put(key, par);
		return true;
	}

	public IPartida getPartida(String name) {
		return partidas.get(name);
	}

	public Jugador getJugador(String name) {
		Iterator<Entry<String, IPartida>> iter = partidas.entrySet().iterator();
		while (iter.hasNext()) {
			IPartida ip = iter.next().getValue();
			if (ip.JugadorEsta(name)) {
				return ip.getJugador(name);
			}
		}
		return null;
	}

	public boolean containsJugador(String displayName) {
		Iterator<Entry<String, IPartida>> iter = partidas.entrySet().iterator();
		while (iter.hasNext()) {
			IPartida ip = iter.next().getValue();
			if (ip.JugadorEsta(displayName)) {
				return true;
			}
		}
		return false;
	}

	public IPartida getSPartida(Sign sign) {
		Iterator<Entry<Location, IPartida>> it = senal.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Location, IPartida> next = it.next();
			if (next.getKey().equals(sign.getLocation())) {
				return next.getValue();
			}
		}
		return null;
	}

	@Override
	public void guardar(Almacen nbt) {
		Iterator<Entry<String, IPartida>> it = partidas.entrySet().iterator();
		while (it.hasNext()) {

			Entry<String, IPartida> next = it.next();

			nbt.escribir((Datos) next.getValue(), next.getKey());
		}

	}

	@Override
	public void cargar(Almacen nbt) {

		Iterator<Almacen> it = nbt.getAlmacenes().iterator();

		while (it.hasNext()) {

			Almacen n = it.next();
			IPartida m = NPartida(n.getName());
			((Datos) m).cargar(n);
			partidas.put(n.getName(), m);
			if (m.getSign() != null) {
				senal.put(m.getSign().getLocation(), m);
				CItems.addSign(new CustomSignGames(this, m.getSign()));
			}
		}

	}

	public boolean containPartida(String string) {
		return partidas.containsKey(string);
	}

	public void removeSenal(Sign sign) {
		senal.remove(sign);
	}

	public boolean containSPartida(Sign s) {
		return senal.containsKey(s);
	}

	public String help() {
		String s = "";
		s += ("/APIG " + name + " (Partida) (Comando)" + "\n");
		s += ("Comandos: " + inf + "\n");
		s += ("          " + "listar" + "\n");
		s += ("          " + "on" + "\n");
		s += ("          " + "off" + "\n");
		s += ("          " + iniciar + "\n");
		s += ("          " + rA + "/" + rB + "  (Radio)" + "\n");
		s += ("          " + mp + " (Mapa)" + "\n");
		s += ("          " + "seg" + " (Mapa)" + "\n");
		s += ("          " + "sigs" + " (Mapa)" + "\n");
		return s;

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (args.length < 2) {
			sender.sendMessage(help());

			return true;
		}
		if (args[1].equalsIgnoreCase("listar")) {
			Iterator<Entry<String, IPartida>> it = partidas.entrySet()
					.iterator();
			sender.sendMessage("Listado de partidas: ");
			while (it.hasNext()) {
				Entry<String, IPartida> next = it.next();
				sender.sendMessage("  -"+next.getKey());
			}
			return true;
		}
		if (args.length < 3) {
			sender.sendMessage("No se ha introducido comando");
			sender.sendMessage(help());

			return true;
		}

		IPartida partida = NPartida(args[1]);
		if (partidas.containsKey(args[1])) {
			partida = getPartida(args[1]);
		}
		if (partida == null) {
			partida = NPartida(args[1]);
			partidas.put(args[1], partida);
		}
		if (args[2].equals(inf)) {
			sender.sendMessage(partida.Datos());
			return true;
		}
		if (args[2].equals(iniciar)) {
			if(!partida.isEnable()){
				partida.setEnable(true);
			}
			if (!partida.Funcional()) {
				sender.sendMessage("No se ha introducido todos los valores");
				sender.sendMessage(partida.Datos());

				return true;
			}
			
			partida.iniciar();
			sender.sendMessage("Partida " + args[1] + " iniciada");
			return true;

		}


		if (args[2].equals(mp)) {

			if (args.length < 4) {
				sender.sendMessage("No se ha introducido valores");

				return true;
			}

			partida.setMapa(APIM.getInstance().getMap(args[3]));
			if (partida.getMapa() == null) {
				sender.sendMessage("No existe mapa " + args[3]);
				return true;
			}
			sender.sendMessage("Se usara mapa " + args[3]);
			partidas.put(args[1], partida);

			return true;

		}

		if (args[2].equals("seg")) {
			if (args.length < 4) {
				sender.sendMessage("No se ha introducido Valor");
				return true;
			}
			Map m = APIM.getInstance().getMap(args[3]);
			if (m != null) {
				partida.setSegura(m);
				partidas.put(args[1], partida);

				sender.sendMessage("Guardada zona segura");
				return true;

			} else {
				sender.sendMessage("No existe Mapa");
				return true;

			}
		}

		if (args[2].equals("sigs")) {
			if (sender.getName() == "CONSOLE") {
				return true;
			}
			Player pl = sender instanceof Player?(Player)sender:null;
			Location a = LocAlmacen.cargar(pl, "LocA");
			
			if (a == null) {
				sender.sendMessage("No se ha introducido valores");

				return true;
			}
			if (partida.setSigS(a)) {
				partidas.put(args[1], partida);
				sender.sendMessage("Guardada Sign de zona segura");

				return true;
			}
			sender.sendMessage("No se ha guardado Sign de zona segura");

			return true;

		}
		if (args[2].equals("in")) {
			if (partida.getEstado() == Estado.EsperandoJugadores) {
				Player pl = sender instanceof Player?(Player)sender:null;
				if(pl==null){return true;}
				if (!partida.JugadorEsta(sender.getName())) {
					partida.nuevoGoJugador(pl);
					sender.sendMessage("Entra en partida " + partida.getName());
				} else {
					sender.sendMessage("Ya esta en la partida "
							+ partida.getName());

				}

			} else {
				sender.sendMessage("Partida " + partida.getName()
						+ " no disponible");
				return true;
			}

			return true;

		}
		sender.sendMessage(help());
		return true;
	}

	public String getName() {
		return name;
	}

	@Override
	public void addSenal(Sign sign, IPartida partida) {
		senal.put(sign.getLocation(), partida);
	}

	@Override
	public void addPartida(IPartida partida) {
		partidas.put(partida.getName(), partida);

	}

	@Override
	public void removePartida(IPartida partida) {
		partidas.remove(partida);
	}

	@Override
	public HashMap<String, IPartida> getPartidas() {
		return partidas;
	}

	public abstract IPartida NPartida(String s);

	public int getMax() {
		return max;
	}

	public void setMax(int num) {
		max = num;
	}

	public SQLGame getSql() {
		return sql;
	}

	public String getAlias(){
		return alias;
	}

	public void cargaConf(ConfigurationSection gam) {
		ConfigGames.loadGame(this,gam);
		ConfigGames.saveGame(this,gam);
		Iterator<Entry<String, IPartida>> it = getPartidas().entrySet()
				.iterator();

		while (it.hasNext()) {
			Entry<String, IPartida> next = it.next();
			if (gam.isConfigurationSection(next.getKey())) {
				next.getValue().cargaConf(gam.getConfigurationSection(next.getKey()));
			}else {
				next.getValue().cargaConf(gam.createSection(next.getKey()));
			}
			gam.set(next.getKey(), gam.getConfigurationSection(next.getKey()));
		}
	}
	@Override
	public Long getTime() {
		return time;
	}
	public void setTime(long tim){
		time=tim;
	}
	public List<IJugador> getJugadores(){
		List<IJugador> players=new LinkedList<>();
		for (IPartida p : partidas.values()) {
			Enumeration<IJugador> l=p.getJugadores();
			while (l.hasMoreElements()) {
				players.add(l.nextElement());
			}
		}
		return players;
	}

	public List<Player> getPlayers(){
		List<Player> players=new LinkedList<>();
		for (IPartida p : partidas.values()) {
			Enumeration<IJugador> l=p.getJugadores();
			while (l.hasMoreElements()) {
				players.add(l.nextElement().getPlayer());
			}
		}
		return players;
	}

}
