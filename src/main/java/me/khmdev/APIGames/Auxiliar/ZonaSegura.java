package me.khmdev.APIGames.Auxiliar;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CItems;
import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.APIBase.API;
import me.khmdev.APIGames.Partidas.Partida;
import me.khmdev.APIMaps.APIM;
import me.khmdev.APIMaps.Auxiliar.Map;
import me.khmdev.APIMaps.Auxiliar.Zona;

public class ZonaSegura extends Zona {
	private boolean completo=false, regenerando;
	protected List<Location> R;
	protected Location sign;
	protected Location ultima;
	protected Map map;
	protected Partida partida;
	protected CustomSignGames csign;

	public ZonaSegura(Partida p, Map m) {
		super(m.getZona().getLocA(), m.getZona().getLocB());
		setPerms((byte) 127);	
		w=a.getWorld();
		map = m;
		partida = p;
		completo = false;
		R = new LinkedList<Location>();
		APIM.getInstance().cargaMapa(map, null);
		map.getZona().setPerms((byte) 127);
		
		APIM.getInstance()
		.AsegurarZona(p.getName(), map.getZona());
		regenerando = true;
	}

	public ZonaSegura(Partida p) {
		setPerms((byte) 127);

		partida = p;
	}

	public void setMap(Map m) {
		a = m.getZona().getLocA();
		b = m.getZona().getLocB();
		a = new Location(a.getWorld(), Math.min(a.getX(), b.getX()), Math.min(
				a.getY(), b.getY()), Math.min(a.getZ(), b.getZ()));
		b = new Location(a.getWorld(), Math.max(a.getX(), b.getX()), Math.max(
				a.getY(), b.getY()), Math.max(a.getZ(), b.getZ()));
		x = (int) (b.getX() - a.getX());
		y = (int) (b.getY() - a.getY());
		z = (int) (b.getZ() - a.getZ());
		w=a.getWorld();
		setPerms((byte) 127);
		map.getZona().setPerms((byte) 127);
		map = m;
		completo = false;
		R = new LinkedList<Location>();
		APIM.getInstance().cargaMapa(map, null);
		APIM.getInstance()
		.AsegurarZona(partida.getName(), map.getZona());
		regenerando = true;
	}

	public boolean Completo() {
		return completo;

	}

	public Location SpawnZone() {
		return R.size()==0?null:R.get((int) (Math.random() * R.size()));
	}

	public boolean rVacio() {
		return R.size() == 0 || sign == null;
	}

	public boolean getFreeZone() {
		if (regenerando) {
			if (map.isReparando()) {
				return true;
			} else {
				regenerando = false;
				if (sign != null) {
					createSign(sign);
				}
				APIM.getInstance()
						.AsegurarZona(map.getName(), map.getZona());
			}
		}
		World w = getWorld();
		Location l = new Location(w, 0, 0, 0);
		if (R.size() != 0) {
			l.setX(ultima.getX());
			l.setY(ultima.getY());
			l.setZ(ultima.getZ() + 1);
		} else {

			l.setX(minX());
			l.setY(minY());
			l.setZ(minZ());

		}

		for (int x = (int) l.getX(); x < maxX(); x++) {
			for (int y = (int) l.getY(); y < maxY(); y++) {
				for (int z = (int) l.getZ(); z < maxZ(); z++) {
					signal(x, y, z, w);
					if (AuxPlayer.isPossibleSpawn(w, x, y, z)) {
						ultima = new Location(w, x, y, z);
						R.add(ultima);
						return false;
					}
				}
				l.setZ(minZ());
			}
			l.setY(minY());
		}
		ultima = new Location(w, l.getX(), l.getY(), l.getZ());
		completo = true;
		return completo;
	}

	private void signal(int x, int y, int z, World w) {
		if (sign != null) {
			return;
		}

		if (w.getBlockAt(x + 1, y, z).getType() == Material.AIR
				&& w.getBlockAt(x + 1, y - 1, z).getType() == Material.AIR
				&& w.getBlockAt(x + 1, y - 2, z).getType() != Material.AIR) {
			Block b = w.getBlockAt(x + 1, y, z);
			b.setType(Material.WALL_SIGN);
			sign = b.getLocation();
		} else if (w.getBlockAt(x - 1, y, z).getType() == Material.AIR
				&& w.getBlockAt(x - 1, y - 1, z).getType() == Material.AIR
				&& w.getBlockAt(x - 1, y - 2, z).getType() != Material.AIR) {
			Block b = w.getBlockAt(x - 1, y, z);
			b.setType(Material.WALL_SIGN);
			sign = b.getLocation();
		} else if (w.getBlockAt(x, y, z + 1).getType() == Material.AIR
				&& w.getBlockAt(x, y - 1, z + 1).getType() == Material.AIR
				&& w.getBlockAt(x, y - 2, z + 1).getType() != Material.AIR) {
			Block b = w.getBlockAt(x, y, z + 1);
			b.setType(Material.WALL_SIGN);
			sign = b.getLocation();
		} else if (w.getBlockAt(x, y, z - 1).getType() == Material.AIR
				&& w.getBlockAt(x, y - 1, z - 1).getType() == Material.AIR
				&& w.getBlockAt(x, y - 2, z - 1).getType() != Material.AIR) {
			Block b = w.getBlockAt(x, y, z - 1);
			b.setType(Material.WALL_SIGN);
			sign = b.getLocation();
		}
	}

	public void actualizarSign(String a, String b, String c, String d) {
		if (sign != null) {
			if (!(sign.getBlock().getType().equals(Material.WALL_SIGN) || sign
					.getBlock().getType().equals(Material.SIGN_POST))) {
				createSign(sign);
				return;
			}
			Sign s = (Sign) sign.getBlock().getState();
			s.setLine(0, a);
			s.setLine(1, b);
			s.setLine(2, c);
			s.setLine(3, d);
			s.update();
		}
	}

	public Map getMap() {
		return map;
	}

	public Sign getSign() {
		if (sign == null
				|| !((sign.getBlock().getType().equals(Material.WALL_SIGN) || sign
						.getBlock().getType().equals(Material.SIGN_POST)))) {
			return null;
		}
		return (Sign) sign.getBlock().getState();
	}

	public void createSign(Location a) {
		if (getSign() != null) {
			partida.getGame().removeSenal(getSign());
			CItems.removeSign(sign);
		}
		a.getBlock().setType(Material.WALL_SIGN);
		ajustSign(a);
		sign = a;


		if (getSign() != null) {
			API.setMetadata(getSign(), "Game",partida.getGame().getName());
			API.setMetadata(getSign(), "Partida",partida.getName());
			csign = new CustomSignGames(partida.getGame(), getSign());
			CItems.addSign(csign);
		}
	}

	@SuppressWarnings("deprecation")
	private void ajustSign(Location b) {
		Location b2 = b.clone();
		if (!(b2.add(-1, 0, 0).getBlock().getType().equals(Material.AIR))) {
			b.getBlock().setData((byte) 5);
		} else if (!(b2.add(2, 0, 0).getBlock().getType().equals(Material.AIR))) {

			b.getBlock().setData((byte) 4);
		} else if (!(b2.add(-1, 0, -1).getBlock().getType()
				.equals(Material.AIR))) {

			b.getBlock().setData((byte) 3);
		} else if (!(b2.add(0, 0, 2).getBlock().getType().equals(Material.AIR))) {

			b.getBlock().setData((byte) 2);
		}

	}
}
