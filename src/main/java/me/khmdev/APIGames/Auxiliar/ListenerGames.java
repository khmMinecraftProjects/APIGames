package me.khmdev.APIGames.Auxiliar;

import me.khmdev.APIGames.Auxiliar.ConstantesGames.Equipo;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Estado;
import me.khmdev.APIGames.Games.IGame;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public abstract class ListenerGames implements Listener {
	protected IGame game;

	public ListenerGames(IGame plug) {
		game = plug;

	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {

		Jugador j = game.getJugador(event.getPlayer().getName());
		if (j == null || j.getPartida().getEstado() != Estado.EnCurso) {
			event.getRecipients().removeAll(game.getPlayers());
			return;
		} else {
			event.setCancelled(true);
			if (event.getMessage().startsWith("all")) {
				j.getPartida().sendAsToAll(j,
						event.getMessage().replaceFirst("all ", ""));
			} else {
				j.getPartida().sendAsToTeam(j, event.getMessage());
			}
		}
	}

	@EventHandler
	public void Asegurar(EntityDamageByEntityEvent event) {

		if (event.getEntity() instanceof Player) {
			Player pl = (Player) event.getEntity();
			IJugador j = game.getJugador(pl.getName());
			if (j == null) {
				return;
			}

			Entity killer = event.getDamager();
			if (killer instanceof Arrow) {
				Arrow a = (Arrow) killer;
				killer = (Entity) a.getShooter();
			}

			if (killer == null || !(killer instanceof Player)) {

				// event.setCancelled(true);
				return;

			}
			Player dam = (Player) killer;
			Jugador damJ = j.getPartida().getJugador(dam.getName());

			if (damJ == null
					|| (damJ.getEquipo() != null
							&& !damJ.getEquipo().equals(Equipo.Ninguno) && damJ
							.getEquipo().equals(j.getEquipo()))) {
				event.setCancelled(true);
				return;
			}

		}
	}

	@EventHandler
	public void logOut(PlayerQuitEvent event) {
		if (game.getPartidas() == null) {
			return;
		}

		Jugador j;
		if ((j = game.getJugador(event.getPlayer().getName())) != null) {
			j.getPartida().JugadorAbandona(j);
			j.getPartida().pierde(j);
		}
	}

	protected abstract void spawn(Jugador j, PlayerRespawnEvent event);

	protected abstract void death(Jugador j, EntityDeathEvent event);

	@EventHandler(priority = EventPriority.NORMAL)
	public void ReSpawn(PlayerRespawnEvent event) {

		Jugador j = (Jugador) game.getJugador(event.getPlayer().getName());
		if (j == null) {
			return;
		}
		spawn(j, event);
		if (game.getGestorVentaja() != null) {
			game.getGestorVentaja().respawn(j, event);
		}
	}

	@EventHandler
	public void onDeath(EntityDeathEvent event) {

		if (event.getEntity() instanceof Player) {
			Player pl = (Player) event.getEntity();

			if (!game.containsJugador(pl.getName())) {
				return;
			}
			Jugador j = (Jugador) game.getJugador(pl.getName());
			if (j == null) {
				return;
			}
			j.death();
			if (pl.getKiller() != null) {
				Jugador killer = j.getPartida().getJugador(
						pl.getKiller().getName());
				if (killer != null) {
					killer.kill();
					killer.getPartida().kill(killer, j);
				}
			}
			death(j, event);
			if (game.getGestorVentaja() != null) {
				game.getGestorVentaja().death(j, event);
			}

			pl.getInventory().clear();
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		if (game.containsJugador(e.getPlayer().getName())) {
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void death(PlayerDeathEvent e) {
		e.getDrops().clear();
	}
}
