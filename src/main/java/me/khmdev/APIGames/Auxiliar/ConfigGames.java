package me.khmdev.APIGames.Auxiliar;

import java.util.Iterator;
import java.util.Map.Entry;

import me.khmdev.APIBase.Auxiliar.Auxiliar;
import me.khmdev.APIGames.Games.IGame;
import me.khmdev.APIGames.Partidas.IPartida;

import org.bukkit.configuration.ConfigurationSection;

public class ConfigGames {

	public static void loadGame(IGame game, ConfigurationSection section) {
		game.setEnable(section.getBoolean("Default_enable", true));
		int max = Auxiliar.getNatural(section.getString("Jugadores_default"),
				-1);
		if (max != -1) {
			game.setMax(max);
		}
		long time = Auxiliar.getNatural(section.getString("Tiempo_default"),
				-1);
		if (time != -1) {
			game.setTime(time*1000);
		}
	}

	public static void saveEnableGame(IGame game, ConfigurationSection section) {
		ConfigurationSection gam;
		if (section.isConfigurationSection(game.getName())) {
			gam = section.getConfigurationSection(game.getName());

		} else {
			gam = section.createSection(game.getName());
		}
		gam.set("Default_enable", game.isEnable());

	}

	public static void saveGame(IGame game, ConfigurationSection section) {
		
		section.set("Default_enable", game.isEnable());
		if (!section.isSet("Jugadores_default")) {
			section.set("Jugadores_default", game.getMax());
		}
		if (!section.isSet("Tiempo_default")) {
			section.set("Tiempo_default", game.getTime()/1000);
		}
	}

	public static void loadPartidas(IGame game, ConfigurationSection section) {
		Iterator<Entry<String, IPartida>> it = game.getPartidas().entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<String, IPartida> next = it.next();
			if (section.isConfigurationSection(next.getKey())) {
				loadPartida(section.getConfigurationSection(next.getKey()),
						next.getValue());
			}
		}
	}

	public static void loadPartida(ConfigurationSection section,
			IPartida partida) {
		partida.setEnable(section.getBoolean("Enable", true));
		int max = Auxiliar.getNatural(section.getString("Jugadores"), -1);
		if (max != -1) {
			partida.setMax(max);
		}
		long time = Auxiliar.getNatural(section.getString("Tiempo"), -1);
		if (time != -1) {
			partida.setTime(time*1000);
		}
	}

	public static void savePartidas(IGame game, ConfigurationSection section) {
		Iterator<Entry<String, IPartida>> it = game.getPartidas().entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<String, IPartida> next = it.next();
			if (section.isConfigurationSection(next.getKey())) {
				savePartida(section.getConfigurationSection(next.getKey()),
						next.getValue());
			} else {
				savePartida(section.createSection(next.getKey()),
						next.getValue());
			}
		}
	}

	public static void savePartida(ConfigurationSection section,
			IPartida partida) {
		section.set("Enable", partida.isEnable());
		// section.set("Max", partida.getMax());

		if (!section.isInt("Jugadores")) {
			section.set("Jugadores", partida.getMax());
		}
		if (!section.isSet("Tiempo")) {
			section.set("Tiempo", partida.getTime()/1000);
		}
	}
}
