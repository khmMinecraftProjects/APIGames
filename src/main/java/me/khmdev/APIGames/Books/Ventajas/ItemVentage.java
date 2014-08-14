package me.khmdev.APIGames.Books.Ventajas;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.khmdev.APIBase.API;

public class ItemVentage extends ShopItem {
	private Ventaja ventaja;

	public ItemVentage(ItemStack it, Ventaja v) {
		super(it, v.getPrice());
		ItemMeta meta = it.getItemMeta();
		List<String> list = meta.getLore();
		if (v.getDescripcion().length() < 20) {
			list.add(v.getDescripcion());
		} else {
			String d=v.getDescripcion();
			int max=35;
			while (d.length()!=0) {
				if(d.length()>=max){
					list.add(d.substring(0,max));
					d=d.substring(max);
				}else{
					list.add(d.substring(0, d.length()));
					d="";
				}
			}
		}
		meta.setLore(list);
		meta.setDisplayName(v.getName());
		it.setItemMeta(meta);
		ventaja = v;
	}

	@Override
	public void execute(InventoryClickEvent event) {
		if (API.getMetadata(event.getWhoClicked(), ventaja.getName()) != null) {
			Bukkit.getServer().getPlayer(event.getWhoClicked().getUniqueId())
					.sendMessage("Ya lo has comprado");
			return;
		}
		if (Buy(event.getWhoClicked())) {
			API.setMetadata(event.getWhoClicked(), ventaja.getName(), true);
		}
	}

	@Override
	public void execute(PlayerInteractEvent event) {

	}

}
