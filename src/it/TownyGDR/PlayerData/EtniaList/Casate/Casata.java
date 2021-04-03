/**
 * 
 */
package it.TownyGDR.PlayerData.EtniaList.Casate;

import java.io.IOException;

import org.bukkit.configuration.ConfigurationSection;

import it.TownyGDR.Util.Exception.ExceptionLoad;

/*********************************************************************
 * @author: Elsalamander
 * @data: 10 mar 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Descrizione generale delle casate
 * 
 *********************************************************************/
public abstract class Casata{

	//Variabili per tutte le etnie
	protected String name;
	protected String desc;
	
	
	protected Casata() {
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
	
	public void save(ConfigurationSection database) {
		// TODO Auto-generated method stub
		
	}

	public static Casata load(ConfigurationSection database) {
		return null;
		// TODO Auto-generated method stub
		
	}
}
