/**
 * 
 */
package it.TownyGDR.PlayerData.EtniaList;

import java.util.ArrayList;

import it.TownyGDR.PlayerData.Etnia;
import it.TownyGDR.PlayerData.EtniaList.Casate.CasataType;
import it.TownyGDR.PlayerData.EtniaList.Casate.Ardese.ArdeseCasate;
import it.TownyGDR.PlayerData.EtniaList.Casate.Beriana.BerianaCasata;

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
public class Beriana extends Etnia{
	
	private static String nome = "Beriana";
	private static ArrayList<CasataType> casate = new ArrayList<CasataType>();
	static{
		casate.add(BerianaCasata.Gwael);
		casate.add(BerianaCasata.Hemilock);
		casate.add(BerianaCasata.Mady);
		casate.add(BerianaCasata.Rhoben);
	}

	public static ArrayList<CasataType> getCasate(){
		return casate;
	}
	
	public static String getNameEtnia() {
		return nome;
	}

	public Beriana() {
		this.name = nome;
		this.desc = "Etnia Beriana";
		
	}
}
