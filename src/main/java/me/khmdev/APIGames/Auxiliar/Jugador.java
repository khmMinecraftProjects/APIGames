package me.khmdev.APIGames.Auxiliar;

import me.khmdev.APIAuxiliar.ScoreBoard.IBoard;
import me.khmdev.APIGames.Auxiliar.ConstantesGames.Equipo;
import me.khmdev.APIGames.Partidas.IPartida;
import me.khmdev.APIGames.Partidas.Partida;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Jugador implements IJugador{
	private Player player=null;
	private IBoard last=null;
	private IPartida partida=null;
	private Equipo equipo=Equipo.Ninguno;
	private int puntuacion=0,kills=0,deaths=0;
	private ItemStack[] inventario=null;
	private ItemStack[] armor=null;
	private String tag="",tab="";

	private Inventory kit=null;
	private int ganador=-1;
	private boolean abandona=false;
	public Jugador(){
		setPuntuacion(0);
		kit=null;
	}
	
	public void kill(){kills++;}

	public void death(){deaths++;}

	public int getKills(){return kills;}
	
	public int getDeaths(){return deaths;}

	
	public void setKit(Inventory inv){
		kit=inv;
	}
	public Inventory getKit(){
		return kit;
	}
	public IPartida getPartida() {
		return partida;
	}
	public void setPartida(Partida partida) {
		this.partida = partida;
	}
	public Equipo getEquipo() {
		return equipo;
	}
	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}
	public String toString() {
		return player.getName();
	}

	public void setPlayer(Player p) {
		player=p;
	}
	public Player getPlayer() {
		return player;
	}

	public int getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}

	public ItemStack[] getInventario() {
		return inventario;
	}

	public void setInventario(ItemStack[] inventario) {
		this.inventario = inventario;
	}

	public ItemStack[] getArmor() {
		return armor;
	}

	public void setArmor(ItemStack[] armor) {
		this.armor = armor;
	}

	public void setTag(String s) {
		tag=s;
	}
	
	public String getTag() {
		return (player!=null&&tag.length()==0)?player.getDisplayName():tag;
	}
	
	public void setTab(String s) {
		tab=s;
	}
	
	public String getTab() {
		return (player!=null&&tab.length()==0)?player.getName():tab;
	}
	public int isGanador() {
		return ganador;
	}

	public void setGanador(int ganador) {
		this.ganador = ganador;
	}

	@Override
	public IBoard getLastBoard() {
		return last;
	}

	@Override
	public void setLastBoard(IBoard b) {
		last=b;
	}

	@Override
	public boolean abandonado() {
		return abandona;
	}

	@Override
	public void abandona() {
		abandona=true;		
	}

}
