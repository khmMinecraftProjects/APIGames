package me.khmdev.APIGames.Books.Ventajas;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CItems;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomInventory;
import me.khmdev.APIGames.Auxiliar.IJugador;
import me.khmdev.APIGames.Auxiliar.Jugador;

public class GestorDeVentajas {
	List<Ventaja> ventajas;
	HashMap<String,JugadorVentaja> jugadores;
	CustomInventory inventory;
	public GestorDeVentajas(String game,Inventory inv){
		ventajas=new LinkedList<>();
		jugadores=new HashMap<>();
		inventory=new CustomInventory("Ventajas "+game);
		//inventory.addItem(new ItemOpenInventory(AuxPlayer.getItem(Material.STICK,
		//		"&CVolver"), inv));
		CItems.addInventorys(inventory);
	}
	
	public void addVentaja(Ventaja v){
		ventajas.add(v);

		inventory.addItem(new ItemVentage(v.getItem(), v));
	}
	
	public Inventory getInvVentajas(){
		return inventory.getInventory();
	}
	
	public void CargarVentajas(Jugador j){
		Iterator<Ventaja> it=ventajas.iterator();
		while(it.hasNext()){
			Ventaja v=it.next();
			if(v.have(j.getPlayer())){
				v.use(j);
				j.getPlayer().sendMessage("Usada ventaja: "+v.getName());
				addVP(j,v.getNew());
			}
		}
	}
	
	private void addVP(Jugador j,Ventaja v){
		JugadorVentaja jv=jugadores.get(j.getPlayer().getName());
		if(jv==null){
			jv=new JugadorVentaja(j);
			jugadores.put(j.getPlayer().getName(), jv);

		}
		jv.addVentaja(v);

	}
	
	protected void equip(Jugador jugador){
		JugadorVentaja jv=jugadores.get(jugador.getPlayer().getName());
		if(jv!=null){jv.equip(jugador);}
	}

	public void spawn(Jugador j){
		JugadorVentaja jv=jugadores.get(j.getPlayer().getName());
		if(jv!=null){jv.spawn(j);}
	}
	
	public void respawn(Jugador j,PlayerRespawnEvent event){
		JugadorVentaja jv=jugadores.get(j.getPlayer().getName());
		if(jv!=null){jv.respawn(j,event);}
	}
	
	public void death(Jugador j,EntityDeathEvent event){
		JugadorVentaja jv=jugadores.get(j.getPlayer().getName());
		if(jv!=null){jv.death(j,event);}
	}

	public void CargarVentajas(Enumeration<IJugador> elements) {
		while(elements.hasMoreElements()){
			Jugador j= (Jugador) elements.nextElement();
			CargarVentajas(j);
			spawn(j);
		}
	}
	
	public void unloadVentajas(Enumeration<IJugador> elements) {
		while(elements.hasMoreElements()){
			removeJugador((Jugador) elements.nextElement());
		}
	}
	public void removeJugador(Jugador j) {
		jugadores.remove(j.getPlayer().getName());
	}
}
