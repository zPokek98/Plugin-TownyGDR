/**
 * 
 */
package it.TownyGDR.PlayerData.EtniaList;

import java.util.ArrayList;

import it.TownyGDR.PlayerData.Etnia;
import it.TownyGDR.PlayerData.EtniaList.Casate.CasataType;
import it.TownyGDR.PlayerData.EtniaList.Casate.Dragoniana.DragonianaCasata;
import it.TownyGDR.PlayerData.EtniaList.Casate.Shariana.SharianaCasata;

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
public class Shariana extends Etnia{
	
	private static String nome = "Shariana";
	private static ArrayList<CasataType> casate = new ArrayList<CasataType>();
	static{
		casate.add(SharianaCasata.Iril);
		casate.add(SharianaCasata.Malel);
		casate.add(SharianaCasata.Marine);
		casate.add(SharianaCasata.Tanak);
	}

	public Shariana() {
		this.name = nome;
		this.desc = "Etnia Shariana";
		
	}
	
	public static String getNameEtnia() {
		return nome;
	}

	/**
	 * @return
	 */
	public static ArrayList<CasataType> getCasate() {
		return casate;
	}
}
