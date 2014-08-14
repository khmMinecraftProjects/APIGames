package me.khmdev.APIGames.Auxiliar;


import java.util.LinkedList;
import java.util.List;

import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Equipo;

import org.bukkit.Location;
import org.bukkit.World;


public class Respawn {
	protected Location posicion;
	protected int radio;
	protected List<Location> R;
	protected boolean maxA;
	protected Equipo own;
	public Respawn(Equipo e,Location a,int rd){
		own=e;
		posicion=a;
		radio=rd;
		R=new LinkedList<Location>();
		setMaxA(false);
	}
	
	
	public boolean getFreeZone() {
		Location l = new Location(getPosicion().getWorld(), 0, 0, 0), bb = getPosicion();
		World w = getPosicion().getWorld();
		if (R.size() != 0) {
			bb = R.get(R.size() - 1);
			l.setX(bb.getX());
			l.setY(bb.getY());
			l.setZ(bb.getZ() + 1);
		} else {

			l.setX(bb.getX() - getRadio());
			l.setY(bb.getY() - getRadio());
			l.setZ(bb.getZ() - getRadio());

		}
		for (int x = (int) l.getX(); x < bb.getX() + getRadio(); x++) {
			for (int y = (int) l.getY(); y < bb.getY() + getRadio(); y++) {
				for (int z = (int) l.getZ(); z < bb.getZ() + getRadio(); z++) {
					if (AuxPlayer.isPossibleSpawn(w, x, y, z)) {
						R.add(new Location(w, x, y, z));
						return false;
					}l.setZ(bb.getZ() - getRadio());
				}l.setY(bb.getY() - getRadio());
			}
		}
		 setMaxA(true);
		return true;
	}

	public Location SpawnZone() {
		return R.get((int) (Math.random() * R.size()));
	}
	public Location getPosicion() {
		return posicion;
	}
	public void setPosicion(Location posicion) {
		this.posicion = posicion;
	}
	public int getRadio() {
		return radio;
	}
	public void setRadio(int radio) {
		this.radio = radio;
	}

	public String toString(){
		String s="";
		s+=posicion.getX()+";"+posicion.getY()+";"
				+posicion.getZ()+";r:"+radio;
		return s;
	}


	public boolean isMaxA() {
		return maxA;
	}


	public void setMaxA(boolean maxA) {
		this.maxA = maxA;
	}


	public Equipo getOwn() {
		return own;
	}


	public void setOwn(Equipo own) {
		this.own = own;
	}


	public boolean Completo() {
		return !(R.size() < 10 && !maxA);
	}

	public boolean rVacio(){
		return R.isEmpty();
	}
}
