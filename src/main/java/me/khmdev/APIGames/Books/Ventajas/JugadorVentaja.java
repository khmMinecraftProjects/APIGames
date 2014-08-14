package me.khmdev.APIGames.Books.Ventajas;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.khmdev.APIGames.Auxiliar.Jugador;

public class JugadorVentaja {
	@SuppressWarnings("unused")
	private Jugador jugador;
	List<Ventaja> ventajas;
	public JugadorVentaja(Jugador j){
		jugador=j;
		ventajas=new LinkedList<>();
	}
	
	public void addVentaja(Ventaja v){

		ventajas.add(v);

	}
	
	protected void equip(Jugador j){
		Iterator<Ventaja> it=ventajas.iterator();
		while(it.hasNext()){
			Ventaja v=it.next();
			if(v!=null){
			v.equip(j);}
		}
	}

	public void spawn(Jugador j){
		Iterator<Ventaja> it=ventajas.iterator();
		while(it.hasNext()){
			Ventaja v=it.next();
			if(v!=null){
			v.spawn(j);}
		}
	}
	
	public void respawn(Jugador j, PlayerRespawnEvent event){
		Iterator<Ventaja> it=ventajas.iterator();
		while(it.hasNext()){
			Ventaja v=it.next();
			if(v!=null){
			v.respawn(j,event);}
		}
	}
	
	public void death(Jugador j,EntityDeathEvent event){
		Iterator<Ventaja> it=ventajas.iterator();
		while(it.hasNext()){
			Ventaja v=it.next();
			if(v!=null){
			v.death(j,event);}
		}
	}
}
