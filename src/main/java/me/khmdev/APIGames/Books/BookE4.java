package me.khmdev.APIGames.Books;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.ItemOpenInventory;
import me.khmdev.APIGames.Auxiliar.Variables;
import me.khmdev.APIGames.Games.Game;

public class BookE4 extends BookE2{
	protected ItemOpenInventory equipo;
	public BookE4(Game g) {
		super(g);
		
		inven.addItem(new setterEquipo(Variables.C));
		
		inven.addItem(new setterEquipo(Variables.D));
	}

	

	
	
}
