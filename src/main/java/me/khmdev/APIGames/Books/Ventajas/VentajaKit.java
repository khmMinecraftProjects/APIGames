package me.khmdev.APIGames.Books.Ventajas;

import me.khmdev.APIGames.Auxiliar.Jugador;

import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;

public class VentajaKit  extends Ventaja{
	private Inventory inventario;
	public VentajaKit(Inventory inv) {
		super(inv.getName(), "kit");
		inventario=inv;
	}
	

	@Override
	public void spawn(Jugador j) {
		
	}

	@Override
	public void respawn(Jugador j,PlayerRespawnEvent event) {
		
	}

	@Override
	public void death(Jugador j,EntityDeathEvent event) {
		
	}

	@Override
	protected void equip(Jugador jugador) {
		jugador.setKit(inventario);
	}



}
