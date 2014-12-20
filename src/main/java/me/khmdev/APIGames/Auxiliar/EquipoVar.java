package me.khmdev.APIGames.Auxiliar;

import me.khmdev.APIGames.Auxiliar.ConstantesGames.Equipo;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public class EquipoVar {
	public final String name;
	public final ChatColor chat;
	public final DyeColor dye;
	public final Equipo equipo;

	public EquipoVar(String nam,ChatColor cht, DyeColor color,Equipo equip){
		dye=color;
		chat=cht;
		name=nam;
		equipo=equip;
	}

}
