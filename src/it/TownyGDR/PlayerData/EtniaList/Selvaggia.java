/**
 * 
 */
package it.TownyGDR.PlayerData.EtniaList;

import java.util.ArrayList;

import it.TownyGDR.PlayerData.Etnia;
import it.TownyGDR.PlayerData.EtniaList.Casate.CasataType;
import it.TownyGDR.PlayerData.EtniaList.Casate.Ardese.ArdeseCasate;

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
public class Selvaggia extends Etnia{
	
	private static String nome = "Selvaggia";
	private static ArrayList<CasataType> casate = new ArrayList<CasataType>();
	static{
		//...
	}

	public static ArrayList<CasataType> getCasate(){
		return casate;
	}
	
	public static String getNameEtnia() {
		return nome;
	}

	public Selvaggia() {
		this.name = nome;
		this.desc = "Etnia Selvaggia";

	}
}
