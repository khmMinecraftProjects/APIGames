package me.khmdev.APIGames.Books.Kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomInventory;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.ItemOpenInventory;
import me.khmdev.APIAuxiliar.Players.AuxPlayer;

public class InventoryShowKit extends CustomInventory {
	private int posOk = (5 * 9 - 1);



	public InventoryShowKit(String name, String game, String name2,
			ItemStack[] inv, ItemStack[] arm,Inventory volver) {
		super(name);
		inventory = Bukkit.getServer().createInventory(null, 5 * 9,game+"_"+ name);
		pos = 2;
		
		ItemKits it=new ItemKits(game,name,AuxPlayer.getItem(Material.STICK,"&2Aceptar")
				, inv,arm);
		ItemOpenInventory it2=new ItemOpenInventory(
				AuxPlayer.getItem(Material.STICK,"&CVolver"),volver);
		items.add(it);
		items.add(it2);
		
		ItemStack[] aux = inventory.getContents();
		aux[aux.length - 1] = it.getItem();
		aux[aux.length - 2] = it2.getItem();
		inventory.setContents(aux);
		
		addArmors(it.getArmor());
		addItems(it.getItems());
	}

	public InventoryShowKit(String name, String game, String name2,
			ItemStack[] inv, ItemStack[] arm,Inventory volver,
			double pr) {
		super(name);
		inventory = Bukkit.getServer().createInventory(null, 5 * 9,game+"_"+ name);
		pos = 2;
		
		ItemKitShop it=new ItemKitShop(game,name,null,AuxPlayer.getItem(Material.STICK,"&2Aceptar")
				, inv,arm,pr);
		ItemOpenInventory it2=new ItemOpenInventory(
				AuxPlayer.getItem(Material.STICK,"&CVolver"),volver);
		items.add(it);
		items.add(it2);
		
		ItemStack[] aux = inventory.getContents();
		aux[aux.length - 1] = it.getItem();
		aux[aux.length - 2] = it2.getItem();
		inventory.setContents(aux);
		
		addArmors(it.getArmor());
		addItems(it.getItems());
	}
	
	public InventoryShowKit(String name, String game, String name2,String perms,
			ItemStack[] inv, ItemStack[] arm,Inventory volver,
			double pr) {
		super(name);
		inventory = Bukkit.getServer().createInventory(null, 5 * 9,game+"_"+ name);
		pos = 2;
		
		ItemKitShop it=new ItemKitShop(game,name,perms,AuxPlayer.getItem(Material.STICK,"&2Aceptar")
				, inv,arm,pr);
		ItemOpenInventory it2=new ItemOpenInventory(
				AuxPlayer.getItem(Material.STICK,"&CVolver"),volver);
		items.add(it);
		items.add(it2);
		
		ItemStack[] aux = inventory.getContents();
		aux[aux.length - 1] = it.getItem();
		aux[aux.length - 2] = it2.getItem();
		inventory.setContents(aux);
		
		addArmors(it.getArmor());
		addItems(it.getItems());
	}
	public void addArmor(ItemStack item) {
		int p = AuxPlayer.posArmor(item);
		ItemStack[] aux;
		switch (p) {
		case 0:
			aux = inventory.getContents();
			aux[9 * 3] = item;
			inventory.setContents(aux);
			break;
		case 1:
			aux = inventory.getContents();
			aux[9 * 2] = item;
			inventory.setContents(aux);
			break;
		case 2:
			aux = inventory.getContents();
			aux[9] = item;
			inventory.setContents(aux);
			break;
		case 3:
			aux = inventory.getContents();
			aux[0] = item;
			inventory.setContents(aux);
			break;
		default:
			break;
		}
	}

	public void Request(InventoryClickEvent event) {
		event.setCancelled(true);
		if (items.get(0) == null) {
			event.setCancelled(true);
			return;
		}
		if(event.getSlot()==posOk){
			items.get(0).execute(event);
		}else if(event.getSlot()==posOk-1){
			items.get(1).execute(event);
		}
	}

	public void addItems(ItemStack[] item) {
		ItemStack[] aux = inventory.getContents();

		for (int i = 0; i < item.length; i++) {
			if (item[i] != null) {
				aux[pos] = item[i];
				pos++;
				if (pos % 9 == 0) {
					pos += 2;
				}
			}
		}
		inventory.setContents(aux);

	}

	public void addArmors(ItemStack[] item) {
		for (int i = 0; i < item.length; i++) {
			if (item[i] != null) {
				addArmor(item[i]);
			}
		}
	}

	public void addItem(ItemStack item) {
		if (inventory.getSize() <= pos) {
			int len = inventory.getSize() + 9;
			ItemStack[] aux = inventory.getContents();
			inventory = Bukkit.getServer().createInventory(null, len, name);
			addItems(aux);
		}

		ItemStack[] aux = inventory.getContents();
		aux[pos] = item;
		pos++;
		if (pos % 9 == 0) {
			pos += 2;
		}
		inventory.setContents(aux);
	}
}
