package me.khmdev.APIGames.Partidas;

import java.util.Enumeration;

import me.khmdev.APIBase.API;
import me.khmdev.APIBase.Almacenes.Almacen;
import me.khmdev.APIGames.Auxiliar.EquipoVar;
import me.khmdev.APIGames.Auxiliar.IJugador;
import me.khmdev.APIGames.Auxiliar.Jugador;
import me.khmdev.APIGames.Auxiliar.Respawn;
import me.khmdev.APIGames.Auxiliar.Variables;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Equipo;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Estado;
import me.khmdev.APIGames.Games.IGame;
import me.khmdev.APIGames.Scores.BoardE4;
import me.khmdev.APIGames.Scores.BoardGames;
import me.khmdev.APIGames.lang.Lang;
import me.khmdev.APIMaps.APIM;
import me.khmdev.APIMaps.Auxiliar.Zona;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public abstract class PartidaEquipos4 extends PartidaEquipos2 {
	protected int pC, pD;
	protected Respawn rC, rD;
	private int nA, nB, nC, nD;

	public PartidaEquipos4(String s, IGame game2, API ap) {
		super(s, game2, ap);
		pC = 0;
		pD = 0;
		nA=0;
		nB=0;
		nC=0;
		nD=0;
	}
	
	protected void asegurar() {
		super.asegurar();
		Zona zC=new Zona(
				rC.getPosicion().clone().add(-rC.getRadio(), -rC.getRadio(), -rC.getRadio()),
				rC.getPosicion().clone().add(rC.getRadio(), rC.getRadio(), rC.getRadio()));
		zC.setPerms((byte) 121);
		
		APIM.getInstance().AsegurarZona("zC_"+game.getName()+"_"+name, zC);
		
		Zona zD=new Zona(
				rD.getPosicion().clone().add(-rD.getRadio(), -rD.getRadio(), -rD.getRadio()),
				rD.getPosicion().clone().add(rD.getRadio(), rD.getRadio(), rD.getRadio()));
		zD.setPerms((byte) 121);
		
		APIM.getInstance().AsegurarZona("zD_"+game.getName()+"_"+name, zD);
	}
	
	public void setEquipo(IJugador j, Equipo e) {
		switch (j.getEquipo()) {
		case B:
			nA--;
			break;
		case A:
			nB--;
			break;
		case C:
			nC--;
			break;
		case D:
			nD--;
			break;
		default:
			break;
		}
		j.setEquipo(e);

		switch (e) {
		case B:
			nA++;
			break;
		case A:
			nB++;
			break;
		case C:
			nC++;
			break;
		case D:
			nD++; 
			break;
		default:
			break;
		}
	}
	
	public boolean iniciar() {
		if (!super.iniciar()) {
			return false;
		}
		pC = 0;
		pD = 0;
		nA=0;
		nB=0;
		nC=0;
		nD=0;
		return true;
	}
	
	public String Datos() {
		String s = "";
		s += ok(rC) + "Respawn equipo C: " + dat(rC) + "\n";
		s += ok(rD) + "Respawn equipo D: " + dat(rD) + "\n";
		s+=super.Datos();
		return s;
	}
	
	public void finalizar() {
		super.finalizar();
		pC = 0;
		pD = 0;
		nA=0;
		nB=0;
		nC=0;
		nD=0;
	}

	public void JugadorAbandona(Jugador j) {
		Equipo e = j.getEquipo();

		switch (e.toString()) {
		case "Azul":
			nA--;
			break;
		case "Rojo":
			nB--;

			break;
		case "Verde":
			nC--;

			break;
		case "Amarillo":
			nD--;

			break;
		default:
			break;
		}
		if (estado==Estado.EnCurso&&(
				nA+nB+nC==0 || nA + nB + nD ==0
				|| nA + nC + nD ==0 || nB + nC + nD == 0)) {
			forzarFin();
		}
		super.JugadorAbandona(j);
	}

	public void compruebaPuntuacion() {
		if (pMax != 0 && (pA >= pMax || pB >= pMax || pC > pMax || pD > pMax)) {
			ganador();
			setEstado(Estado.Finalizada);
		}
	}

	public void ganador() {
		EquipoVar winner=null;

		if (pMax != 0 && pA < pMax && pB < pMax && pC < pMax && pD < pMax) {
			return;
		}
		int x=Math.max(Math.max(pA,pB), Math.max(pB,pC));
		
		if (pA==x) {
			setPuntuaciones(Equipo.A);
			winner=Variables.A;
		}else{
			setPuntuacionesPerdedor(Equipo.A);
		}
		if (pB==x) {
			setPuntuaciones(Equipo.B);
			winner=Variables.B;
		}else{
			setPuntuacionesPerdedor(Equipo.B);
		}
		if (pC==x) {
			setPuntuaciones(Equipo.C);
			winner=Variables.C;
		}else{
			setPuntuacionesPerdedor(Equipo.C);
		}
		if (pD==x) {
			setPuntuaciones(Equipo.D);
			winner=Variables.D;
		}else{
			setPuntuacionesPerdedor(Equipo.D);
		}
		if(winner==null){return;}
		sendAll(Lang.get("fin_winner").replace("%Winner%", winner.name));

	}


	public int getpC() {
		return pC;
	}

	public int getpD() {
		return pD;
	}

	public void setEquipo(Jugador j) {
		if (nA<=nB&&nA<=nC&&nA<=nD){
			j.setEquipo(Equipo.A);
			nA++;
		} else if (nB<=nA&&nB<=nC&&nB<=nD){
			j.setEquipo(Equipo.B);
			nB++;
		}else if (nC<=nB&&nC<=nB&&nC<=nD){
			j.setEquipo(Equipo.C);
			nC++;
		}else if (nD<=nA&&nD<=nC&&nD<=nB){
			j.setEquipo(Equipo.D);
			nD++;
		}
	}
	
	protected void setPuntuaciones(Equipo eq) {
		Enumeration<IJugador> j = jugadores.elements();
		while (j.hasMoreElements()) {
			Jugador jj = (Jugador) j.nextElement();
			if(jj.getEquipo().equals(eq)){
				jj.setGanador(1);
			}
		}
	}
	protected void setPuntuacionesPerdedor(Equipo eq) {
		Enumeration<IJugador> j = jugadores.elements();
		while (j.hasMoreElements()) {
			Jugador jj = (Jugador) j.nextElement();
			if(jj.getEquipo().equals(eq)){
				jj.setGanador(0);
			}
		}
	}
	public void forzarFin() {
		EquipoVar winner=null;
		int x=Math.max(Math.max(pA,pB), Math.max(pB,pC));
		if (pA==x) {
			setPuntuaciones(Equipo.A);
			winner=Variables.A;
		}else{
			setPuntuacionesPerdedor(Equipo.A);
		}
		if (pB==x) {
			setPuntuaciones(Equipo.B);
			winner=Variables.B;
		}else{
			setPuntuacionesPerdedor(Equipo.B);
		}
		if (pC==x) {
			setPuntuaciones(Equipo.C);
			winner=Variables.C;
		}else{
			setPuntuacionesPerdedor(Equipo.C);
		}
		if (pD==x) {
			setPuntuaciones(Equipo.D);
			winner=Variables.D;
		}else{
			setPuntuacionesPerdedor(Equipo.D);
		}
		this.setEstado(Estado.Finalizada);
		if(winner!=null){
			sendAll(Lang.get("fin_winner").replace("%Winner%", winner.name));
		}else{
			sendAll(Lang.get("fin_empate"));
		}
	}
	
	public boolean Funcional() {
		return (mapa != null&&rA!=null&&rB!=null&&rC!=null
				&&rD!=null);
	}
	
	@Override
	public BoardGames getScore() {
		return new BoardE4(this);
	}

	public void setRC(Respawn respawn) {
		rC = respawn;
	}

	public void setRD(Respawn respawn) {
		rD = respawn;
	}
	
	@Override
	public void guardar(Almacen nbt) {

		super.guardar(nbt);

		if (rC != null) {
			int[] vecBanderaA = { (int) rC.getPosicion().getX(),
					(int) rC.getPosicion().getY(),
					(int) rC.getPosicion().getZ(), (int) rC.getRadio() };
			nbt.setString("wC", rC.getPosicion().getWorld().getName());
			nbt.setIntArray("bC", vecBanderaA);
		}
		

		if (rD != null) {
			int[] vecBanderaB = { (int) rD.getPosicion().getX(),
					(int) rD.getPosicion().getY(),
					(int) rD.getPosicion().getZ(), (int) rD.getRadio() };

			nbt.setString("wD", rD.getPosicion().getWorld().getName());

			nbt.setIntArray("bD", vecBanderaB);

		}
	}
	public void cargar(Almacen nbt) {
		super.cargar(nbt);

		String w = nbt.getString("wC");
		if (w.length() != 0) {
			int[] vec = nbt.getIntArray("bC");
			rC=new Respawn(Equipo.C, new Location(Bukkit.getServer()
					.getWorld(w), vec[0], vec[1], vec[2]), vec[3]);
		}
		
		w = nbt.getString("wD");
		if (w.length() != 0) {
			int[] vec = nbt.getIntArray("bD");
			rD=(new Respawn(Equipo.D, new Location(Bukkit.getServer()
					.getWorld(w), vec[0], vec[1], vec[2]), vec[3]));
		}
	}
	
	public boolean SpawnCompleto() {
		return rC.Completo() && rD.Completo()
				&& super.SpawnCompleto();
	}

	public boolean SpawnFuncional() {
		return !(rC.rVacio() && rD.rVacio()) && super.SpawnFuncional();
	}

	public void creaSpawners() {
		super.creaSpawners();
		long ini = System.currentTimeMillis();
		while (!(rD.Completo())
				&& (ini + timeO) - System.currentTimeMillis() > 0) {

			rD.getFreeZone();

		}

		ini = System.currentTimeMillis();
		while (!(rC.Completo())
				&& (ini + timeO) - System.currentTimeMillis() > 0) {
			rC.getFreeZone();

		}
		
	}
	

	public Location spawnZone(Jugador j) {
		if (j.getEquipo() == Equipo.A) {
			return rA.SpawnZone().clone().add(0.5, 0, 0.5);
		} else if(j.getEquipo() == Equipo.B){
			return rB.SpawnZone().clone().add(0.5, 0, 0.5);
		} else if(j.getEquipo() == Equipo.C){
			return rC.SpawnZone().clone().add(0.5, 0, 0.5);
		} else{
			return rD.SpawnZone().clone().add(0.5, 0, 0.5);
		}
	}
	
	
	
}
