package me.khmdev.APIGames.ListenAPIG.partida;
import me.khmdev.APIGames.Partidas.IPartida;

import org.bukkit.event.Cancellable;

public class PartidaFinalizaEvent extends PartidaEvent implements Cancellable {


	public PartidaFinalizaEvent(IPartida par) {
		super(par);
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
}
