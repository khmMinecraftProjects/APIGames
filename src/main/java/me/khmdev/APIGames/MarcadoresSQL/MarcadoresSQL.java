package me.khmdev.APIGames.MarcadoresSQL;

import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.khmdev.APIGames.Auxiliar.Jugador;

public class MarcadoresSQL extends BukkitRunnable{
	private static Queue<Jugador> actualizar=null;
	
	public MarcadoresSQL(){actualizar=new LinkedList<>();}
	public static void addMarcador(Jugador j){
		if(actualizar!=null){
		actualizar.add(j);}
	}
	private int id;
	@Override
	public void run() {
		Jugador j=null;
		if((j=actualizar.poll())!=null){
			SQLGame sql=j.getPartida().getGame().getSql();
			sql.actualizar(j);
		}
	}
	public void setId(int idd) {
		id=idd;
	}
	public void kill(){Bukkit.getScheduler().cancelTask(id);}
	
}
