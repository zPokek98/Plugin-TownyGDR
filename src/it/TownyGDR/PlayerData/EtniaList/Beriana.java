/**
 * 
 */
package it.TownyGDR.PlayerData.EtniaList;

import java.util.ArrayList;

import it.TownyGDR.PlayerData.Etnia;
import it.TownyGDR.PlayerData.EtniaList.Casate.CasataType;
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

	public Beriana() {
		this.name = "Beriana";
		this.desc = "Etnia Beriana";
		this.casate = new ArrayList<CasataType>();
		this.casate.add(BerianaCasata.Gwael);
		this.casate.add(BerianaCasata.Hemilock);
		this.casate.add(BerianaCasata.Mady);
		this.casate.add(BerianaCasata.Rhoben);
		
	}
}
