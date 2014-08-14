package me.khmdev.APIGames.Auxiliar;

import me.khmdev.APIGames.Auxiliar.ConstantesGames.Equipo;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public class Variables {

	public static String ChatColorStandar = ChatColor.translateAlternateColorCodes('&',
			"&6");
	public static EquipoVar A = 
			new EquipoVar("Azul","&1", DyeColor.BLUE,Equipo.A),
			B = new EquipoVar("Rojo","&C", DyeColor.RED,Equipo.B), 
			C = new EquipoVar("Verde","&2",DyeColor.GREEN,Equipo.C), 
			D = new EquipoVar("Amarillo","&E", DyeColor.YELLOW,Equipo.D);

	public static EquipoVar get(Equipo e){
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
