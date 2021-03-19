/**
 * 
 */
package it.TownyGDR.PlayerData.EtniaList;

import java.util.ArrayList;

import it.TownyGDR.PlayerData.Etnia;
import it.TownyGDR.PlayerData.EtniaList.Casate.CasataType;
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

	public Dragoniana() {
		this.name = "Dragoniana";
		this.desc = "Etnia Dragoniana";
		this.casate = new ArrayList<CasataType>();
		this.casate.add(DragonianaCasata.Dandu);
		this.casate.add(DragonianaCasata.Dragoy);
		this.casate.add(DragonianaCasata.Drengot);
		this.casate.add(DragonianaCasata.Hol);
		this.casate.add(DragonianaCasata.Petrix);
		this.casate.add(DragonianaCasata.Renix);
		this.casate.add(DragonianaCasata.Roy);
		this.casate.add(DragonianaCasata.Trevor);
		this.casate.add(DragonianaCasata.Zamputor);
		
	}
}
