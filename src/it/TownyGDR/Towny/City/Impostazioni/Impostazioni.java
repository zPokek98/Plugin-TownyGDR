/**
 * 
 */
package it.TownyGDR.Towny.City.Impostazioni;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;

import it.TownyGDR.Towny.City.Impostazioni.set.PvpOnCity;
import it.TownyGDR.Util.Save.Salva;

/*********************************************************************
 * @author: Elsalamander
 * @data: 27 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Uni insieme di Settings che regolano la città modificabili solo
 * dal sindaco.
 * 
 * La gui per la visualizazione delle impostazioni? -> Gui generale?
 * 
 *********************************************************************/
public class Impostazioni implements Salva<ConfigurationSection>{
	
	//Variabili ogg
	private ArrayList<Settings> settings;
	
	/**
	 * Togliere l'istanza pubblica
	 */
	public Impostazioni() {
		this.settings = new ArrayList<Settings>();
		this.settings.add(new PvpOnCity());
	}
	
	@Override
	public void save(ConfigurationSection config) throws IOException {
		for(Settings sett : this.settings) {
			if(!config.contains(sett.getName())) config.set(sett.getName() + ".tmp", "tmp");
			sett.save(config.getConfigurationSection(sett.getName()));
			if(config.contains(sett.getName() + ".tmp")) config.set(sett.getName() + ".tmp", null);
		}
	}

	@Override
	public void load(ConfigurationSection config) throws IOException {
		for(Settings sett : this.settings) {
			sett.load(config.getConfigurationSection(sett.getName()));
		}
	}
	
	/**
	 * Ritorna l'impostazione con il nome "name"
	 * @param name
	 * @return
	 */
	public Settings getSettings(String name) {
		for(Settings imp : this.settings)
		{
			if(imp.getName().equalsIgnoreCase(name))
			{
				return imp;
			}
		}
		return null;
	}
	
	
	/**
	 * Caria tutti i valori dei default
	 * @return
	 */
	public static Impostazioni loadDefault() {
		Impostazioni tmp = new Impostazioni();
		for(Settings sett : tmp.settings)
		{
			sett.setDefault();
		}
		return tmp;
	}
	

}
