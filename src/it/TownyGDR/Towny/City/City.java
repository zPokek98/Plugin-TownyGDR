/**
 * 
 */
package it.TownyGDR.Towny.City;

import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;

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
 * Classe che descrivi la città nel suo insieme.
 * La città avrà bisogno di parecchie classi di supporto tra cui:
 * - Membri (Sindaco,cittadino,...)
 * - Area
 * - Regole
 * - Impostazioni
 * - Edifici
 * - Nazione
 * 
 *********************************************************************/
public class City implements Salva<FileConfiguration>, Taggable{

	@Override
	public void save(FileConfiguration database) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void load(FileConfiguration database) {
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
