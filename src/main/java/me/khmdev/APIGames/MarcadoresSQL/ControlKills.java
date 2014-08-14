package me.khmdev.APIGames.MarcadoresSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;

import me.khmdev.APIBase.Almacenes.ConstantesAlmacen.typeVar;
import me.khmdev.APIBase.Almacenes.SQLPlayerData;
import me.khmdev.APIBase.Almacenes.varSQL;
import me.khmdev.APIEconomy.Own.APIEconomy;

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
			p.sendMessage("10 kills recibes 10 coins Extras!!");
			APIEconomy.addCash(p.getName(), 10);
			break;
		case 100:
			p.sendMessage("First Hundred recibes 100 coins Extras!!");
			APIEconomy.addCash(p.getName(), 100);
			break;
		case 500:
			p.sendMessage("Por la mitad recibes 250 coins extras!!");
			APIEconomy.addCash(p.getName(), 250);
			break;
		case 1000:
			p.sendMessage("finalmente Mil! Felicidades! +1000 coins!!");
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
			players.put(n, getKills(n));
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
			ResultSet r = SQLPlayerData.getPlayer(name);

			try {
				if (r.next()) {
					s = r.getInt("killStreack");
				}
			} catch (SQLException e1) {
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
