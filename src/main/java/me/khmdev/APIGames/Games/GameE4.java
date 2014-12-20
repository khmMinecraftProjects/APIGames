package me.khmdev.APIGames.Games;

import me.khmdev.APIBase.API;
import me.khmdev.APIBase.Almacenes.local.LocAlmacen;
import me.khmdev.APIGames.Auxiliar.Respawn;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Equipo;
import me.khmdev.APIGames.Books.BookE4;
import me.khmdev.APIGames.Books.IBook;
import me.khmdev.APIGames.Partidas.PartidaEquipos4;


import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class GameE4 extends Game {

	public GameE4(API ap, String nam, String string) {
		super(ap, nam, string);
		max = 4;

	}

	public String help() {
		String s = "";
		s += super.help();
		s += ("          " + rA + "/" + rB + "/" + "RC/RD" + "  (Radio)" + "\n");
		return s;

	}
	public IBook initBook(){
		return new BookE4(this);
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (args.length >= 3) {

			PartidaEquipos4 partida = (PartidaEquipos4) NPartida(args[1]);
			if (partidas.containsKey(args[1])) {
				partida = (PartidaEquipos4) getPartida(args[1]);
			}
			if (partida == null) {
				partida = (PartidaEquipos4) NPartida(args[1]);
				partidas.put(args[1], partida);
			}

			if (args[2].equals(rA)) {
				if (sender.getName() == "CONSOLE") {
					return true;
				}
				Player pl = sender instanceof Player?(Player)sender:null;
				Location a = LocAlmacen.cargar(pl, "LocA");

				if (a == null || args.length < 4) {
					sender.sendMessage("No se ha introducido valores");

					return true;
				}

				int r = Integer.valueOf(args[3]);
				partida.setRA(new Respawn(Equipo.A, a, r));
				sender.sendMessage("Guardado Respawn del equipo A");
				partidas.put(args[1], partida);

				return true;
			}

			if (args[2].equals(rB)) {
				if (sender.getName() == "CONSOLE") {
					return true;
				}
				Player pl = sender instanceof Player?(Player)sender:null;
				Location a = LocAlmacen.cargar(pl, "LocA");

				if (a == null || args.length < 4) {
					sender.sendMessage("No se ha introducido valores");

					return true;
				}

				int r = Integer.valueOf(args[3]);
				partida.setRB(new Respawn(Equipo.B, a, r));
				sender.sendMessage("Guardada Respawn del equipo B");
				partidas.put(args[1], partida);

				return true;
			}
			
			if (args[2].equals("RC")) {
				if (sender.getName() == "CONSOLE") {
					return true;
				}
				Player pl = sender instanceof Player?(Player)sender:null;
				Location a = LocAlmacen.cargar(pl, "LocA");

				if (a == null || args.length < 4) {
					sender.sendMessage("No se ha introducido valores");

					return true;
				}

				int r = Integer.valueOf(args[3]);
				partida.setRC(new Respawn(Equipo.C, a, r));
				sender.sendMessage("Guardado Respawn del equipo C");
				partidas.put(args[1], partida);

				return true;
			}

			if (args[2].equals("RD")) {
				if (sender.getName() == "CONSOLE") {
					return true;
				}
				Player pl = sender instanceof Player?(Player)sender:null;
				Location a = LocAlmacen.cargar(pl, "LocA");

				if (a == null || args.length < 4) {
					sender.sendMessage("No se ha introducido valores");

					return true;
				}

				int r = Integer.valueOf(args[3]);
				partida.setRD(new Respawn(Equipo.D, a, r));
				sender.sendMessage("Guardada Respawn del equipo D");
				partidas.put(args[1], partida);

				return true;
			}
		}
		return super.onCommand(sender, cmd, label, args);
	}

}
