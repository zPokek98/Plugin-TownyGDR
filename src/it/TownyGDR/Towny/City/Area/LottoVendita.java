/**
 * 
 */
package it.TownyGDR.Towny.City.Area;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import it.TownyGDR.TownyGDR;
import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.City.Membri.Membro;
import it.TownyGDR.Towny.Zone.ElementoArea;
import it.TownyGDR.Towny.Zone.Zona;
import it.TownyGDR.Util.Exception.ExceptionLoad;
import it.TownyGDR.Util.Save.Salva;

/*********************************************************************
 * @author: Elsalamander
 * @data: 27 mar 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Coppia Elemento d'area prezzo, pper i lotti
 * 
 * I lotti da vendere saranno salvati nel seguente modo
 * LottiDaVendere:
 * 					- x;z;prezzo
 * 					- x;z;prezzo
 * A mo di lista
 * 
 *********************************************************************/
public class LottoVendita{
	
	private ElementoArea ele;
	private double prezzo;
	private Membro sid;
	
	private Sign cartello;
	
	public LottoVendita(ElementoArea ele, double prezzo, Membro sindaco) {
		this.ele = ele;
		this.prezzo = prezzo;
		this.sid = sindaco;
		
		//Piazza un cartello in mezzo
		int x = ele.getX() * 16 + 7;
		int z = ele.getZ() * 16 + 7;
		World world = TownyGDR.getTownyWorld();
		Block b = world.getHighestBlockAt(x, z);
		
		Sign sign = null;
		
		if(b instanceof Sign) {
			// c'è gia un cartello, sovvrascrivi
			//BlockState signState = b.getState();
			//sign = (Sign) signState;
		}else{
			b = (new Location(world, x, world.getHighestBlockYAt(x, z) + 1, z)).getBlock();
			b.setType(Material.BIRCH_SIGN);
		}
		BlockState signState = b.getState();
		sign = (Sign) signState;
		Player p = PlayerData.getFromUUID(sindaco.getUUID()).getPlayer();
		
		sign.setLine(0, ChatColor.DARK_RED + 	"Lotto in vendita");
		sign.setLine(1,							"Da " + p.getName());
		sign.setLine(2, 						"Per: " + prezzo);
		
		sign.update();
		
		this.cartello = sign;
	}
	

	/**
	 * @return the sid
	 */
	public Membro getSid() {
		return sid;
	}


	/**
	 * @return the cartello
	 */
	public Sign getCartello() {
		return cartello;
	}


	public boolean equals(LottoVendita lv) {
		return ele.equals(lv.getEle());
	}
	
	/**
	 * Ritorna l'elemento d'area della vendita
	 * @return the ele
	 */
	public ElementoArea getEle() {
		return ele;
	}


	/**
	 * Ritorna il prezzo
	 * @return the prezzo
	 */
	public double getPrezzo() {
		return prezzo;
	}

	/**
	 * Salva i lotti da vendere
	 * @param config
	 */
	public static void save(ConfigurationSection config, ArrayList<LottoVendita> lotti){
		List<String> list = new ArrayList<String>(); 
		
		for(LottoVendita lv : lotti) {
			list.add(lv.ele.getX() + ";" + lv.ele.getZ() + ";" + lv.prezzo  + ";" + lv.sid.getUUID());
		}
		
		config.set("LottiDaVednere", null);  //cancella per rimpiazzare
		config.set("LottiDaVednere" , list);
	}

	/**
	 * @param config
	 * @return
	 */
	public static ArrayList<LottoVendita> load(ConfigurationSection config) {
		ArrayList<LottoVendita> lv = new ArrayList<LottoVendita>();
		
		List<String> strList = config.getStringList("LottiDaVednere");
		
		Scanner scan = null;
		for(String str : strList) {
			scan = new Scanner(str);
			scan.useDelimiter(";");
			try{
				int x = scan.nextInt();
				int z = scan.nextInt();
				double pr = scan.nextDouble();
				String uuidStr = scan.next();
				
				UUID uuid = UUID.fromString(uuidStr);
				City city = PlayerData.getFromUUID(uuid).getCity();
				
				lv.add(new LottoVendita(new ElementoArea(x,z), pr, city.getMembroByUUID(uuid)));
			}catch(NoSuchElementException e){
				//errore lettura
			}
		}
		if(scan != null) {
			scan.close();
		}
		return lv;
	}


	/**
	 * @param event
	 * @param sign
	 */
	public static void clickSign(PlayerInteractEvent event, Sign sign) {
		Location loc = sign.getLocation();
		Zona zon = Zona.getZonaByLocation(loc);
		City city = (City)zon.getLuogo();
		LottoVendita lv = city.getArea().LottoVenditagetByElementoArea(new ElementoArea(loc.getChunk()));
		
		Player p = event.getPlayer();
		Membro mem = city.getMembroByUUID(p.getUniqueId());
		if(mem.getLotto().compraLotto(lv, mem)) {
			p.sendMessage("Lotto comprato!");
		}else{
			p.sendMessage("Lotto NON comprato!");
		}
		
	}


}
