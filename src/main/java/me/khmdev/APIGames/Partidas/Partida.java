package me.khmdev.APIGames.Partidas;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import me.khmdev.APIAuxiliar.ConstantesAuxiliar;
import me.khmdev.APIAuxiliar.Effects.BossBar;
import me.khmdev.APIAuxiliar.Inventory.InventoryBase;
import me.khmdev.APIAuxiliar.Inventory.StandarInventorys;
import me.khmdev.APIAuxiliar.Players.NamesTags;
import me.khmdev.APIAuxiliar.Players.VisiblesPlayer;
import me.khmdev.APIAuxiliar.ScoreBoard.IBoard;
import me.khmdev.APIAuxiliar.ScoreBoard.runBoard;
import me.khmdev.APIBase.API;
import me.khmdev.APIBase.Almacenes.Almacen;
import me.khmdev.APIBase.Almacenes.Datos;
import me.khmdev.APIEconomy.Own.APIEconomy;
import me.khmdev.APIGames.Auxiliar.ConfigGames;
import me.khmdev.APIGames.Auxiliar.IControl;
import me.khmdev.APIGames.Auxiliar.IJugador;
import me.khmdev.APIGames.Auxiliar.Jugador;
import me.khmdev.APIGames.Auxiliar.ListenerGames;
import me.khmdev.APIGames.Auxiliar.Variables;
import me.khmdev.APIGames.Auxiliar.ZonaSegura;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Equipo;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Estado;
import me.khmdev.APIGames.Books.Ventajas.GestorDeVentajas;
import me.khmdev.APIGames.Games.IGame;
import me.khmdev.APIGames.ListenAPIG.jugador.JugadorAbandonaEvent;
import me.khmdev.APIGames.ListenAPIG.jugador.JugadorEntraEvent;
import me.khmdev.APIGames.ListenAPIG.jugador.JugadorFinalizaEvent;
import me.khmdev.APIGames.ListenAPIG.jugador.PreJugadorEntraEvent;
import me.khmdev.APIGames.ListenAPIG.partida.PartidaFinalizaEvent;
import me.khmdev.APIGames.MarcadoresSQL.ControlKills;
import me.khmdev.APIGames.MarcadoresSQL.MarcadoresSQL;
import me.khmdev.APIGames.Scores.BoardGames;
import me.khmdev.APIGames.lang.Lang;
import me.khmdev.APIMaps.APIM;
import me.khmdev.APIMaps.Auxiliar.Map;

public abstract class Partida implements IPartida, Datos {
	protected boolean ReInicio = true, inicio = true;
	protected IGame game;
	protected ListenerGames listen;
	protected Hashtable<String, IJugador> jugadores;
	protected Map mapa;
	protected int Max = 1;
	protected Estado estado;
	protected Sign sign;
	protected String name;
	protected IControl control;
	public API api;
	protected final long timeO = 500;
	protected long time = -1;
	protected ZonaSegura segura;
	protected BoardGames scores;
	protected InventoryBase inventory = ConstantesAuxiliar.standar;

	private boolean enable = false;

	public boolean isIn(Sign s) {
		if (sign != null && (sign.equals(s))) {
			return true;
		}
		return false;
	}

	public void kill(Jugador killer, Jugador death) {
		ControlKills.kill(killer.getPlayer());

	}

	public void pierde(Jugador j) {
		JugadorGoAbandona(j);
		j.setGanador(0);
	}

	public void gana(Jugador j) {
		JugadorGoAbandona(j);
		j.setGanador(1);
	}

	public InventoryBase getInventory() {
		return inventory;
	}

	public GestorDeVentajas getGestor() {
		return gestor;
	}

	protected GestorDeVentajas gestor;

	public Partida(String s, IGame game2, API ap) {
		api = ap;
		estado = Estado.SinIniciar;
		game = game2;
		jugadores = new Hashtable<String, IJugador>();
		name = s;
		scores = getScore();
		runBoard.addBoard(scores);
		Max = game.getMax();
		time = game.getTime();
		gestor = game.getGestorVentaja();
	}

	public abstract BoardGames getScore();

	protected void inAll() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			nuevoJugador(player);
		}
	}

	@SuppressWarnings("deprecation")
	public void Equipar(Jugador j) {

		j.getPlayer().getInventory().clear();
		j.getPlayer().updateInventory();

		if (inventory == null) {
			return;
		}
		DyeColor cl = null;
		if (j.getEquipo() == Equipo.A) {
			cl = Variables.A.dye;
		} else if (j.getEquipo() == Equipo.B) {
			cl = Variables.B.dye;
		} else if (j.getEquipo() == Equipo.C) {
			cl = Variables.C.dye;
		} else if (j.getEquipo() == Equipo.D) {
			cl = Variables.D.dye;
		}
		PlayerInventory inv = j.getPlayer().getInventory();
		ItemStack[] armor = inventory.getArmor(), invent = inventory
				.getInventory();
		if (API.getMetadata(j.getPlayer(), game.getName() + "_kit") != null) {
			String i = API.getMetadata(j.getPlayer(), game.getName() + "_kit")
					.asString();
			InventoryBase in = StandarInventorys.getInventory(i);
			API.removeMetadata(j.getPlayer(), i + "_buy");
			API.removeMetadata(j.getPlayer(), game.getName() + "_kit");
			if (in != null) {
				armor = in.getArmor();
				invent = in.getInventory();
			}

		}

		if (cl != null) {
			for (int i = 0; i < armor.length; i++) {
				if (armor[i] != null
						&& ConstantesAuxiliar.leather.contains(armor[i]
								.getType())) {
					LeatherArmorMeta meta = (LeatherArmorMeta) armor[i]
							.getItemMeta();
					meta.setColor(cl.getColor());
					armor[i].setItemMeta(meta);
				}
			}
		}

		if (cl != null) {
			for (int i = 0; i < invent.length; i++) {
				if (invent[i] != null
						&& invent[i].getType().equals(Material.WOOL)) {
					invent[i] = new ItemStack(Material.WOOL,
							invent[i].getAmount(), cl.getData());
				}
			}
		}

		inv.setArmorContents(armor);

		inv.setContents(invent);
	}

	@SuppressWarnings("deprecation")
	protected void addSecureItems(Player pl) {
		pl.getInventory().addItem(game.getBook().getBook().getItem());
		pl.updateInventory();
	}

	@SuppressWarnings("deprecation")
	protected void limpiarEquipacion(Jugador j) {
		PlayerInventory inv = j.getPlayer().getInventory();
		j.setArmor(inv.getArmorContents());
		j.setInventario(inv.getContents());
		j.getPlayer().getInventory().clear();
		inv.setArmorContents(null);
		j.getPlayer().updateInventory();
	}

	public void cargarEquipacion(Jugador j) {
		PlayerInventory inv = j.getPlayer().getInventory();
		if (j.getInventario() == null) {
			return;
		}
		inv.setArmorContents(j.getArmor());

		inv.setContents(j.getInventario());
	}

	public abstract void iniControl();

	public boolean iniciar() {
		if (!isEnable()) {
			return false;
		}
		if (!Funcional()) {

			setEstado(Estado.NoDisponible);
			return false;
		}
		if (estado != Estado.SinIniciar) {
			return false;
		}

		setEstado(Estado.Regenerando);
		APIM.getInstance().cargaMapa(mapa, null);
		for (Chunk c : mapa.getChunks()) {
			mapa.getWorld().loadChunk(c);
		}
		actualizarSign();

		int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(api,
				(BukkitRunnable) control, 0L, 10L);
		control.setId(id);
		return true;
	}

	public void comprobaciones() {

	}

	public void spawnJugadores() {
		Iterator<Entry<String, IJugador>> j = jugadores.entrySet().iterator();
		while (j.hasNext()) {
			spawnJugador((Jugador) j.next().getValue());
		}
	}

	public void spawnJugador(Jugador jj) {
		jj.getPlayer().teleport(spawnZone(jj));
		for (PotionEffect effect : jj.getPlayer().getActivePotionEffects()) {
			jj.getPlayer().removePotionEffect(effect.getType());
		}
		jj.getPlayer().setMaxHealth(20);
		jj.getPlayer().setHealth(20);
		Equipar(jj);
	}

	public void clearJugadores() {
		Enumeration<IJugador> j = jugadores.elements();
		while (j.hasMoreElements()) {

			IJugador jj = j.nextElement();
			JugadorGoAbandona((Jugador) jj);

		}
	}

	public void DesSpawn(Player pl) {
		if (sign != null) {
			pl.teleport(sign.getLocation());

		} else {
			pl.teleport(pl.getWorld().getSpawnLocation());
		}
	}

	public void sendAll(String s) {
		Iterator<Entry<String, IJugador>> j = jugadores.entrySet().iterator();
		while (j.hasNext()) {
			j.next()
					.getValue()
					.getPlayer()
					.sendMessage(
							Lang.get("send_all")
									.replace("%clr%",
											Variables.ChatColorStandar)
									.replace("%Game%", game.getAlias())
									.replace("%Partida%", name)
									.replace(
											"%msg%",
											ChatColor
													.translateAlternateColorCodes(
															'&', s)));
		}
	}

	public void sendAsToAll(Jugador j, String message) {
		Iterator<Entry<String, IJugador>> jj = jugadores.entrySet().iterator();
		message = Lang
				.get("send_selective_all")
				.replace(
						"%clr%",
						Variables.get(j.getEquipo()) != null ? Variables.get(j
								.getEquipo()).chat + "" : "")
				.replace("%Player%", j.getPlayer().getName())
				.replace("%msg%",
						ChatColor.translateAlternateColorCodes('&', message));
		while (jj.hasNext()) {

			jj.next().getValue().getPlayer().sendMessage(message);
		}
	}

	public void sendAsToTeam(Jugador j, String message) {
		Iterator<Entry<String, IJugador>> jj = jugadores.entrySet().iterator();
		message = Lang
				.get("send_selective_team")
				.replace(
						"%clr%",
						Variables.get(j.getEquipo()) != null ? Variables.get(j
								.getEquipo()).chat + "" : "")
				.replace("%Player%", j.getPlayer().getName())
				.replace("%msg%",
						ChatColor.translateAlternateColorCodes('&', message));
		while (jj.hasNext()) {
			IJugador other = jj.next().getValue();
			if (other.getEquipo() == j.getEquipo()) {
				other.getPlayer().sendMessage(ChatColor.ITALIC + message);
			}
		}
	}

	public void sendEquipo(String s, Equipo e) {
		String colr = Variables.ChatColorStandar;
		if (e == Equipo.A) {
			colr = Variables.A.chat + "";
		} else if (e == Equipo.B) {
			colr = Variables.B.chat + "";
		} else if (e == Equipo.C) {
			colr = Variables.C.chat + "";
		} else if (e == Equipo.D) {
			colr = Variables.D.chat + "";
		}
		Enumeration<IJugador> j = jugadores.elements();
		while (j.hasMoreElements()) {
			Jugador jg = (Jugador) j.nextElement();
			if (jg.getEquipo() == e) {
				jg.getPlayer().sendMessage(
						Lang.get("send_all")
								.replace("%clr%", colr)
								.replace("%Game%", game.getAlias())
								.replace("%Partida%", name)
								.replace(
										"%msg%",
										ChatColor.translateAlternateColorCodes(
												'&', s)));
			}
		}
	}

	public String Datos() {
		String s = "";
		s += ok(mapa) + "Mapa: " + dat(mapa) + "\n";
		s += "Opcional: \n";
		s += ok(sign) + "Sign: " + dat(sign) + "\n";
		if (segura != null) {
			s += ok(segura.getMap()) + "Zona segura: " + dat(segura.getMap())
					+ "\n";

			s += ok(segura.getSign()) + "Sign de ZS: " + dat(segura.getSign())
					+ "\n";
		} else {
			s += ok(segura) + "Zona segura:: " + dat(segura) + "\n";
			s += ok(segura) + "Sign de ZS: " + dat(segura) + "\n";

		}
		return s;
	}

	public String dat(Object obj) {
		String s = "";
		if (obj == null) {
			s = "";
		} else {
			s = obj.toString();
		}
		return s;
	}

	public String ok(Object obj) {
		String s = "";
		if (obj == null) {
			s = "[ ]";
		} else {
			s = "[X]";
		}
		return s;
	}

	public void actualizarSign(SignChangeEvent event) {
		event.setLine(0,
				ChatColor.BLACK + "[" + ChatColor.DARK_AQUA + game.getAlias()
						+ ChatColor.BLACK + "]");
		event.setLine(1, ChatColor.YELLOW + name);
		if (getEstado() == Estado.EsperandoJugadores) {
			event.setLine(2, ChatColor.GREEN + "Esperando...");
		} else {
			event.setLine(2, ChatColor.RED + getEstado().toString());

		}
		event.setLine(3, ChatColor.YELLOW + "" + getNJug() + "/" + getMax());
	}

	public void actualizarSign() {
		if (sign != null) {
			sign.setLine(
					0,
					ChatColor.BLACK + "[" + ChatColor.DARK_AQUA
							+ game.getAlias() + ChatColor.BLACK + "]");
			sign.setLine(1, ChatColor.YELLOW + name);
			if (getEstado() == Estado.EsperandoJugadores) {
				sign.setLine(2, ChatColor.GREEN + "Esperando...");
			} else {
				sign.setLine(2, ChatColor.RED + getEstado().toString());

			}
			sign.setLine(3, ChatColor.YELLOW + "" + getNJug() + "/" + getMax());
			sign.update();
		}
		if (segura != null) {
			String s = "";
			if (getEstado() == Estado.EsperandoJugadores) {
				s = ChatColor.GREEN + getEstado().toString();
			} else {
				s = ChatColor.RED + getEstado().toString();
			}
			segura.actualizarSign("", s, ChatColor.YELLOW + "" + getNJug()
					+ "/" + getMax(), "");
		}
	}

	public void ganador() {

	}

	public final void Gofinalizar() {
		manager.callEvent(new PartidaFinalizaEvent(this));
		finalizar();
	}

	public void finalizar() {
		if (control != null) {
			control.ForceFin();
			iniControl();
		}
		scores.clearPlayers();
		for (Entry<String, IJugador> j : jugadores.entrySet()) {
			if (j.getValue().getLastBoard() != null) {
				j.getValue().getLastBoard().addPlayer(j.getValue().getPlayer());
			}
		}
		this.setEstado(Estado.SinIniciar);
		game.removePartida(this);
		clearJugadores();
		if (ReInicio) {
			iniciar();
		}
	}

	public void Cerrar() {
		setEstado(Estado.NoDisponible);
	}

	public boolean Funcional() {
		return (mapa != null);
	}

	public Map getMapa() {
		return mapa;
	}

	public void setMapa(Map map) {
		mapa = map;
		setEstado(Estado.SinIniciar);
	}

	public int getNJug() {
		return jugadores.size();
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estad) {
		this.estado = estad;
		actualizarSign();
	}

	public boolean JugadorEsta(String n) {
		return jugadores.containsKey(n);
	}

	public Jugador getJugador(String n) {
		return (Jugador) jugadores.get(n);
	}

	public Enumeration<IJugador> getJugadores() {
		return jugadores.elements();
	}

	public void JugadorEntra(Jugador j) {
		jugadores.put(j.getPlayer().getName(), j);
		limpiarEquipacion(j);
		actualizarSign();
		sendEquipo(Lang.get("login_player").replace("%Player%", j.toString()),
				j.getEquipo());

	}

	protected int calcularCoins(Jugador j) {
		int coins = 0;
		Player pl = j.getPlayer();
		String v = Variables.ChatColorStandar;
		pl.sendMessage(Lang.get("partida.endGame1")
				.replace("%cl%",v));
		pl.sendMessage(Lang.get("partida.endGame2")
				.replace("%cl%",v));
		coins += 5;
		pl.sendMessage(Lang.get("partida.endGame3")
				.replace("%cl%",v)
				.replace("%kcoins%",j.getKills()+"")
				.replace("%ktot%",(j.getKills() * 2)+""));
		coins += j.getKills() * 2;

		pl.sendMessage(Lang.get("partida.endGame4")
				.replace("%cl%",v)
				.replace("%dcoins%",j.getDeaths()+"")
				.replace("%dtot%",j.getDeaths()+""));
		coins -= j.getDeaths();

		pl.sendMessage(Lang.get("partida.endGame5")
				.replace("%cl%",v)
				.replace("%pcoins%",j.getPuntuacion()+"")
				.replace("%ptot%",j.getPuntuacion() * 3+""));
		
		coins += j.getPuntuacion() * 3;

		if (j.isGanador() == 1) {
			pl.sendMessage(Lang.get("partida.endGamew")
					.replace("%cl%",v));
			coins += 10;
		}
		j.getPlayer().sendMessage(
				v + Lang.get("partida.endGame6").replace("%Coins%", "" + coins));
		pl.sendMessage(Lang.get("partida.endGame7")
				.replace("%cl%",v));

		return coins;
	}

	public final void JugadorGoAbandona(Jugador j) {
		manager.callEvent(new JugadorFinalizaEvent(j));
		JugadorAbandona(j);
	}

	public final void JugadorGoRendirse(Jugador j) {
		JugadorAbandonaEvent e = new JugadorAbandonaEvent(j);
		manager.callEvent(new JugadorFinalizaEvent(j));
		if (!e.isCancelled()) {
			JugadorAbandona(j);
		}
	}

	public void JugadorAbandona(Jugador j) {
		BossBar.removeBarPlayer(j.getPlayer());

		// API.getInstance().getSpam().sendMensaje(j.getPlayer());
		API.removeMetadata(j.getPlayer(), "Jugando");
		MarcadoresSQL.addMarcador(j);
		scores.removePlayer(j.getPlayer());
		if (j.getLastBoard() != null) {
			j.getLastBoard().addPlayer(j.getPlayer());
		}
		for (PotionEffect pot : j.getPlayer().getActivePotionEffects()) {
			j.getPlayer().removePotionEffect(pot.getType());
		}
		jugadores.remove(j.getPlayer().getName());
		resetDisplay(j);
		VisiblesPlayer.resetearJugadores(j.getPlayer());
		VisiblesPlayer.mostrame(j.getPlayer());
		if (estado != Estado.EnCurso && !j.abandonado()) {
			int coins = calcularCoins(j);
			APIEconomy.addCash(j.getPlayer().getName(), coins);
		}
		cargarEquipacion(j);
		DesSpawn(j.getPlayer());
		actualizarSign();
		if (estado == Estado.EnCurso && jugadores.size() <= 1) {
			sendAll(Lang.get("fin_abandono"));
			forzarFin();
		}
		if (gestor != null) {
			gestor.removeJugador(j);
		}

	}

	public boolean SpawnCompleto() {
		return ((segura == null) || (segura.getMap() != null && segura
				.Completo()));
	}

	public boolean SpawnFuncional() {
		return ((segura == null) || (segura.getMap() != null && !segura
				.rVacio()));
	}

	public void creaSpawners() {
		long ini = System.currentTimeMillis();

		if (segura != null && segura.getMap() != null && !segura.Completo()) {
			ini = System.currentTimeMillis();
			boolean b = false;

			while (!(segura.Completo()) && !b
					&& (ini + timeO) - System.currentTimeMillis() > 0) {
				b = segura.getFreeZone();

			}
		}
	}

	protected void changeDisplay(IJugador j) {
		j.setTag(NamesTags.getName(j.getPlayer()));
		j.setTab(j.getPlayer().getPlayerListName());
		String clr = "";
		if (j.getEquipo() == Equipo.A) {
			clr = Variables.A.chat + "";
		} else if (j.getEquipo() == Equipo.B) {
			clr = Variables.B.chat + "";
		} else if (j.getEquipo() == Equipo.C) {
			clr = Variables.C.chat + "";
		} else if (j.getEquipo() == Equipo.D) {
			clr = Variables.D.chat + "";
		}
		clr = clr + j.getPlayer().getName();
		if (clr.length() > 16) {
			clr = clr.substring(0, 15);
		}
		NamesTags.addPlayer(j.getPlayer(), clr);
		j.getPlayer().setPlayerListName(clr);
	}

	protected void resetDisplay(Jugador j) {
		if (j.getTag() != null) {
			NamesTags.addPlayer(j.getPlayer(), j.getTag());
		} else {
			NamesTags.removePlayer(j.getPlayer());
			NamesTags.actualizar(j.getPlayer());
		}
		if (j.getTab() != null) {
			j.getPlayer().setPlayerListName(j.getTab());
		} else {
			j.getPlayer().setPlayerListName(j.getPlayer().getName());
		}
	}

	@Override
	public void guardar(Almacen nbt) {

		ReInicio = false;
		Gofinalizar();

		if (mapa != null) {

			nbt.setString("mapa", mapa.getName());
		}
		nbt.setString("wS", "");

		if (sign != null
				&& (sign.getBlock().getType() == Material.SIGN_POST || sign
						.getBlock().getType() == Material.WALL_SIGN)) {

			int[] vecS = { (int) sign.getBlock().getX(),
					(int) sign.getBlock().getY(), (int) sign.getBlock().getZ() };
			nbt.setIntArray("sign", vecS);
			nbt.setString("wS", sign.getWorld().getName());
		}

		if (segura != null) {
			if (segura.getMap() != null) {
				nbt.setString("MapS", segura.getMap().getName());
			}
			if (segura.getSign() != null) {
				Sign s = segura.getSign();
				int[] vecS = { (int) s.getBlock().getX(),
						(int) s.getBlock().getY(), (int) s.getBlock().getZ() };
				nbt.setIntArray("signS", vecS);
				nbt.setString("wSS", s.getWorld().getName());
			}
		}
	}

	@Override
	public void cargar(Almacen nbt) {

		name = nbt.getName();

		String w = nbt.getString("wS");

		if (w.length() != 0) {
			int[] vec = nbt.getIntArray("sign");
			Block b = Bukkit.getServer().getWorld(w)
					.getBlockAt(vec[0], vec[1], vec[2]);
			if (b.getType() == Material.SIGN_POST
					|| b.getType() == Material.WALL_SIGN) {
				setSign((Sign) b.getState());
				API.setMetadata(b, "Game", game.getName());
				API.setMetadata(b, "Partida", getName());
				actualizarSign();
			}
		}

		String m = nbt.getString("mapa");

		if (APIM.getInstance().containsMap(m)) {
			mapa = APIM.getInstance().getMap(m);
		}

		m = nbt.getString("MapS");
		Map map = APIM.getInstance().getMap(m);
		if (map != null) {
			setSegura(map);
			w = nbt.getString("wSS");
			if (w.length() != 0) {
				int[] vec = nbt.getIntArray("signS");
				Location l = new Location(Bukkit.getServer().getWorld(w),
						vec[0], vec[1], vec[2]);
				segura.createSign(l);
			}
		}
		if (inicio) {
			iniciar();
		}

	}

	public abstract Jugador newIJ();

	PluginManager manager = Bukkit.getServer().getPluginManager();

	public final boolean nuevoGoJugador(Player p) {
		PreJugadorEntraEvent e = new PreJugadorEntraEvent(p, this);
		manager.callEvent(e);

		if (e.isCancelled()) {
			return false;
		}

		if (!nuevoJugador(p)) {
			return false;
		}
		manager.callEvent(new JugadorEntraEvent(getJugador(p.getName())));
		return true;
	}

	public boolean nuevoJugador(Player p) {

		if (Max <= jugadores.size()) {
			return false;
		}
		API.setMetadata(p, "Jugando", getName());
		Jugador jug = newIJ();
		// setEquipo(jug);
		jug.setPlayer(p);
		jug.setPartida(this);
		JugadorEntra(jug);

		spawnZS(p);
		addSecureItems(p);
		return true;
	}

	protected abstract void setEquipo(Jugador j);

	protected boolean spawnZS(Player p) {
		if (segura != null && segura.getMap() != null && !segura.rVacio()) {
			p.teleport(segura.SpawnZone());
			return true;
		}
		return false;
	}

	public Sign getSign() {
		return sign;
	}

	public void setSign(Sign sig) {
		this.sign = sig;
		game.addSenal(sign, this);
		actualizarSign();

	}

	public IGame getGame() {
		return game;
	}

	public IControl getControl() {
		return control;

	}

	public int getMax() {
		return Max;
	}

	public void setMax(int max) {
		Max = max;
	}

	public String getName() {
		return name;
	}

	private void setEquipos() {
		for (Entry<String, IJugador> j : jugadores.entrySet()) {

			if (j.getValue().getEquipo() == Equipo.Ninguno) {
				setEquipo((Jugador) j.getValue());
			}

		}

	}

	protected long init = 0;

	public void iniciada() {
		setEquipos();
		for (Entry<String, IJugador> j : jugadores.entrySet()) {
			changeDisplay(j.getValue());
			IBoard b = runBoard.getBoard(j.getValue().getPlayer());
			if (b != null) {
				b.removePlayer(j.getValue().getPlayer());
			}
			j.getValue().setLastBoard(b);
			scores.addPlayer(j.getValue().getPlayer());
		}
		asegurar();
		spawnJugadores();
		crearTab();
		init = System.currentTimeMillis();
		if (gestor != null) {
			gestor.CargarVentajas(jugadores.elements());
		}

	}

	protected void asegurar() {
		mapa.asegurarMargenes();
	}

	protected void crearTab() {
		for (Entry<String, IJugador> j : jugadores.entrySet()) {
			VisiblesPlayer.esconderJugadores(j.getValue().getPlayer());
			VisiblesPlayer.mostrarLista(j.getValue().getPlayer(),
					getJugadores());
		}

	}

	public API getApi() {
		return api;
	}

	public abstract Location spawnZone(Jugador j);

	public ZonaSegura getSegura() {
		return segura;
	}

	public void setSegura(Map m) {

		if (segura == null) {
			segura = new ZonaSegura(this, m);
			return;
		}
		segura.setMap(m);
	}

	public boolean setSigS(Location a) {
		if (segura == null) {
			segura = new ZonaSegura(this);
		}
		segura.createSign(a);
		return true;
	}

	public void setEnable(boolean e) {
		if (e && !enable) {
			enable = e;

			iniControl();
			estado = Estado.SinIniciar;
			jugadores = new Hashtable<String, IJugador>();
			if (inicio) {
				iniciar();

			}

		} else if (!e && enable) {
			enable = e;

			control.Fin();
			estado = Estado.NoDisponible;
		}

	}

	public boolean isEnable() {
		return enable;
	}

	public long getTime() {
		return time;
	}

	public long getTimeFalta() {
		return time - (System.currentTimeMillis() - init);
	}

	public void setTime(long tim) {
		time = tim;
	}

	@Override
	public void forzarFin() {
		this.setEstado(Estado.Finalizada);
	}

	@Override
	public void cargaConf(ConfigurationSection section) {
		ConfigGames.loadPartida(section, this);
		ConfigGames.savePartida(section, this);
	}

}
