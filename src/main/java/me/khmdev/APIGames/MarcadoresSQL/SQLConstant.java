package me.khmdev.APIGames.MarcadoresSQL;

public class SQLConstant {
	public static final String tablaJuegos = "Juego",
			tablaMarcadores = "Marcador", celdaJuegoID = "id",
			celdaJuegoAlias = "Alias", celdaUsuarioID = "usuario",
			celdaUsuarioJuego = "juego", celdaUsuarioGanadas = "Ganadas",
			celdaUsuarioPerdidas = "Perdidas", celdaUsuarioPuntos = "Puntos",
			celdaUsuarioKills = "Kills", celdaUsuarioDeaths = "Deaths";
	public static final String[] sql = {
			"CREATE TABLE IF NOT EXISTS Juego (" + "id varchar(20),"
					+ "alias varchar(50) NOT NULL," + "PRIMARY KEY (id)"
					+ ")\n",
			"CREATE TABLE IF NOT EXISTS Marcador ("
					+ "usuario varchar(50) NOT NULL,"
					+ "juego varchar(20) NOT NULL,"
					+ "Ganadas int(11) DEFAULT 0,"
					+ "Perdidas int(11) DEFAULT 0,"
					+ "Puntos int(11) DEFAULT 0,"
					+ "Kills int(11) DEFAULT 0,"
					+ "Deaths int(11) DEFAULT 0,"
					+ "FOREIGN KEY ( usuario ) REFERENCES usuarios( usuario ) ON DELETE CASCADE ON UPDATE CASCADE,"
					+ "FOREIGN KEY ( juego ) REFERENCES Juego( id ) ON DELETE CASCADE ON UPDATE CASCADE,"
					+ "PRIMARY KEY ( usuario, juego )" + ");\n",
			"ALTER TABLE `usuarios` "
					+ "ADD killStreack int(11) default 0,"
					+ "\n" };
}
