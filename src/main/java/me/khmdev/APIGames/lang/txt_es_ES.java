package me.khmdev.APIGames.lang;

import java.util.*;

import me.khmdev.APIEconomy.ConstantesEconomy;

import org.bukkit.ChatColor;

public class txt_es_ES extends ListResourceBundle {
	public Object[][] getContents() {
		return contenido;
	}

	private Object[][] contenido = {
			{ "send_all", "%clr%%Game%:&F %msg%" }, // (%Partida%)
			{ "send_eq", "%clr%%Game%:&F %msg%" }, // (%Partida%)
			{ "fin_abandono", "La partida finalizara por abandono" },
			{ "login_player", "Se ha unido %Player%" },
			{ "fin_winner", "Ha ganado el equipo %Winner%" },
			{ "fin_winnerPlayer", "Ha ganado %Winner%" },
			{ "fin_empate", "Ha habido un empate" },
			{ "selector_team", "Selector de equipo %Game%" },
			{ "select_team", "Elije equipo" },
			{ "selected_team", "Te has unido al equipo %Team%" },
			{ "no_perms", "Permisos insuficientes" },
			{ "start_up", "Empieza ya" },
			{ "bossBar_left", "Tiempo para el final" },
			{ "bossBar_end", "Partida finalizada" },
			{ "time_start", "Empezara en %Time% segundos" },
			{ "send_selective_all",
					"%clr%%Player%(Todos):&F %msg%" },
			{ "send_selective_team",
					"%clr%%Player%(Equipo):&F %msg%" },
			{ "ShopItem.price", "Precio: %price%%UM%" },
			{ "ShopItem.buy", "Has comprado %item% por %price%%UM%" },
			{ "ShopItem.noMoney", "Fondos insuficientes" },

			{ "GestorDeVentajas.use", "Usada ventaja: %ventaja%" },
			{ "GestorDeVentajas.name", "Ventajas %game%" },
			
			{ "APIG.leave", "Has abandonado la partida"},
			{ "APIG.leaveWait", "No puedes abandonar hasta dentro de %time% segundos"},
			{ "APIG.noGame", "No estas en ninguna partida"},

			{ "CustomSign.inOther", "Ya esta en otra partida" },
			{ "CustomSign.inThis", "Ya esta en la partida %partida%"},
			{ "CustomSign.inOther", "Ya esta en la partida %partida%"},
			{"CustomSign.error", "No se ha podido entrar a la partida %partida%"},
			
			{"ControlKills.10", "10 kills recibes 10 coins Extras!!"},
			{"ControlKills.100", "First Hundred recibes 100 coins Extras!!"},
			{"ControlKills.500", "Por la mitad recibes 250 coins extras!!"},
			{"ControlKills.1000", "finalmente Mil! Felicidades! +1000 coins!!"},
			
			
			{ "ItemVentage.buyYet", "Ya lo has comprado" },
			{ "ItemKitShop.useKit", "Se usara el kit %kit%" },
			{ "ItemKitShop.noPerms", "No tienes permiso para comprar este kit" },
			{ "ItemKitShop.buy", "Has comprado el kit %kit% por %price%%UM%" },

			{ "partida.endGame1", "%cl%/--------------------------------\\" },
			{ "partida.endGame2", "%cl%Coins por participar:        +5" },
			{ "partida.endGame3",
					"%cl%Coins por kill:           %kcoins%x2=+%ktot%" },
			{ "partida.endGame4",
					"%cl%Coins por muerte:  %dcoins%x(-1=-%dtot%)" },
			{ "partida.endGame5",
					"%cl%Coins por punto:       %pcoins%x3=+%ptot%" },
			{ "partida.endGamew", "%cl%Coins por ganar:            +10" },
			{ "partida.endGame6",
					"%cl%Has ganado &C%Coins% coins" },
			{ "partida.endGame7", "%cl%\\--------------------------------/" },

	};
}