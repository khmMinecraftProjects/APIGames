package me.khmdev.APIGames.Books.Kits;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.khmdev.APIAuxiliar.Inventory.InventoryBase;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomItem;
import me.khmdev.APIBase.API;
import me.khmdev.APIGames.lang.Lang;

public class ItemKits extends CustomItem {
	protected ItemStack[] armor;
	protected ItemStack[] inventory;
	protected String name,game,perms;
	public ItemKits(String gam,String s,ItemStack it, InventoryBase inv) {
		name=s;
		item = it;
		game=gam;
		inventory = inv.getInventory();
		armor = inv.getArmor();
	}

	public ItemKits(String gam,String s,ItemStack it, ItemStack[] inv, 
						ItemStack[] arm) {
		name=s;
		game=gam;
		item = it;
		inventory = inv;
		armor = arm;
	}



	public void execute(InventoryClickEvent event) {

		//event.getWhoClicked().getInventory().setArmorContents(armor);
		//event.getWhoClicked().getInventory().setContents(inventory);
		Bukkit.getServer().getPlayer(
		event.getWhoClicked().getUniqueId()).sendMessage(Lang.get("ItemKitShop.useKit").replace("%kit%", name));
		API.setMetadata(event.getWhoClicked(), game+"_kit", name);
	}

	public InventoryBase getInventory() {
		return new InventoryBase(name, armor, inventory);
	}
	public ItemStack[] getItems() {
		return inventory;
	}
	public ItemStack[] getArmor() {
		return armor;
	}
	public void execute(PlayerInteractEvent event) {

	}

}
