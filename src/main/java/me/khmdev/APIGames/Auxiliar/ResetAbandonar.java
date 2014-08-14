package me.khmdev.APIGames.Auxiliar;

import java.util.HashMap;

public class ResetAbandonar {

	private static HashMap<String, Long> times=
			new HashMap<>();
	private static final long time=300000;
	public static void leave(String s){
		times.put(s, System.currentTimeMillis());
	}
	public static boolean canLeave(String s){
		return !times.containsKey(s)
				||(System.currentTimeMillis()-times.get(s))
							>=time;
	}
	public static long time(String s){
		return canLeave(s)?0:
			time-(System.currentTimeMillis()-times.get(s));
	}
	
	public static String timeString(String s){
		long t=time(s);
		int min=((int)((t/1000)/60)),sec=((int)((t/1000)%60));
		return (min>9?min:"0"+min)+":"+
		(sec>9?sec:"0"+sec);
	}	
	
	
}
