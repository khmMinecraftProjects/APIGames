package me.khmdev.APIGames.Books.Ventajas;

import me.khmdev.APIBase.Auxiliar.Auxiliar;
import me.khmdev.APIGames.Auxiliar.Jugador;

import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class VentajaInitItem extends Ventaja{
	private ItemStack ItemOri;
	public VentajaInitItem(ItemStack it,
			String nam, String description,double p) {
		super(nam, description,p);
	}
	public VentajaInitItem(ItemStack it,double p) {
		super(Auxiliar.getOriginalName(it.getType().name())
				, "Obten el item al iniciar la partida",p);
		ItemOri=it.clone();
		item=it;
	}
	@SuppressWarnings("deprecation")
	@Override
	protected void equip(Jugador j) {
		j.getPlayer().getInventory().addItem(ItemOri);
		j.getPlayer().updateInventory();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void spawn(Jugador j) {

		j.getPlayer().getInventory().addItem(ItemOri);

		j.getPlayer().updateInventory();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void respawn(Jugador j,PlayerRespawnEvent event) {

		event.getPlayer().getInventory().addItem(ItemOri);
		event.getPlayer().updateInventory();

	}

	@Override
	public void death(Jugador j,EntityDeathEvent event) {
		
	}

}
