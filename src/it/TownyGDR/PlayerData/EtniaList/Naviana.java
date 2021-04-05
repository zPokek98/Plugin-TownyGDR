/**
 * 
 */
package it.TownyGDR.PlayerData.EtniaList;

import java.util.ArrayList;

import it.TownyGDR.PlayerData.Etnia;
import it.TownyGDR.PlayerData.EtniaList.Casate.CasataType;
import it.TownyGDR.PlayerData.EtniaList.Casate.Naviana.NavianaCasata;

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
public class Naviana extends Etnia{
	
	private static String nome = "Naviana";
	private static ArrayList<CasataType> casate = new ArrayList<CasataType>();
	static{
		casate.add(NavianaCasata.Darnis);
		casate.add(NavianaCasata.Egeril);
		casate.add(NavianaCasata.Fen);
		casate.add(NavianaCasata.Forven);
		casate.add(NavianaCasata.Leowell);
		casate.add(NavianaCasata.River);
		casate.add(NavianaCasata.Singer);
	}

	public static ArrayList<CasataType> getCasate(){
		return casate;
	}
	
	public static String getNameEtnia() {
		return nome;
	}
	
	public Naviana() {
		this.name = nome;
		this.desc = "Etnia Naviana";
		
	}
}
