/**
 * 
 */
package it.TownyGDR.PlayerData.EtniaList;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;

import it.TownyGDR.PlayerData.Etnia;
import it.TownyGDR.PlayerData.EtniaList.Casate.CasataType;
import it.TownyGDR.PlayerData.EtniaList.Casate.Ardese.ArdeseCasate;
import it.TownyGDR.Util.Exception.ExceptionLoad;

/*********************************************************************
 * @author: Elsalamander
 * @data: 19 mar 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * 
 *********************************************************************/
public class Ardese extends Etnia{
	
	private static String nome = "Ardese";
	private static ArrayList<CasataType> casate = new ArrayList<CasataType>();
	static{
		casate.add(ArdeseCasate.Brial);
		casate.add(ArdeseCasate.Maedon);
		casate.add(ArdeseCasate.Nekromount);
		casate.add(ArdeseCasate.Tunnor);
	}

	public static ArrayList<CasataType> getCasate(){
		return casate;
	}
	
	public static String getNameEtnia() {
		return nome;
	}
	
	public Ardese() {
		this.name = nome;
		this.desc = "Etnia Ardese";
	}
	
	
	
}
