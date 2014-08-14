package me.khmdev.APIGames.Books.Kits;

import me.khmdev.APIAuxiliar.Inventory.InventoryBase;
import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.APIBase.API;
import me.khmdev.APIEconomy.ConstantesEconomy;
import me.khmdev.APIEconomy.Own.APIEconomy;

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
		AuxPlayer.addDescription(item, "Precio: " + price+ ConstantesEconomy.UM);
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
		AuxPlayer.addDescription(item, "Precio: " + price+ ConstantesEconomy.UM);
		if(p!=null){
			perms=p;
			AuxPlayer.addDescription(item, "VIP");
		}
	}

	public void execute(InventoryClickEvent event) {
		if (Buy(event.getWhoClicked())) {
			Bukkit.getServer().getPlayer(event.getWhoClicked().getUniqueId())
					.sendMessage("Se usara el kit " + name);
			API.setMetadata(event.getWhoClicked(), game + "_kit", name);
		}
	}

	protected boolean Buy(HumanEntity humanEntity) {
		if(perms!=null && !humanEntity.hasPermission(perms)){
			Bukkit.getServer()
			.getPlayer(humanEntity.getUniqueId())
			.sendMessage("No tienes permiso para comprar este kit");
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
					.sendMessage(
							"Has comprado el kit " + name + " por " + price
									+ ConstantesEconomy.UM);
			API.setMetadata(humanEntity, name+"_buy",true);
			return true;
		} else {
			Bukkit.getServer().getPlayer(humanEntity.getUniqueId())
					.sendMessage("Fondos insuficientes");
			return false;
		}
	}

}
