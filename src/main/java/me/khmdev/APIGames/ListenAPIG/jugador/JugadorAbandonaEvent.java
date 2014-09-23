package me.khmdev.APIGames.ListenAPIG.jugador;

import me.khmdev.APIGames.Auxiliar.IJugador;

import org.bukkit.event.Cancellable;

public class JugadorAbandonaEvent extends JugadorSaleEvent implements Cancellable {
	public JugadorAbandonaEvent(IJugador ju) {
		super(ju);
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
