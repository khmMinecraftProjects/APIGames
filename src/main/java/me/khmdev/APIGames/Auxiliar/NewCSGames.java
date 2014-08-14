package me.khmdev.APIGames.Auxiliar;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomSign;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.NewsCustomSign;
import me.khmdev.APIBase.API;
import me.khmdev.APIBase.Auxiliar.Auxiliar;
import me.khmdev.APIGames.Games.IGame;
import me.khmdev.APIGames.Partidas.IPartida;

public class NewCSGames extends NewsCustomSign {
	IGame game;

	public NewCSGames(IGame g) {
		game = g;
	}

	public CustomSign getSign(Sign sign) {
		return new CustomSignGames(game, sign);

	}

	@Override
	public boolean isthis(SignChangeEvent event) {
		String s = event.getLine(0);

		if (Auxiliar.getSeparate(s, 0, ' ').equals(game.getName())) {
			String Spartida = Auxiliar.getSeparate(s, 1, ' ');
			IPartida partida = null;
			partida = game.getPartida(Spartida);

			if (partida == null) {
				event.setLine(1, "No existe partida");
				return false;

			} else {
				if (partida.getSign() == null) {
					partida.setSign((Sign) event.getBlock().getState());
				} else if (!partida.getSign().equals(
						(Sign) event.getBlock().getState())) {
					partida.getSign().getBlock().setType(Material.AIR);
					partida.setSign((Sign) event.getBlock().getState());
				}
				Block b=partida.getSign().getBlock();
				API.setMetadata(b, "Game",game.getName());
				API.setMetadata(b, "Partida",partida.getName());

				partida.actualizarSign(event);
				return true;

			}
		}
		return false;
	}

}
