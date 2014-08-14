package me.khmdev.APIGames.Partidas;

import java.util.Enumeration;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import me.khmdev.APIBase.API;
import me.khmdev.APIBase.Almacenes.Almacen;
import me.khmdev.APIBase.Auxiliar.Auxiliar;
import me.khmdev.APIGames.Auxiliar.EquipoVar;
import me.khmdev.APIGames.Auxiliar.IJugador;
import me.khmdev.APIGames.Auxiliar.Jugador;
import me.khmdev.APIGames.Auxiliar.Respawn;
import me.khmdev.APIGames.Auxiliar.Variables;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Equipo;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Estado;
import me.khmdev.APIGames.Games.IGame;
import me.khmdev.APIGames.Scores.BoardE2;
import me.khmdev.APIGames.Scores.BoardGames;
import me.khmdev.APIGames.lang.Lang;
import me.khmdev.APIMaps.APIM;
import me.khmdev.APIMaps.Auxiliar.Zona;

public abstract class PartidaEquipos2 extends Partida {
	protected Respawn rA, rB;
	
	public PartidaEquipos2(String s, IGame game2, API ap) {
		super(s, game2, ap);
		pA = 0;
		pB = 0;
		eq = 0;
	}

	protected void asegurar() {
		super.asegurar();

		Zona zA=new Zona(
				rA.getPosicion().clone().add(-rA.getRadio(), -rA.getRadio(), -rA.getRadio()),
				rA.getPosicion().clone().add(rA.getRadio(), rA.getRadio(), rA.getRadio()));
		zA.setPerms((byte) 121);
		
		APIM.getInstance().AsegurarZona("zA_"+game.getName()+"_"+name, zA);

		Zona zB=new Zona(
				rB.getPosicion().clone().add(-rB.getRadio(), -rB.getRadio(), -rB.getRadio()),
				rB.getPosicion().clone().add(rB.getRadio(), rB.getRadio(), rB.getRadio()));
		zB.setPerms((byte) 121);
		
		APIM.getInstance().AsegurarZona("zB_"+game.getName()+"_"+name, zB);
	}

	public void setEquipo(IJugador j, Equipo e) {
		switch (j.getEquipo()) {
		case B:
			eq--;
			break;
		case A:
			eq++;
			break;
		default:
			break;
		}
		switch (e) {
		case B:
			j.setEquipo(e);
			eq++;
			break;
		case A:
			j.setEquipo(e);
			eq--;
			break;
		default:
			break;
		}
	}

	public boolean iniciar() {
		if (!super.iniciar()) {
			return false;
		}
		pA = 0;
		pB = 0;
		eq = 0;
		return true;
	}

	public void finalizar() {
		super.finalizar();
		pA = 0;
		pB = 0;
		eq = 0;
	}

	protected void setPuntuaciones(Equipo eq) {
		Enumeration<IJugador> j = jugadores.elements();
		while (j.hasMoreElements()) {
			Jugador jj = (Jugador) j.nextElement();
			if (jj.getEquipo().equals(eq)) {
				jj.setGanador(1);
			} else {
				jj.setGanador(0);
			}
		}
	}

	public void JugadorAbandona(Jugador j) {
		if (j.getEquipo() == Equipo.A) {
			eq++;
		} else {
			eq--;
		}
		if (estado == Estado.EnCurso && Math.abs(eq) >= jugadores.size()) {
			forzarFin();
		}
		super.JugadorAbandona(j);
	}

	protected int pA, pB, eq, pMax = 5;

	public void compruebaPuntuacion() {
		if (pMax != 0 && (pA >= pMax || pB >= pMax)) {
			ganador();
			setEstado(Estado.Finalizada);
		}
	}

	public void ganador() {
		EquipoVar winner;
		if (pMax != 0 && pA < pMax && pB < pMax) {
			return;
		}
		if (pA > pB) {
			setPuntuaciones(Equipo.A);

			winner=Variables.A;
		} else {
			setPuntuaciones(Equipo.B);
			winner=Variables.B;

		}

		sendAll(Lang.get("fin_winner").replace("%Winner%", winner.name));
	}

	public boolean Funcional() {
		return (mapa != null && rA != null && rB != null);
	}

	public void comprobaciones() {
		compruebaPuntuacion();
		;
	}

	public int getpA() {
		return pA;
	}

	public String Datos() {
		String s = "";
		s += ok(rA) + "Respawn equipo A: " + dat(rA) + "\n";
		s += ok(rB) + "Respawn equipo B: " + dat(rB) + "\n";
		s += super.Datos();
		return s;
	}

	public int getpB() {
		return pB;
	}

	public int getpMax() {
		return pMax;
	}

	public void setEquipo(Jugador j) {
		if (eq >= 0) {
			j.setEquipo(Equipo.A);
			eq--;
		} else {
			j.setEquipo(Equipo.B);
			eq++;
		}

	}

	public void forzarFin() {
		EquipoVar winner=null;
		if (pA > pB) {
			setPuntuaciones(Equipo.A);
			winner=Variables.A;
		} else if (pA < pB) {
			setPuntuaciones(Equipo.B);
			winner=Variables.B;
		} else {
			sendAll(Lang.get("fin_empate"));
			this.setEstado(Estado.Finalizada);
			return;
		}
		this.setEstado(Estado.Finalizada);
		sendAll(Lang.get("fin_winner").replace("%Winner%", winner.name));

	}

	@Override
	public BoardGames getScore() {
		return new BoardE2(this);
	}

	public void cargaConf(ConfigurationSection section) {
		super.cargaConf(section);
		int max = Auxiliar.getNatural(section.getString("Puntuacion_Maxima"),
				-1);
		if (max != -1) {
			pMax = max;
		}
		section.set("Puntuacion_Maxima", pMax);
	}

	@Override
	public void guardar(Almacen nbt) {

		super.guardar(nbt);

		if (rA != null) {
			int[] vecBanderaA = { (int) rA.getPosicion().getX(),
					(int) rA.getPosicion().getY(),
					(int) rA.getPosicion().getZ(), (int) rA.getRadio() };
			nbt.setString("wA", rA.getPosicion().getWorld().getName());
			nbt.setIntArray("bA", vecBanderaA);
		}

		if (rB != null) {
			int[] vecBanderaB = { (int) rB.getPosicion().getX(),
					(int) rB.getPosicion().getY(),
					(int) rB.getPosicion().getZ(), (int) rB.getRadio() };

			nbt.setString("wB", rB.getPosicion().getWorld().getName());

			nbt.setIntArray("bB", vecBanderaB);

		}
	}

	public void cargar(Almacen nbt) {
		super.cargar(nbt);

		String w = nbt.getString("wA");
		if (w.length() != 0) {
			int[] vec = nbt.getIntArray("bA");
			setRA(new Respawn(Equipo.A, new Location(Bukkit.getServer()
					.getWorld(w), vec[0], vec[1], vec[2]), vec[3]));
		}
		w = nbt.getString("wB");
		if (w.length() != 0) {
			int[] vec = nbt.getIntArray("bB");
			setRB(new Respawn(Equipo.B, new Location(Bukkit.getServer()
					.getWorld(w), vec[0], vec[1], vec[2]), vec[3]));
		}
	}

	public boolean SpawnCompleto() {
		return getRB().Completo()
				&& getRB().Completo()
				&& ((segura == null) || (segura.getMap() != null && segura
						.Completo()));
	}

	public boolean SpawnFuncional() {
		return !(getRB().rVacio() && getRA().rVacio() && ((segura == null) || (segura
				.getMap() != null && segura.rVacio())));
	}

	public void creaSpawners() {
		long ini = System.currentTimeMillis();
		while (!(getRA().Completo())
				&& (ini + timeO) - System.currentTimeMillis() > 0) {

			getRA().getFreeZone();

		}

		ini = System.currentTimeMillis();
		while (!(getRB().Completo())
				&& (ini + timeO) - System.currentTimeMillis() > 0) {
			getRB().getFreeZone();

		}
		if (segura != null && segura.getMap() != null) {
			ini = System.currentTimeMillis();
			boolean b = false;
			while (!(segura.Completo()) && !b
					&& (ini + timeO) - System.currentTimeMillis() > 0) {
				b = segura.getFreeZone();

			}
		}
	}

	public void setRA(Respawn r) {
		rA = r;
		setEstado(Estado.SinIniciar);
	}

	public Respawn getRA() {
		return rA;
	}

	public void setRB(Respawn r) {
		rB = r;
		setEstado(Estado.SinIniciar);
	}

	public Respawn getRB() {
		return rB;
	}

	public Location spawnZone(Jugador j) {
		if (j.getEquipo() == Equipo.A) {
			return rA.SpawnZone().clone().add(0.5, 0, 0.5);
		} else {
			return rB.SpawnZone().clone().add(0.5, 0, 0.5);
		}
	}

}
