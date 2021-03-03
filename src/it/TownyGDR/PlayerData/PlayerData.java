/**
 * 
 */
package it.TownyGDR.PlayerData;

import java.util.ArrayList;

import it.MySQL.MySQL;
import it.TownyGDR.Tag.Taggable;
import it.TownyGDR.Util.Save.Salva;

/*********************************************************************
 * @author: Elsalamander
 * @data: 27 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Classe che "Estende" la classe player anche se non esplicitamente
 * Verrano racchiuse tutte le informazioni riguardanti il player
 * e le sue statistiche di rilevanza e altri dati.
 * 
 *********************************************************************/
public class PlayerData implements Salva<MySQL>, Taggable{

	@Override
	public void save(MySQL database) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void load(MySQL database) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasTag(String tag) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<String> getTagList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValueFromTag(String str) {
		// TODO Auto-generated method stub
		return null;
	}

}
