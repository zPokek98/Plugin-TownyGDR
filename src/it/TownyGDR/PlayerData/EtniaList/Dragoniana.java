/**
 * 
 */
package it.TownyGDR.PlayerData.EtniaList;

import java.util.ArrayList;

import it.TownyGDR.PlayerData.Etnia;
import it.TownyGDR.PlayerData.EtniaList.Casate.CasataType;
import it.TownyGDR.PlayerData.EtniaList.Casate.Ardese.ArdeseCasate;
import it.TownyGDR.PlayerData.EtniaList.Casate.Dragoniana.DragonianaCasata;

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
public class Dragoniana extends Etnia{
	
	private static String nome = "Dragoniana";
	private static ArrayList<CasataType> casate = new ArrayList<CasataType>();
	static{
		casate.add(DragonianaCasata.Dandu);
		casate.add(DragonianaCasata.Dragoy);
		casate.add(DragonianaCasata.Drengot);
		casate.add(DragonianaCasata.Hol);
		casate.add(DragonianaCasata.Petrix);
		casate.add(DragonianaCasata.Renix);
		casate.add(DragonianaCasata.Roy);
		casate.add(DragonianaCasata.Trevor);
		casate.add(DragonianaCasata.Zamputor);
	}

	public static ArrayList<CasataType> getCasate(){
		return casate;
	}
	
	public static String getNameEtnia() {
		return nome;
	}

	public Dragoniana() {
		this.name = nome;
		this.desc = "Etnia Dragoniana";
		
	}
}
