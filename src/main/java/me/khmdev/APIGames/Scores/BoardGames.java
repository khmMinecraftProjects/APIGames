package me.khmdev.APIGames.Scores;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import me.khmdev.APIAuxiliar.ScoreBoard.Board;
import me.khmdev.APIAuxiliar.ScoreBoard.FunctorString;
import me.khmdev.APIAuxiliar.ScoreBoard.getStringConst;
import me.khmdev.APIGames.Partidas.IPartida;

public abstract class BoardGames extends Board {
	protected Scoreboard board=Bukkit.getScoreboardManager().getNewScoreboard();
	public BoardGames(final IPartida p){
		super(p.getName());
		if(p.getTime()>0){
		title=new FunctorString() {
			
			@Override
			public String getString(Player pl) {
				long sec=p.getTimeFalta()/1000;
				String s=p.getName()+" "+((int)sec/60)+":"+((int)sec%60);
				return s.substring(0, Math.min(15,s.length()));
			}
		};}
		else{
			title=new getStringConst(p.getName());
		}
		
	}
	public Scoreboard getBoard(Player p) {
		return board;
	}
	public void addScoreBoard(Player pl) {
		pl.setScoreboard(board);
	}

	public void DeSpawnBoard() {
		board.clearSlot(DisplaySlot.SIDEBAR);
	}



}
