/**
 * 
 */
package it.TownyGDR.PlayerData.EtniaList;

import java.util.ArrayList;

import it.TownyGDR.PlayerData.Etnia;
import it.TownyGDR.PlayerData.EtniaList.Casate.CasataType;
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

	public Shariana() {
		this.name = "Shariana";
		this.desc = "Etnia Shariana";
		this.casate = new ArrayList<CasataType>();
		this.casate.add(SharianaCasata.Iril);
		this.casate.add(SharianaCasata.Malel);
		this.casate.add(SharianaCasata.Marine);
		this.casate.add(SharianaCasata.Tanak);
		
	}
}
