package me.khmdev.APIGames.ListenAPIG.jugador;

import me.khmdev.APIGames.ListenAPIG.partida.PartidaEvent;
import me.khmdev.APIGames.Partidas.IPartida;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PreJugadorEntraEvent extends PartidaEvent implements Cancellable {
	private Player player;
	public PreJugadorEntraEvent(Player pl,IPartida p) {
		super(p);
		player=pl;
	}

	private boolean cancelled = false;

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public Player getPlayer() {
		return player;
	}
}
