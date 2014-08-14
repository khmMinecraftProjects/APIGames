package me.khmdev.APIGames.Books;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CItems;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomInventory;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomItem;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.ItemOpenInventory;
import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.APIGames.Books.Kits.GestorKit;
import me.khmdev.APIGames.Books.Ventajas.GestorDeVentajas;
import me.khmdev.APIGames.Games.IGame;

public class SelectorGame {
	private static HashMap<String, GestorDeVentajas> gestorV;
	private static HashMap<String, GestorKit> gestorK;

	private static CustomInventory invVent;
	private static CustomInventory invKit;

	private static CustomItem itemV,itemK;

	public static void init() {
		if (gestorV != null) {
			return;
		}
		gestorV = new HashMap<>();
		gestorK = new HashMap<>();

		invVent = new CustomInventory("Tienda de Ventajas");
		CItems.addInventorys(invVent);
		invKit = new CustomInventory("Tienda de Kits");
		CItems.addInventorys(invKit);
		itemV = new ItemOpenInventory(AuxPlayer.getItem(Material.BLAZE_ROD
				,"&2Ventajas"),
				invVent.getInventory());
		
		itemK = new ItemOpenInventory(AuxPlayer.getItem(Material.BLAZE_POWDER
				,"&1Kits"),
				invKit.getInventory());
		CItems.addItem(itemV);
		CItems.addItem(itemK);

	}

	public static ItemStack getItemV() {
		return itemV.getItem();
	}
	
	public static ItemStack getItemK() {
		return itemK.getItem();
	}

	
	public static GestorDeVentajas getGV(IGame g,Material m) {
		if (!gestorV.containsKey(g.getName())) {
			GestorDeVentajas gestor = new GestorDeVentajas(g.getName(),invVent.getInventory());
			gestorV.put(g.getName(), gestor);
			invVent.addItem(new ItemOpenInventory(AuxPlayer.getItem(m,g.getAlias())
											, gestor.getInvVentajas()));
			return gestor;
		}
		return gestorV.get(g.getName());
	}
	

	public static CustomInventory getSelector() {
		return invVent;
	}

	public static GestorKit getGK(IGame g, Material m) {
		if (!gestorK.containsKey(g.getName())) {
			GestorKit gestor = new GestorKit(g.getName(),invKit.getInventory());
			gestorK.put(g.getName(), gestor);
			invKit.addItem(new ItemOpenInventory(AuxPlayer.getItem(m,g.getAlias())
											, gestor.getInventory()));
			return gestor;
		}
		return gestorK.get(g.getName());
	}
}
