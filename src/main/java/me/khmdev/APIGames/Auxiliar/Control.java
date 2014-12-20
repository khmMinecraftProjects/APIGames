package me.khmdev.APIGames.Auxiliar;

import java.util.Enumeration;

import me.khmdev.APIAuxiliar.Auxiliar.Timer;
import me.khmdev.APIAuxiliar.Effects.BarRun;
import me.khmdev.APIAuxiliar.Effects.BossBar;
import me.khmdev.APIAuxiliar.Effects.IFuncion;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Estado;
import me.khmdev.APIGames.Games.IGame;
import me.khmdev.APIGames.Partidas.Partida;
import me.khmdev.APIGames.lang.Lang;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Control extends BukkitRunnable implements IControl {
	protected Partida partida;
	protected World w;
	protected int jugadores;
	protected IGame game;

	public Control(Partida p, IGame plu) {
		partida = p;
		game = plu;
	}

	public void Regenerando() {
		if (!partida.getMapa().isReparando()) {
			partida.setEstado(Estado.Iniciando);
			game.addPartida(partida);
		}
	}

	public void Iniciando() {

		partida.creaSpawners();

		if (partida.SpawnCompleto()) {
			if (partida.SpawnFuncional()) {

				partida.setEstado(Estado.EsperandoJugadores);
				time.start();
			} else {
				partida.setEstado(Estado.NoDisponible);
			}
		}
	}

	protected long timeout = 30000,tineOne=15000;
	protected long lastTime = 0;

	protected Timer time = new Timer(timeout);

	public void EsperandoJ() {
		if (jugadores != partida.getNJug()) {
			jugadores = partida.getNJug();

			time.start();

			if (jugadores == partida.getMax()) {
				time.changeTimeOut(tineOne);
			}
		} else if (jugadores >= partida.getMax() / 2 
				&& jugadores!=0) {
			isStart();
		}
	}

	private void isStart() {
		if (!time.isEnd()) {
			long tim = time.getLeftSeconds();
			if (time.getLeftSeconds() % 10 == 0
					|| tim<10) {
				if (tim != lastTime) {
					partida.sendAll(Lang.get("time_start").replace("%Time%",
							"" + tim

					));
					lastTime = tim;
				}
			}
		} else {
			start();
		}
	}

	private void start() {
		partida.sendAll(Lang.get("start_up"));
		partida.setEstado(Estado.EnCurso);

		partida.iniciada();

		timer();

	}

	private BarRun run = null;
	
	private void timer() {
		if (partida.getTime() <= 0) {
			Player[] pl = toArray();
			for (Player player : pl) {
				BossBar.removeBarPlayer(player);
			}
			return;
		}
		Player[] pl = toArray();
		run = BossBar.sendCargaThese(pl, Lang.get("bossBar_left"),
				Lang.get("bossBar_end"), partida.getTime(), false,
				new IFuncion() {

					@Override
					public void exec() {
						partida.forzarFin();

					}
				});
	}

	private Player[] toArray() {
		Enumeration<IJugador> it = partida.getJugadores();
		Player[] pl = new Player[partida.getNJug()];
		int i = 0;
		while (it.hasMoreElements()) {
			pl[i] = it.nextElement().getPlayer();
			i++;
		}
		return pl;
	}

	public void EnCurso() {
		partida.comprobaciones();
	}

	public void Fin() {
		if (run != null) {
			run.finalizar();
		}
		partida.Gofinalizar();
		Bukkit.getScheduler().cancelTask(id);

	}

	@Override
	public void run() {

		if (partida.getEstado() == Estado.Regenerando) {
			Regenerando();
		} else if (partida.getEstado() == Estado.Iniciando) {
			Iniciando();
		} else if (partida.getEstado() == Estado.EsperandoJugadores) {
			EsperandoJ();
		} else if (partida.getEstado() == Estado.EnCurso) {
			EnCurso();
		} else if (partida.getEstado() == Estado.Finalizada) {
			Fin();
		}

	}

	int id;

	public void setId(int i) {
		id = i;
	}

	@Override
	public void ForceFin() {
		if (run != null) {
			run.finalizar();
		}
		Bukkit.getScheduler().cancelTask(id);

	}
}
