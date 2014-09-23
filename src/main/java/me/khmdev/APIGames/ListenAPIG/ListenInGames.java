package me.khmdev.APIGames.ListenAPIG;


import me.khmdev.APIBase.Auxiliar.UsuariosOcupados;
import me.khmdev.APIGames.ListenAPIG.jugador.JugadorEntraEvent;
import me.khmdev.APIGames.ListenAPIG.jugador.JugadorSaleEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ListenInGames implements Listener {
	@EventHandler
	public void jugadorEntra(JugadorEntraEvent e) {
		UsuariosOcupados.addPlayer(e.getJugador().getPlayer());
	}
	@EventHandler
	public void jugadorSale(JugadorSaleEvent e) {
		UsuariosOcupados.removePlayer(e.getJugador().getPlayer());
	}
}
