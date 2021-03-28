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

import org.bukkit.configuration.ConfigurationSection;

import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.City.Membri.Membro;
import it.TownyGDR.Towny.Zone.ElementoArea;
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
	
	public LottoVendita(ElementoArea ele, double prezzo, Membro sindaco) {
		this.ele = ele;
		this.prezzo = prezzo;
		this.sid = sindaco;
	}
	

	/**
	 * @return the sid
	 */
	public Membro getSid() {
		return sid;
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


}
