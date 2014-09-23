package me.khmdev.APIGames;

import me.khmdev.APIGames.MarcadoresSQL.ControlKills;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class init extends JavaPlugin {
	private APIG apig;

	public void onEnable() {
		if (!hasPluging("APIBase") || !hasPluging("APIEconomy")
				|| !hasPluging("APIAuxiliar") || !hasPluging("APIMaps")) {
			getLogger().severe(
					getName()
							+ " se desactivo debido ha que no encontro la API");
			setEnabled(false);
			return;
		}
		apig = new APIG(this);
		ControlKills.init(this);
		
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (apig.onCommand(sender, cmd, label, args)) {
			return true;
		}

		return false;
	}

	private static boolean hasPluging(String s) {
		try {
			return Bukkit.getPluginManager().getPlugin(s).isEnabled();
		} catch (Exception e) {

		}
		return false;
	}

	public void onDisable() {
		
		if (apig != null) {
			apig.onDisable();
			ControlKills.shutdown();
		}
	}
}
