package me.khmdev.APIGames.Books.Kits;

import me.khmdev.APIAuxiliar.Inventory.InventoryBase;
import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.APIBase.API;
import me.khmdev.APIEconomy.ConstantesEconomy;
import me.khmdev.APIEconomy.Own.APIEconomy;
import me.khmdev.APIGames.lang.Lang;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemKitShop extends ItemKits {
	private double price;
	private String perms=null;

	public ItemKitShop(String gam, String s,String p, ItemStack it, InventoryBase inv,
			double pr) {
		super(gam, s, it, inv);
		AuxPlayer.addDescription(item, Lang.get("ShopItem.buy").replace("%price%", price+"")
				.replace("%UM%", ConstantesEconomy.UM+"")
				.replace("%item%", name));
		price = pr;
		if(p!=null){
			perms=p;
			AuxPlayer.addDescription(item, "VIP");
		}
	}

	public ItemKitShop(String gam, String s,String p, ItemStack it, ItemStack[] inv,
			ItemStack[] arm, double pr) {
		super(gam, s, it, inv, arm);
		price = pr;
		AuxPlayer.addDescription(item, Lang.get("ShopItem.buy").replace("%price%", price+"")
				.replace("%UM%", ConstantesEconomy.UM+"")
				.replace("%item%", name!=null?name:""));
		if(p!=null){
			perms=p;
			AuxPlayer.addDescription(item, "VIP");
		}
	}

	public void execute(InventoryClickEvent event) {
		if (Buy(event.getWhoClicked())) {
			Bukkit.getServer().getPlayer(event.getWhoClicked().getUniqueId())
					.sendMessage(Lang.get("ItemKitShop.useKit").replace("%kit%", name));
			API.setMetadata(event.getWhoClicked(), game + "_kit", name);
		}
	}

	protected boolean Buy(HumanEntity humanEntity) {
		if(perms!=null && !humanEntity.hasPermission(perms)){
			Bukkit.getServer()
			.getPlayer(humanEntity.getUniqueId())
			.sendMessage(Lang.get("ItemKitShop.noPerms"));
			return false;
		}
		
		if(API.getMetadata(humanEntity, name+"_buy")!=null){
			return true;
		}
		double money = APIEconomy.getCash(humanEntity.getName());

		
		if (price <= money) {
			APIEconomy.reduceCash(humanEntity.getName(), price);

			Bukkit.getServer()
					.getPlayer(humanEntity.getUniqueId())
					.sendMessage(Lang.get("ItemKitShop.buy")
							.replace("%kit%",name)
							.replace("%price%",price+"")
							.replace("%UM%",ConstantesEconomy.UM+""));
			API.setMetadata(humanEntity, name+"_buy",true);
			return true;
		} else {
			Bukkit.getServer().getPlayer(humanEntity.getUniqueId())
					.sendMessage(Lang.get("ShopItem.noMoney"));
			return false;
		}
	}

}
