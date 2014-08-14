package me.khmdev.APIGames.MarcadoresSQL;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.khmdev.APIBase.API;
import me.khmdev.APIBase.Almacenes.AlmacenSQL;
import me.khmdev.APIBase.Almacenes.FieldSQL;
import me.khmdev.APIBase.Almacenes.varSQL;
import me.khmdev.APIBase.Almacenes.ConstantesAlmacen.typeVar;
import me.khmdev.APIGames.Games.IGame;

public class SQLGame {
	private AlmacenSQL sql;
	private IGame game;
	public SQLGame(IGame g){
		sql=API.getInstance().getSql();
		game=g;
		createGame();
		addGame();
	}
	
	private void addGame(){
		if(!sql.existTable("juegos")){
			sql.createTab("juegos",
					new varSQL("id", typeVar.varchar,20),
					new varSQL("Alias", typeVar.varchar,50));
			sql.setUnique("juegos","id");
		}
		if(!sql.existId("juegos", "id", game.getName())){
		sql.createField("juegos", new FieldSQL("id", game.getName()),
				new FieldSQL("Alias", game.getAlias()));
		}
	}
	
	public boolean createGame(){
		if(sql.existTable(game.getName())){
			return false;
		}

		sql.createTab(game.getName(),
				new varSQL("User", typeVar.varchar,50),
				new varSQL("Ganadas", typeVar.inteteger),
				new varSQL("Perdidas", typeVar.inteteger),
				new varSQL("Puntos", typeVar.inteteger),
				new varSQL("Kills", typeVar.inteteger),
				new varSQL("Deaths", typeVar.inteteger));
		
		sql.setUnique(game.getName(),"User");
		return true;
	}
	
	public boolean perdida(String pl,int i){
		if(!sql.existId(game.getName(), "User", pl)){
			crearUser(pl);
		}
		ResultSet res = sql.getValue(game.getName(), "User", pl);


		try {
			int num=-1;
			while (res!=null&&res.next()) {
				if(res.getString("User").equalsIgnoreCase(pl)){

				num=res.getInt("Perdidas");}
			}

			sql.changeData(game.getName(),
					"User", "'"+pl+"'", "Perdidas", num+i+"");

			return true;
		}catch(SQLException e){
			return false;
		}
	}
	public boolean punto(String pl,int i){
		if(!sql.existId(game.getName(), "User", pl)){

			crearUser(pl);
		}
		
		ResultSet res = sql.getValue(game.getName(), "User", pl);


		try {
			int num=-1;
			
			while (res!=null&&res.next()) {

				if(res.getString("User").equalsIgnoreCase(pl)){

				num=res.getInt("Puntos");}
			}

			sql.changeData(game.getName(),
					"User", "'"+pl+"'", "Puntos", num+i+"");
			return true;
		}catch(SQLException e){
			return false;
		}
	}
	public boolean kill(String pl,int i){
		if(!sql.existId(game.getName(), "User", pl)){

			crearUser(pl);
		}
		
		ResultSet res = sql.getValue(game.getName(), "User", pl);


		try {
			int num=-1;
			
			while (res!=null&&res.next()) {

				if(res.getString("User").equalsIgnoreCase(pl)){

				num=res.getInt("Kills");}
			}

			sql.changeData(game.getName(),
					"User", "'"+pl+"'", "Kills", num+i+"");
			return true;
		}catch(SQLException e){
			return false;
		}
	}
	public boolean deaths(String pl,int i){
		if(!sql.existId(game.getName(), "User", pl)){

			crearUser(pl);
		}
		
		ResultSet res = sql.getValue(game.getName(), "User", pl);


		try {
			int num=-1;
			
			while (res!=null&&res.next()) {

				if(res.getString("User").equalsIgnoreCase(pl)){

				num=res.getInt("Deaths");}
			}

			sql.changeData(game.getName(),
					"User", "'"+pl+"'", "Deaths", num+i+"");
			return true;
		}catch(SQLException e){
			return false;
		}
	}
	
	public boolean ganada(String pl,int i){
		
		if(!sql.existId(game.getName(), "User", pl)){

			crearUser(pl);
		}
		
		ResultSet res = sql.getValue(game.getName(), "User", pl);


		try {
			int num=-1;
			
			while (res!=null&&res.next()) {

				if(res.getString("User").equalsIgnoreCase(pl)){

				num=res.getInt("Ganadas");}
			}

			sql.changeData(game.getName(),
					"User", "'"+pl+"'", "Ganadas", num+i+"");
			return true;
		}catch(SQLException e){
			return false;
		}
	}
	public void crearUser(String pl){
		sql.createField(game.getName(), new FieldSQL("User", pl),
				new FieldSQL("Ganadas", 0),new FieldSQL("Perdidas",0));
	}

	public void resultadoPartida(String pl, int ganador) {
		if(ganador==1){
			ganada(pl, 1);
		}else if(ganador==0){
			perdida(pl, 1);
		}
	}

}
