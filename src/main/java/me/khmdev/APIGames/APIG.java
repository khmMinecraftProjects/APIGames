package me.khmdev.APIGames;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.khmdev.APIBase.API;
import me.khmdev.APIBase.Almacenes.Almacen;
import me.khmdev.APIBase.Almacenes.Central;
import me.khmdev.APIBase.Almacenes.Datos;
import me.khmdev.APIBase.Almacenes.local.ConfigFile;
import me.khmdev.APIBase.Almacenes.sql.AlmacenSQL;
import me.khmdev.APIGames.Auxiliar.ConfigGames;
import me.khmdev.APIGames.Auxiliar.Jugador;
import me.khmdev.APIGames.Auxiliar.ResetAbandonar;
import me.khmdev.APIGames.Auxiliar.Variables;
import me.khmdev.APIGames.Books.SelectorGame;
import me.khmdev.APIGames.Games.Game;
import me.khmdev.APIGames.ListenAPIG.ListenInGames;
import me.khmdev.APIGames.MarcadoresSQL.MarcadoresSQL;
import me.khmdev.APIGames.MarcadoresSQL.SQLConstant;

public class APIG implements Datos {

	private JavaPlugin api;
	private HashMap<String, Game> games;
	private static Almacen cargador;
	private ConfigFile conf, confGeneral;
	private static APIG instance;
	private static Central central;

	public APIG(JavaPlugin p) {
		Bukkit.getServer().getPluginManager()
		.registerEvents(new ListenInGames(), p);
		api = p;
		games = new HashMap<String, Game>();
		AlmacenSQL server=API.getInstance().getSql();
		if(server.isEnable()){
			for (String s : SQLConstant.sql) {
				server.sendUpdate(s);
			}
		}
		conf = new ConfigFile(api.getDataFolder(), "Games");
		confGeneral = new ConfigFile(api.getDataFolder(), "config");
		Variables.cargarConfig(confGeneral);
		SelectorGame.init();

		if (API.getInstance().getSql().isEnable()) {
			MarcadoresSQL run = new MarcadoresSQL();
			int idd = Bukkit.getScheduler().scheduleSyncRepeatingTask(api, run,
					100, 100L);
			run.setId(idd);
		}
		instance = this;
		central = new Central(p);
		central.insertar(this);

	}

	public void onDisable() {
		central.guardar();
	}

	public static APIG getInstance() {
		return instance;
	}

	public void newGame(Game g) {
		String name = g.getName();
		api.getLogger().info(name + " cargado");

		games.put(g.getName(), g);

		ConfigurationSection gam = null;
		cargador.leer(g, g.getName());
		if (getConf().getConfig().isConfigurationSection(g.getName())) {
			gam = getConf().getConfig().getConfigurationSection(g.getName());

		} else {
			gam = getConf().getConfig().createSection(g.getName());
		}
		g.cargaConf(gam);

		conf.saveConfig();
	}

	@Override
	public void guardar(Almacen nbt) {

		Iterator<Entry<String, Game>> it = games.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Game> next = it.next();

			nbt.escribir(next.getValue(), next.getKey());
			// ConfigGames.saveGame(next.getValue(), conf.getConfig());
		}
		conf.reload();
		// conf.saveConfig();

	}

	@Override
	public void cargar(Almacen nbt) {
		cargador = nbt;
		Iterator<Entry<String, Game>> it = games.entrySet().iterator();

		while (it.hasNext()) {
			Entry<String, Game> next = it.next();
			nbt.leer(next.getValue(), next.getKey());
			ConfigurationSection gam = null;
			if (getConf().getConfig().isConfigurationSection(
					next.getValue().getName())) {
				gam = getConf().getConfig().getConfigurationSection(
						next.getValue().getName());

			} else {
				gam = getConf().getConfig().createSection(
						next.getValue().getName());
			}
			next.getValue().cargaConf(gam);
		}
		conf.saveConfig();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (cmd.getName().equalsIgnoreCase("Ventajas")) {
			if (sender.getName().equalsIgnoreCase("CONSOLE")) {
				return true;
			}
			Player pl = sender instanceof Player?(Player)sender:null;
			pl.getInventory().addItem(SelectorGame.getItemV());
			sender.sendMessage("Item enviado");

			return true;
		}

		if (cmd.getName().equalsIgnoreCase("abandonar")) {
			if (sender.getName().equalsIgnoreCase("CONSOLE")) {
				return true;
			}

			Player pl = sender instanceof Player?(Player)sender:null;

			Jugador j = getJugador(pl);
			if (j != null) {
				if (ResetAbandonar.canLeave(pl.getName())) {
					if(!pl.hasPermission("abandonarOP.command")){
					ResetAbandonar.leave(pl.getName());
					}
					j.abandona();
					j.getPartida().JugadorGoRendirse(j);
					sender.sendMessage("Has abandonado la partida");

				} else {

					pl.sendMessage("No puedes abandonar hasta dentro de "
							+ ResetAbandonar.timeString(pl.getName()));
				}
			} else {
				sender.sendMessage("No estas en ninguna partida");
			}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("kits")) {
			if (sender.getName().equalsIgnoreCase("CONSOLE")) {
				return true;
			}
			Player pl = sender instanceof Player?(Player)sender:null;
			pl.getInventory().addItem(SelectorGame.getItemK());
			sender.sendMessage("Item enviado");

			return true;
		}
		if (!cmd.getName().equalsIgnoreCase("apig")) {
			return false;
		}

		if (args.length < 1) {
			sender.sendMessage(help());

			return true;
		}

		Iterator<Entry<String, Game>> it = games.entrySet().iterator();
		boolean b = false;
		while (!b && it.hasNext()) {

			Game gam = it.next().getValue();

			if (args[0].equalsIgnoreCase(gam.getName())) {
				if (args.length > 1 && args[1].equals("on")) {
					sender.sendMessage(gam.getName() + " activado");
					gam.setEnable(true);
					ConfigGames.saveEnableGame(gam, getConf().getConfig());
					getConf().saveConfig();

					return true;
				}
				if (args.length > 1 && args[1].equals("off")) {
					sender.sendMessage(gam.getName() + " desactivado");
					gam.setEnable(false);
					ConfigGames.saveEnableGame(gam, getConf().getConfig());
					getConf().saveConfig();
					return true;
				}
				if (gam.isEnable()) {

					b = gam.onCommand(sender, cmd, label, args);
					return true;

				} else {
					sender.sendMessage(gam.getName() + " esta desactivado");
					return true;
				}
			}
		}
		if (!b) {
			sender.sendMessage("No existe juego");
			sender.sendMessage(help());
		}

		return true;
	}

	private String help() {
		String s = "";
		s += "/APIG  (Game)\n";
		Iterator<Entry<String, Game>> it = games.entrySet().iterator();
		while (it.hasNext()) {
			Game g = it.next().getValue();
			if (g.isEnable()) {
				s += "        " + g.getName() + "\n";
			}
		}
		return s;
	}

	public ConfigFile getConf() {
		return conf;
	}

	public Jugador getJugador(Player pl) {
		if (pl == null) {
			return null;
		}
		Iterator<Game> it = games.values().iterator();
		while (it.hasNext()) {
			Jugador j = it.next().getJugador(pl.getName());
			if (j != null) {
				return j;
			}
		}
		return null;
	}

}
