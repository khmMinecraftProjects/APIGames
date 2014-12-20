package me.khmdev.APIGames.Books.Kits;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.khmdev.APIAuxiliar.Inventory.InventoryBase;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CItems;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomInventory;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.ItemOpenInventory;
import me.khmdev.APIAuxiliar.Players.AuxPlayer;

public class GestorKit {
	private CustomInventory inventory;
	private String game;
	private ItemStack standar=new ItemStack(Material.BEACON);
	public GestorKit(String gam,Inventory volver) {
		game=gam;
		inventory=new CustomInventory("Kits "+gam);
		//inventory.addItem(new ItemOpenInventory(AuxPlayer.getItem(Material.STICK,
		//		"&CVolver"), volver));
		CItems.addInventorys(inventory);

	}
	
	public Inventory getInventory() {
		return inventory.getInventory();
	}

	public void addKit(String name,ItemStack[] inv,ItemStack[] arm){
		
		InventoryShowKit show=new InventoryShowKit(name,
				game,name, inv,arm,inventory.getInventory());
		inventory.addItem(new ItemOpenInventory(standar
						, show.getInventory()));
		CItems.addInventorys(show);

	}
	
	public void addKit(InventoryBase inv){
		InventoryShowKit show=new InventoryShowKit(inv.getName(),
				game,inv.getName(), inv.getInventory(),inv.getArmor()
				,inventory.getInventory());
		
		inventory.addItem(new ItemOpenInventory(
				AuxPlayer.getItem(Material.BLAZE_POWDER,
								inv.getName()),show.getInventory()));

		CItems.addInventorys(show);
	}
	
	public void addKit(InventoryBase inv,double pr){
		InventoryShowKit show=new InventoryShowKit(inv.getName(),
				game,inv.getName(), inv.getInventory(),inv.getArmor()
				,inventory.getInventory(),pr);
		
		inventory.addItem(new ItemOpenInventory(
				AuxPlayer.getItem(Material.BLAZE_POWDER,
								inv.getName()),show.getInventory()));

		CItems.addInventorys(show);
	}
	public void addKit(InventoryBase inv,double pr,String p){
		InventoryShowKit show=new InventoryShowKit(inv.getName(),
				game,inv.getName(),p, inv.getInventory(),inv.getArmor()
				,inventory.getInventory(),pr);
		
		inventory.addItem(new ItemOpenInventory(
				AuxPlayer.getItem(Material.BLAZE_POWDER,
								inv.getName()),show.getInventory()));

		CItems.addInventorys(show);
	}
	public void addKit(Material m,InventoryBase inv,double pr,String p){
		InventoryShowKit show=new InventoryShowKit(inv.getName(),
				game,inv.getName(),p, inv.getInventory(),inv.getArmor()
				,inventory.getInventory(),pr);
		
		inventory.addItem(new ItemOpenInventory(
				AuxPlayer.getItem(m,
								inv.getName()),show.getInventory()));

		CItems.addInventorys(show);
	}
	public void addKit(Material m,InventoryBase inv){
		InventoryShowKit show=new InventoryShowKit(inv.getName(),
				game,inv.getName(), inv.getInventory(),inv.getArmor()
				,inventory.getInventory());
		
		inventory.addItem(new ItemOpenInventory(
				AuxPlayer.getItem(m,
								inv.getName()),show.getInventory()));

		CItems.addInventorys(show);
	}
	
	public void addKit(Material m,InventoryBase inv,double pr){
		InventoryShowKit show=new InventoryShowKit(inv.getName(),
				game,inv.getName(), inv.getInventory(),inv.getArmor()
				,inventory.getInventory(),pr);
		
		inventory.addItem(new ItemOpenInventory(
				AuxPlayer.getItem(m,
								inv.getName()),show.getInventory()));

		CItems.addInventorys(show);
	}
}
