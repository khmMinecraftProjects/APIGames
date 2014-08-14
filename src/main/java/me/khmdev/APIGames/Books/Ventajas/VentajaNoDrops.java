package me.khmdev.APIGames.Books.Ventajas;

import java.util.HashMap;

import me.khmdev.APIGames.Auxiliar.Jugador;

import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.PlayerInventory;

public class VentajaNoDrops extends Ventaja{
	private HashMap<String, PlayerInventory> saves;
	private double porciento;
	public VentajaNoDrops(
								double percent, double p) {
		super("NoDrops "+percent*100+"%", 
				"Tienes "+percent*100+
				"% de probabilidades de no perder el inventario", p);
		saves=new HashMap<>();
		porciento=percent;
	}

	@Override
	protected void equip(Jugador jugador) {
		
	}

	@Override
	public void spawn(Jugador j) {
		
	}

	@Override
	public void respawn(Jugador j,PlayerRespawnEvent event) {
		if(saves.containsKey(j.getPlayer().getName())){
			PlayerInventory s=saves.get(j.getPlayer().getName());
			j.getPlayer().getInventory().setArmorContents(s.getArmorContents());
			j.getPlayer().getInventory().setContents(s.getContents());	
		}
	}

	@Override
	public void death(Jugador j,EntityDeathEvent event) {
		if(Math.random()<porciento){
			saves.put(j.getPlayer().getName(), j.getPlayer().getInventory());
			event.getDrops().clear();
		}
	}

}
