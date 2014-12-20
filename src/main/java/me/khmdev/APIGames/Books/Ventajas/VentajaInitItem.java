package me.khmdev.APIGames.Books.Ventajas;

import me.khmdev.APIBase.Auxiliar.Auxiliar;
import me.khmdev.APIGames.Auxiliar.Jugador;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class VentajaInitItem extends Ventaja{
	protected ItemStack ItemOri;
	public VentajaInitItem(ItemStack it,
			String nam, String description,double p) {
		super(nam, description,p);
	}
	public VentajaInitItem(ItemStack it,double p) {
		super(Auxiliar.getOriginalName(
				it.getItemMeta().getDisplayName()!=null?
						it.getItemMeta().getDisplayName():
							it.getType().name())
				, "Obten el item al iniciar la partida",p);
		ItemOri=it;
		item=it.clone();
	}

	@Override
	protected void equip(Jugador j) {
		//j.getPlayer().getInventory().addItem(ItemOri);
		//j.getPlayer().updateInventory();
		eq(j.getPlayer());
	}
	@SuppressWarnings("deprecation")
	protected void eq(Player pl){
		pl.getInventory().remove(ItemOri);
		pl.getInventory().addItem(ItemOri);
		pl.updateInventory();
	}

	@Override
	public void spawn(Jugador j) {
		eq(j.getPlayer());

	}

	@Override
	public void respawn(Jugador j,PlayerRespawnEvent event) {
		eq(j.getPlayer());
	}

	@Override
	public void death(Jugador j,EntityDeathEvent event) {
		
	}

}
