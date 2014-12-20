package me.khmdev.APIGames.MarcadoresSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import me.khmdev.APIBase.API;
import me.khmdev.APIBase.Almacenes.sql.AlmacenSQL;
import me.khmdev.APIBase.Almacenes.sql.Consulta;
import me.khmdev.APIBase.Almacenes.sql.FieldSQL;
import me.khmdev.APIBase.Almacenes.sql.FieldSQLChange;
import me.khmdev.APIGames.Auxiliar.Jugador;
import me.khmdev.APIGames.Games.IGame;

public class SQLGame {
	private AlmacenSQL sql;
	private IGame game;

	public SQLGame(IGame g) {
		sql = API.getInstance().getSql();
		game = g;
		addGame();
	}

	private void addGame() {

		if (!sql.existId(SQLConstant.tablaJuegos, new FieldSQL(
				SQLConstant.celdaJuegoID, game.getName()))) {
			sql.createField(SQLConstant.tablaJuegos, new FieldSQL(
					SQLConstant.celdaJuegoID, game.getName()), new FieldSQL(
					SQLConstant.celdaJuegoAlias, game.getAlias()));
		}
	}

	private boolean addToVar(String pl, int i, String n) {
		if (!sql.existId(SQLConstant.tablaMarcadores, new FieldSQL(
				SQLConstant.celdaUsuarioID, pl), new FieldSQL(
				SQLConstant.celdaUsuarioJuego, game.getName()))) {

			crearUser(pl, new FieldSQL(n, i));
		}

		Consulta c = sql.getValue(SQLConstant.tablaMarcadores, new FieldSQL(
				SQLConstant.celdaUsuarioID, pl), new FieldSQL(
				SQLConstant.celdaUsuarioJuego, game.getName()));
		if(c==null){return false;}
		try {
			ResultSet res=c.getR();

			int num = -1;

			while (res != null && res.next()) {

				if (res.getString(SQLConstant.celdaUsuarioID).equalsIgnoreCase(
						pl)) {

					num = res.getInt(n);
				}
			}

			sql.changeData(game.getName(), new FieldSQLChange(n, num + i + "",
					new FieldSQL(SQLConstant.celdaUsuarioID, "'" + pl + "'")));

			return true;
		} catch (SQLException e) {
			return false;
		}finally{
			c.close();
		}
	}

	public boolean punto(String pl, int i) {
		return addToVar(pl, i, SQLConstant.celdaUsuarioPuntos);
	}

	public boolean kill(String pl, int i) {
		return addToVar(pl, i, SQLConstant.celdaUsuarioKills);
	}

	public boolean deaths(String pl, int i) {
		return addToVar(pl, i, SQLConstant.celdaUsuarioDeaths);
	}

	public boolean ganada(String pl, int i) {
		return addToVar(pl, i, SQLConstant.celdaUsuarioGanadas);
	}

	public boolean perdida(String pl, int i) {
		return addToVar(pl, i, SQLConstant.celdaUsuarioPerdidas);
	}

	public void crearUser(String pl) {
		sql.createField(SQLConstant.tablaMarcadores, new FieldSQL(
				SQLConstant.celdaUsuarioID, pl), new FieldSQL(
				SQLConstant.celdaUsuarioJuego, game.getName()));
	}

	public void crearUser(String pl, FieldSQL c) {
		sql.createField(SQLConstant.tablaMarcadores, new FieldSQL(
				SQLConstant.celdaUsuarioID, pl), new FieldSQL(
				SQLConstant.celdaUsuarioJuego, game.getName()), c);
	}

	public void resultadoPartida(String pl, int ganador) {
		if (ganador == 1) {
			ganada(pl, 1);
		} else if (ganador == 0) {
			perdida(pl, 1);
		}
	}

	public void actualizar(Jugador j) {
		String pl = j.getPlayer().getName(), g = game.getName();

		int gan = j.isGanador() == 1 ? 1 : 0, per = j.isGanador() != 1 ? 1 : 0, kil = j
				.getKills(), dea = j.getDeaths(), pun = j.getPuntuacion();
		Consulta c = sql.getValue(SQLConstant.tablaMarcadores, new FieldSQL(
				SQLConstant.celdaUsuarioID, pl), new FieldSQL(
				SQLConstant.celdaUsuarioJuego, g));
		if(c==null){return;}
		
		try {
			ResultSet r=c.getR();

			if (r.next()) {
				gan += r.getInt(SQLConstant.celdaUsuarioGanadas);
				per += r.getInt(SQLConstant.celdaUsuarioPerdidas);
				kil += r.getInt(SQLConstant.celdaUsuarioKills);
				dea += r.getInt(SQLConstant.celdaUsuarioDeaths);
				pun += r.getInt(SQLConstant.celdaUsuarioPuntos);
			}
			sql.changeData(
					SQLConstant.tablaMarcadores,
					new FieldSQLChange(Arrays.asList(new FieldSQL(
							SQLConstant.celdaUsuarioID, pl), new FieldSQL(
							SQLConstant.celdaUsuarioJuego, game.getName()),
							new FieldSQL(SQLConstant.celdaUsuarioGanadas, gan),
							new FieldSQL(SQLConstant.celdaUsuarioPerdidas, per),
							new FieldSQL(SQLConstant.celdaUsuarioKills, kil),
							new FieldSQL(SQLConstant.celdaUsuarioDeaths, dea),
							new FieldSQL(SQLConstant.celdaUsuarioPuntos, pun)),
							new FieldSQL(SQLConstant.celdaUsuarioID, pl),
							new FieldSQL(SQLConstant.celdaUsuarioJuego, game
									.getName())));
		} catch (Exception e) {
			sql.createField(SQLConstant.tablaMarcadores, new FieldSQL(
					SQLConstant.celdaUsuarioID, pl), new FieldSQL(
					SQLConstant.celdaUsuarioJuego, game.getName()),
					new FieldSQL(SQLConstant.celdaUsuarioGanadas, gan),
					new FieldSQL(SQLConstant.celdaUsuarioPerdidas, per),
					new FieldSQL(SQLConstant.celdaUsuarioKills, kil),
					new FieldSQL(SQLConstant.celdaUsuarioDeaths, dea),
					new FieldSQL(SQLConstant.celdaUsuarioPuntos, pun));
		}finally{
			c.close();
		}
		
	}

}
