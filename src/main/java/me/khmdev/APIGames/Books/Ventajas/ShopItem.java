package me.khmdev.APIGames.Books.Ventajas;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomItem;
import me.khmdev.APIBase.Auxiliar.Auxiliar;
import me.khmdev.APIEconomy.ConstantesEconomy;
import me.khmdev.APIEconomy.Own.APIEconomy;
import me.khmdev.APIGames.lang.Lang;

public class ShopItem extends CustomItem {
	double price;
	ItemStack item2;

	public ShopItem(ItemStack it,double pric){
		item=it;
		price=pric;
		if(item==null){return;}
		item2 = item.clone();
		
		ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(Lang.get("ShopItem.price").replace("%price%", price+"")
				.replace("%UM%", ConstantesEconomy.UM+"")));
		item.setItemMeta(meta);
	}
	public double getPrice(){
		return price;
	}

	@Override
	public void execute(InventoryClickEvent event) {
		event.setCancelled(true);

		if (Buy(event.getWhoClicked())) {
			event.getWhoClicked().getInventory().addItem(item2);
		}
	}
	protected boolean Buy(HumanEntity humanEntity){
		if(item2==null){return false;}
		double money = APIEconomy.getCash(humanEntity.getName());

		if (price <= money) {
			APIEconomy.reduceCash(humanEntity.getName(),
					price);
			String name;
			if(item.getItemMeta()!=null
					&&item.getItemMeta().getDisplayName().length()!=0){
				name=item.getItemMeta().getDisplayName();
			}else{
				name=Auxiliar.getOriginalName(item.getData()
						.getItemType().name());
			}
			Bukkit.getServer()
					.getPlayer(humanEntity.getUniqueId())
					.sendMessage(
							Lang.get("ShopItem.buy").replace("%price%", price+"")
							.replace("%UM%", ConstantesEconomy.UM+"")
							.replace("%item%", name!=null?name:""));
			return true;
		} else {
			Bukkit.getServer().getPlayer(humanEntity.getUniqueId())
					.sendMessage(Lang.get("ShopItem.noMoney"));
			return false;
		}
	}

	@Override
	public void execute(PlayerInteractEvent event) {
		
	}



}
