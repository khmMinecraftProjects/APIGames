package me.khmdev.APIGames.Books.Ventajas;

import org.bukkit.Material;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CItems;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomInventory;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.ItemOpenInventory;
import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.APIGames.Books.IBook;
import me.khmdev.APIGames.Games.Game;

public class InitBook implements IBook{
	protected CustomInventory inv;
	protected ItemOpenInventory book;
	protected Game game;

	public InitBook(Game g) {
		game=g;
		inv=new CustomInventory("Menu de "+g.getName());
		book=(new ItemOpenInventory(AuxPlayer.getItem(Material.BOOK,"Menu de "+g.getName()),
				inv.getInventory()));
		CItems.addItem(book);
		if(g.getGestorKit()!=null){
		inv.addItem(
				new ItemOpenInventory(
				AuxPlayer.getItem(Material.DIAMOND_SWORD,
						"Kits"),
				g.getGestorKit().getInventory()
				));}
		if(g.getGestorVentaja()!=null){
		inv.addItem(
				new ItemOpenInventory(
				AuxPlayer.getItem(Material.BLAZE_POWDER,
						"Ventajas"),
				g.getGestorVentaja().getInvVentajas()
				));
		}
		CItems.addInventorys(inv);
		
	}

	public ItemOpenInventory getBook() {
		return book;
	}

}
