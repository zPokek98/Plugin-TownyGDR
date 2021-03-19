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
	
	public Naviana() {
		this.name = "Naviana";
		this.desc = "Etnia Naviana";
		this.casate = new ArrayList<CasataType>();
		this.casate.add(NavianaCasata.Darnis);
		this.casate.add(NavianaCasata.Egeril);
		this.casate.add(NavianaCasata.Fen);
		this.casate.add(NavianaCasata.Forven);
		this.casate.add(NavianaCasata.Leowell);
		this.casate.add(NavianaCasata.River);
		this.casate.add(NavianaCasata.Singer);
		

		
	}
}
