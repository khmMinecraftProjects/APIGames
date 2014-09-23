package me.khmdev.APIGames.Auxiliar;

import me.khmdev.APIBase.Almacenes.local.ConfigFile;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Equipo;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Variables {
	public static void cargarConfig(ConfigFile file) {
		FileConfiguration conf = file.getConfig();
		A=getData("A", conf, A);
		B=getData("B", conf, B);
		C=getData("C", conf, C);
		D=getData("D", conf, D);
		file.saveConfig();
	}
	private static EquipoVar getData(String s,ConfigurationSection conf,
			EquipoVar est){
		ConfigurationSection section=null;

		if (!conf.isConfigurationSection(s)) {
			section=conf.createSection(s);
		}else{
			section = conf.getConfigurationSection(s);
		}
		EquipoVar eqq=getEquipo(est,section);
		conf.set(s, section);
		return eqq;
	}

	private static EquipoVar getEquipo(EquipoVar e, ConfigurationSection s) {
		String n = e.name;
		ChatColor c = e.chat;
		DyeColor cl = e.dye;
		Equipo ep = e.equipo;
		if (s.isString("name")) {
			n = s.getString("name");
		}
		s.set("name", n);
		
		if (s.isInt("chat")) {
			int i = s.getInt("chat");
			if (ChatColor.values().length < i && i >= 0) {
				c = ChatColor.values()[i];
			}
		}
		
		s.set("chat", getID(c));

		if (s.isInt("dye")) {
			int i = s.getInt("dye");
			if (DyeColor.values().length < i && i >= 0) {
				cl = DyeColor.values()[i];
			}
		}
		s.set("dye", getID(cl));

		return new EquipoVar(n, c, cl, ep);
	}
	private static int getID(DyeColor cl){
		int i=0;
		if(cl==null){return 0;}
		for (DyeColor c : DyeColor.values()) {
			if(c==cl){
				return i;
			}i++;
		}
		return i;
	}
	private static int getID(ChatColor cl){
		int i=0;
		if(cl==null){return 0;}
		for (ChatColor c : ChatColor.values()) {
			if(c==cl){
				return i;
			}i++;
		}
		return i;
	}
	public static String ChatColorStandar = ChatColor
			.translateAlternateColorCodes('&', "&6");
	public static EquipoVar A = new EquipoVar("Azul", ChatColor.DARK_BLUE, DyeColor.BLUE,
			Equipo.A), B = new EquipoVar("Rojo", ChatColor.RED, DyeColor.RED, Equipo.B),
			C = new EquipoVar("Verde", ChatColor.DARK_GREEN, DyeColor.GREEN, Equipo.C),
			D = new EquipoVar("Amarillo", ChatColor.YELLOW, DyeColor.YELLOW, Equipo.D);

	public static EquipoVar get(Equipo e) {
		switch (e) {
		case A:
			return A;
		case B:
			return B;
		case C:
			return C;
		case D:
			return D;
		default:
			return null;
		}
	}
}
