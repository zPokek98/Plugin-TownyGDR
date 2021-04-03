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

	public Ardese() {
		this.name = "Ardese";
		this.desc = "Etnia Ardese";
		this.casate = new ArrayList<CasataType>();
		this.casate.add(ArdeseCasate.Brial);
		this.casate.add(ArdeseCasate.Maedon);
		this.casate.add(ArdeseCasate.Nekromount);
		this.casate.add(ArdeseCasate.Tunnor);
		
	}
	
}
