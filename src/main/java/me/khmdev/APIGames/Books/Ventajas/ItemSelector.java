package me.khmdev.APIGames.Books.Ventajas;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomItem;

public class ItemSelector extends CustomItem {
	private GestorDeVentajas gestor;

	public ItemSelector(ItemStack it, GestorDeVentajas g) {
		item=it;gestor=g;
	}

	@Override
	public void execute(InventoryClickEvent event) {
		event.getWhoClicked().openInventory(gestor.getInvVentajas());
	}

	@Override
	public void execute(PlayerInteractEvent event) {

	}

}
