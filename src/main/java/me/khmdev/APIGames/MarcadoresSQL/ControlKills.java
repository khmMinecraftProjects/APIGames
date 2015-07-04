package me.khmdev.APIGames.MarcadoresSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;

import me.khmdev.APIBase.Almacenes.ConstantesAlmacen.typeVar;
import me.khmdev.APIBase.Almacenes.sql.Consulta;
import me.khmdev.APIBase.Almacenes.sql.varSQL;
import me.khmdev.APIBase.Almacenes.sql.player.SQLPlayerData;
import me.khmdev.APIEconomy.Own.APIEconomy;
import me.khmdev.APIGames.lang.Lang;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ControlKills implements Listener {
	private static HashMap<String, Integer> players = new HashMap<>();

	public static void kill(Player p) {
		int n = 0;
		if (players.containsKey(p.getName())) {
			n = players.get(p.getName());
		}
		n++;
		switch (n) {
		case 10:
			p.sendMessage(Lang.get("ControlKills.10"));
			APIEconomy.addCash(p.getName(), 10);
			break;
		case 100:
			p.sendMessage(Lang.get("ControlKills.100"));
			APIEconomy.addCash(p.getName(), 100);
			break;
		case 500:
			p.sendMessage(Lang.get("ControlKills.500"));
			APIEconomy.addCash(p.getName(), 250);
			break;
		case 1000:
			p.sendMessage(Lang.get("ControlKills.1000"));
			APIEconomy.addCash(p.getName(), 1000);
			break;
		default:
			break;
		}
		players.put(p.getName(), n);
	}

	public static void init(JavaPlugin api) {
		if (!SQLPlayerData.existVar("killStreack")) {
			SQLPlayerData.addVars(new varSQL("killStreack", typeVar.inteteger));
		}
		Bukkit.getServer().getPluginManager()
				.registerEvents(new ControlKills(), api);
		for(Player p:Bukkit.getServer().getOnlinePlayers()){
			String n=p.getName();
			int i=getKills(n);
			players.put(n, i);
		}

	}

	@EventHandler
	public void login(PlayerLoginEvent e) {
		String name = e.getPlayer().getName();

		int s = getKills(name);
		players.put(name, s);

	}
	private static int getKills(String name){
		int s = 0;

		if (!SQLPlayerData.existUser(name)) {
			SQLPlayerData.crearUser(name);
		}else{
			Consulta c = SQLPlayerData.getPlayer(name);
			if(c==null){
				Bukkit.getServer().getLogger().
				severe("ControlKills no pudo conectarse con la bd");
				return 0;}
			try {
				ResultSet r=c.getR();
				if (r.next()) {
					s = r.getInt("killStreack");
				}
			} catch (SQLException e1) {
				Bukkit.getServer().getLogger().
				severe("ControlKills no pudo conectarse con la bd");
			}finally{
				c.close();
			}
		}

		
		return s;
	}

	@EventHandler
	public void logout(PlayerQuitEvent e) {
		String n = e.getPlayer().getName();
		if (!players.containsKey(n)) {
			return;
		}
		int c = players.get(n);
		SQLPlayerData.setVar(n, "killStreack", c);
		players.remove(n);
	}

	public static void shutdown() {
		for (Entry<String, Integer> n : players.entrySet()) {
			SQLPlayerData.setVar(n.getKey(), "killStreack", n.getValue());
		}
	}
}
