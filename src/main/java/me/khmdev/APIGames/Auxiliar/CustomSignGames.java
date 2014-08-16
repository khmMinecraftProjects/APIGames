package me.khmdev.APIGames.Auxiliar;

import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.MetadataValue;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomSign;
import me.khmdev.APIBase.API;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Estado;
import me.khmdev.APIGames.Games.IGame;
import me.khmdev.APIGames.Partidas.IPartida;
import me.khmdev.APIGames.Partidas.Partida;

public class CustomSignGames extends CustomSign {
	IGame game;

	public CustomSignGames(IGame g, Sign sgn) {
		game = g;
		sign = sgn;
	}

	@Override
	public void execute(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			if (event.getPlayer().hasPermission("apig.command")) {
				return;
			}
			event.setCancelled(true);
			return;
		}

		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		Block b = event.getClickedBlock();

		Sign s = (Sign) b.getState();
		MetadataValue partidaMeta = API.getMetadata(s.getBlock(), "Partida");
		if (partidaMeta == null) {
			return;
		}
		String ps = partidaMeta.asString();

		IPartida p = game.getPartida(ps);
		if (p != null) {
			event.setCancelled(true);
			if (p.isIn(s)) {
				if (p.getEstado() == Estado.EsperandoJugadores) {
					MetadataValue act = null;
					if ((act = API.getMetadata(event.getPlayer(), "Jugando")) != null) {
						Partida par = (Partida) game.getPartida(act.asString());
						if (par != null && par.JugadorEsta(p.getName())) {
							event.getPlayer().sendMessage(
									"Ya esta en la partida " + act.asString());
							
							return;
						}else{
							API.removeMetadata(event.getPlayer(), "Jugando");
						}
					}

					if (!p.JugadorEsta(event.getPlayer().getName())) {
						event.getPlayer().sendMessage(
								"Entrando a partida " + p.getName());
						p.nuevoJugador(event.getPlayer());
						event.getPlayer().playEffect(event.getPlayer().getEyeLocation(), Effect.MOBSPAWNER_FLAMES, 30);
						event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.LEVEL_UP, 10, 10);
					} else {
						event.getPlayer().sendMessage(
								"Ya esta en la partida " + p.getName());

					}
				} else {

					event.getPlayer().sendMessage(
							"No se ha podido entrar a la partida "
									+ p.getName());
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ANVIL_LAND, 1, 1);

				}
			} /*else {
				if (p.getEstado() == Estado.EsperandoJugadores) {
					Jugador j = p.getJugador(event.getPlayer().getName());
					if (j != null) {
						if(!j.getPlayer().hasPermission("abandonar.command")){
							j.getPlayer().sendMessage(ChatColor.RED+
									"No tienes permisos suficientes");
							return;
						}
						String ss=j.getPlayer().getName();
						if(ResetAbandonar.canLeave(ss)){
							p.JugadorAbandona(j);
							j.abandona();
							event.getPlayer().sendMessage(
								"Saliendo de la partida " + p.getName());
							ResetAbandonar.leave(ss);
							
						} else {

							event.getPlayer().sendMessage(
									"No puedes abandonar hasta dentro de "
											+ ResetAbandonar.timeString(ss));
						}
					} else {
						event.getPlayer().sendMessage(
								"No esta en la partida " + p.getName());

					}
				} else {
					event.getPlayer().sendMessage(
							"No se ha podido salir de la la partida "
									+ p.getName());

				}

			}*/
		}
	}

	@Override
	public boolean isthis(Sign s) {
		MetadataValue gameMeta = API.getMetadata(s.getBlock(), "Game");

		if (gameMeta != null) {
			String g = gameMeta.asString();
			if (game.getName().equalsIgnoreCase(g)) {
				return true;
			}
		}
		return false;
	}

}
