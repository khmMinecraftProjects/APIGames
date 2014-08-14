package me.khmdev.APIGames.Books;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CItems;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomInventory;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomItem;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.ItemOpenInventory;
import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.APIGames.Auxiliar.EquipoVar;
import me.khmdev.APIGames.Auxiliar.Jugador;
import me.khmdev.APIGames.Auxiliar.Variables;
import me.khmdev.APIGames.Books.Ventajas.InitBook;
import me.khmdev.APIGames.Games.Game;
import me.khmdev.APIGames.lang.Lang;

public class BookE2 extends InitBook{
	protected ItemOpenInventory equipo;
	protected CustomInventory inven;
	public BookE2(Game g) {
		super(g);

		inven=new CustomInventory(Lang.get("selector_team").replace("%Game%", g.getAlias())
				);
		
		equipo=new ItemOpenInventory(AuxPlayer.getItem
				(Material.WOOL, Lang.get("select_team")), 
				inven.getInventory());
		inv.addItem(equipo);

		inven.addItem(new setterEquipo(Variables.A));
		inven.addItem(new setterEquipo(Variables.B));
		CItems.addInventorys(inven);
	}

	
	public class setterEquipo extends CustomItem{
		EquipoVar equip;
		@SuppressWarnings("deprecation")
		public  setterEquipo(EquipoVar eqq){
			super(AuxPlayer
					.getItem(Material.WOOL, "Equipo "+eqq.name,eqq.dye.getData()));
			equip=eqq;
		}
		@Override
		public void execute(InventoryClickEvent event) {
			@SuppressWarnings("deprecation")
			Player pl=Bukkit.getPlayer(event.getWhoClicked().getName());
			if(pl==null){return;}
			if(event.getWhoClicked().hasPermission("setterEquipo.apig")){
			Jugador j=
					game.getJugador(event.getWhoClicked().getName());
			if(j==null){return;}
			j.getPartida().setEquipo(j, equip.equipo);
			pl.sendMessage(
					Lang.get("selected_team").replace("%Team%",equip.name));

			}else{
				pl.sendMessage(Lang.get("no_perms"));
			}
		}

		@Override
		public void execute(PlayerInteractEvent event) {
			
		}
		
	}
	
	
}
