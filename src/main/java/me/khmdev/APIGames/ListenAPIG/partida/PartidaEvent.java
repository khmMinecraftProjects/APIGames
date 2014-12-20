package me.khmdev.APIGames.ListenAPIG.partida;

import me.khmdev.APIGames.Games.IGame;
import me.khmdev.APIGames.ListenAPIG.EventAPIG;
import me.khmdev.APIGames.Partidas.IPartida;

public abstract class PartidaEvent extends EventAPIG {
	private IPartida p;
	private IGame g;

	public PartidaEvent(IPartida par) {
		p=par;
		g=par.getGame();
	}

	public IPartida getPartida() {
		return p;
	}

	public IGame getGame() {
		return g;
	}

	

}
