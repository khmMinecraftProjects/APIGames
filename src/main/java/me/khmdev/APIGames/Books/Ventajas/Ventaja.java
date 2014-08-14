package me.khmdev.APIGames.Books.Ventajas;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import me.khmdev.APIBase.API;
import me.khmdev.APIGames.Auxiliar.Jugador;

public abstract class Ventaja {
	protected String name,descripcion;
	protected ItemStack item;
	protected double price;
	public Ventaja(String nam,String description){
		name=(nam);
		descripcion=(description);
		item=new ItemStack(Material.APPLE);
	}
	public Ventaja(String nam,String description, double p){
		name=(nam);
		descripcion=(description);
		price=p;
		item=new ItemStack(Material.APPLE);

	}
	public void use(Jugador jugador){
		API.removeMetadata(jugador.getPlayer(), name);
		equip(jugador);
	}
	
	protected abstract void equip(Jugador jugador);

	public abstract void spawn(Jugador j);
	
	public abstract void respawn(Jugador j, PlayerRespawnEvent event);
	
	public abstract void death(Jugador j,EntityDeathEvent event);

	public String getName() {
		return name;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public double getPrice() {
		return price;
	}
	
	public boolean have(Player pl) {
		return 	API.getMetadata(pl, name)!=null;
	}
	
	public boolean buy(Jugador jugador) {
		return 	API.getMetadata(jugador.getPlayer(), name)!=null;
	}
	
	public Ventaja getNew() {
		return this; 
	}
	public ItemStack getItem() {
		return item;
	}
	public void setItem(ItemStack item) {
		this.item = item;
	}
	

}
