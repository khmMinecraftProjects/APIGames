package me.khmdev.APIGames.Auxiliar;

import me.khmdev.APIGames.Auxiliar.ConstantesGames.Equipo;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public class EquipoVar {
	public final String chat;
	public final DyeColor dye;
	public final Equipo equipo;


	public final String name;
	
	public EquipoVar(String nam,String cht, DyeColor color,Equipo equip){
		dye=color;
		chat=ChatColor.translateAlternateColorCodes('&',cht);
		name=nam;
		equipo=equip;
	}

}
