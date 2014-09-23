package me.khmdev.APIGames.ListenAPIG.jugador;

import me.khmdev.APIGames.Auxiliar.IJugador;
import me.khmdev.APIGames.Games.IGame;
import me.khmdev.APIGames.ListenAPIG.EventAPIG;
import me.khmdev.APIGames.Partidas.IPartida;

public abstract class JugadorEvent extends EventAPIG {
	private IJugador j;
	private IPartida p;
	private IGame g;

	public JugadorEvent(IJugador ju) {
		j=ju;
		p=ju.getPartida();
		g=ju.getPartida().getGame();
	}

	public IJugador getJugador() {
		return j;
	}

	public IPartida getPartida() {
		return p;
	}

	public IGame getGame() {
		return g;
	}

	

}
