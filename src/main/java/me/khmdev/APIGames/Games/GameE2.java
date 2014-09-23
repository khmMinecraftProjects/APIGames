package me.khmdev.APIGames.Games;

import me.khmdev.APIBase.API;
import me.khmdev.APIBase.Almacenes.local.LocAlmacen;
import me.khmdev.APIGames.Auxiliar.Respawn;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Equipo;
import me.khmdev.APIGames.Books.BookE2;
import me.khmdev.APIGames.Books.IBook;
import me.khmdev.APIGames.Partidas.PartidaEquipos2;


import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class GameE2 extends Game {

	public GameE2(API ap, String nam, String string) {
		super(ap, nam, string);
	}

	public String help() {
		String s = "";
		s += super.help();
		s += ("          " + rA + "/" + rB + "  (Radio)" + "\n");
		return s;

	}
	public IBook initBook(){
		return new BookE2(this);
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (args.length >= 3) {

			PartidaEquipos2 partida = (PartidaEquipos2) NPartida(args[1]);
			if (partidas.containsKey(args[1])) {
				partida = (PartidaEquipos2) getPartida(args[1]);
			}
			if (partida == null) {
				partida = (PartidaEquipos2) NPartida(args[1]);
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
		}
		return super.onCommand(sender, cmd, label, args);
	}

}
