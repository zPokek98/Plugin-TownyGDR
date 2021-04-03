/**
 * 
 */
package it.TownyGDR.PlayerData;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;

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
	
	//Variabili per tutte le etnie
	protected String name;
	protected String desc;
	
	protected ArrayList<CasataType> casate;
	
	
	protected Etnia() {
		this.name = null;
		this.desc = null;
		this.casate = null;
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
	public ArrayList<CasataType> getCasate(){
		return this.casate;
	}
	
	public void save(ConfigurationSection database){
		// TODO Auto-generated method stub
		
	}

	public static Etnia load(ConfigurationSection database){
		return null;
		// TODO Auto-generated method stub
		
	}
	
}
