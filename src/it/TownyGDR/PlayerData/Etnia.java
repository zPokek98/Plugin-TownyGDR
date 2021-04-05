/**
 * 
 */
package it.TownyGDR.PlayerData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;

import it.TownyGDR.PlayerData.EtniaList.Ardese;
import it.TownyGDR.PlayerData.EtniaList.Beriana;
import it.TownyGDR.PlayerData.EtniaList.Dragoniana;
import it.TownyGDR.PlayerData.EtniaList.Naviana;
import it.TownyGDR.PlayerData.EtniaList.Selvaggia;
import it.TownyGDR.PlayerData.EtniaList.Shariana;
import it.TownyGDR.PlayerData.EtniaList.Casate.CasataType;
import it.TownyGDR.Util.Exception.ExceptionLoad;

/*********************************************************************
 * @author: Elsalamander
 * @data: 10 mar 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Descrizione generale della etnia da assegnare al player
 * 
 * La etnia al di sotto ha una casata.
 * La casata è descritta in modo generale è ogni etnia ha:
 * 
 * - Nome
 * - Descrizione
 * - ListCasate
 * 
 * 
 * 
 *********************************************************************/
public abstract class Etnia{
	
	protected static Map<String, ArrayList<CasataType>> map = new TreeMap<String, ArrayList<CasataType>>();
	static{
		map.put(Ardese.getNameEtnia(), Ardese.getCasate());
		map.put(Beriana.getNameEtnia(), Beriana.getCasate());
		map.put(Dragoniana.getNameEtnia(), Dragoniana.getCasate());
		map.put(Naviana.getNameEtnia(), Naviana.getCasate());
		map.put(Selvaggia.getNameEtnia(), Selvaggia.getCasate());
		map.put(Shariana.getNameEtnia(), Shariana.getCasate());
		
	}
	
	//Variabili per tutte le etnie
	protected String name;
	protected String desc;
	
	protected static ArrayList<CasataType> casate;
	
	
	protected Etnia() {
		this.name = null;
		this.desc = null;
	}
	
	/**
	 * Nome della etnia
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Ritorna la descrizione della etnia
	 * @return
	 */
	public String getDescription() {
		return this.desc;
	}
	
	/**
	 * Ritorna le casate che stanno sotto a questa etnia
	 * @return
	 */
	public static ArrayList<CasataType> getCasateByEtnia(Etnia etnia){
		return map.get(etnia.getName());
	}
	
	/**
	 * Ritorna un oggetto Etnia tramite il nome
	 * @param str
	 * @return
	 */
	public static Etnia getByName(String str) {
		switch(str) {
			case "Ardese": 		return new Ardese();
			case "Beriana": 	return new Beriana();
			case "Dragoniana": 	return new Dragoniana();
			case "Naviana": 	return new Naviana();
			case "Selvaggia": 	return new Selvaggia();
			case "Shariana": 	return new Shariana();
		}
		return null;
	}
	
	public void save(ConfigurationSection config){
		config.set("Etnia", this.name);
	}

	public static Etnia load(ConfigurationSection config){
		String str = config.getString("Etnia", null);
		if(str != null) {
			return Etnia.getByName(str);
		}
		return null;
	}
	
	public static Map<String, ArrayList<CasataType>> getMapEtnia(){
		return map;
	}
	
}
