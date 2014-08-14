package me.khmdev.APIGames.lang;
import java.util.*;

import org.bukkit.ChatColor;

public class txt_es_ES extends ListResourceBundle 
{ 
   public Object[][] getContents() 
   {
      return contenido;
   }
   private Object[][] contenido = { 
		   {"send_all", "%clr%%Game%:"+ChatColor.WHITE+" %msg%"}, //  (%Partida%)
		   {"send_eq", "%clr%%Game%:"+ChatColor.WHITE+" %msg%"}, // (%Partida%)
		   {"fin_abandono", "La partida finalizara por abandono"},
		   {"login_player", "Se ha unido %Player%"},		  
		   {"fin_winner", "Ha ganado el equipo %Winner%"},
		   {"fin_winnerPlayer", "Ha ganado %Winner%"},
		   {"fin_empate", "Ha habido un empate"},
		   {"selector_team", "Selector de equipo %Game%"},
		   {"select_team", "Elije equipo"},
		   {"selected_team", "Te has unido al equipo %Team%"},
		   {"no_perms", "Permisos insuficientes"},
		   {"start_up", "Empieza ya"},
		   {"bossBar_left",  "Tiempo para el final"},
		   {"bossBar_end",  "Partida finalizada"},
		   {"time_start",  "Empezara en %Time% segundos"},
		   {"coins_player", "Has ganado "+ChatColor.RED+"%Coins% coins"},
		   {"send_selective_all", "%clr%%Player%(Todos): "+ ChatColor.WHITE +"%msg%"},
		   {"send_selective_team", "%clr%%Player%(Equipo): "+ ChatColor.WHITE +"%msg%"},

		   

   };
}