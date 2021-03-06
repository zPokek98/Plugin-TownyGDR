/**
 * 
 */
package it.TownyGDR.PlayerData.Statistiche.Stats;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import it.TownyGDR.PlayerData.Statistiche.Statistiche;

/*********************************************************************
 * @author: Elsalamander
 * @data: 27 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Classe per descrivere il KD di un giocatore, taggabile e salvabile
 * nel file di passaggio.
 * 
 *********************************************************************/
public class KDA extends Statistiche{
	
	private static final String NameStatics = "KDA";
	
	//Contenitore dei Tag per il player
	private static ArrayList<String> TagList = new ArrayList<String>();
	static{		//Immetti i tag nel contenitore dei Tag
		TagList.add("%KDA.kill%");
		TagList.add("%KDA.death%");
	}
	
	//Variabili oggetto
	private long kill;
	private long death;
	
	/**
	 * Costruttore con statistiche pari a kill=0 e death=0.
	 */
	public KDA() {
		this.reset();
	}
	
	@Override
	public void reset() {
		this.kill = 0;
		this.death = 0;
	}

	@Override
	public String toString() {
		return "Kill: " + this.getKill() + ", Death: " + this.getDeath();
	}

	/**
	 * Ritorna le morti del giocatore
	 * @return
	 */
	public long getDeath() {
		return this.death;
	}

	/**
	 * Ritorna le kill che ha fatto il giocatore
	 * @return
	 */
	public long getKill() {
		return this.kill;
	}

	@Override
	public String getName() {
		return NameStatics;
	}

	@Override
	public void save(ConfigurationSection config) {
		config.set("Kill", this.kill);
		config.set("Death", this.death);
	}

	@Override
	public void load(ConfigurationSection config) {
		this.kill = config.getLong("Kill");
		this.death = config.getLong("Death");
	}

	//********************************************************************************************* TAG
	@Override
	public boolean hasTag(String tag) {
		return TagList.contains(tag);
	}

	@Override
	public ArrayList<String> getTagList() {
		return TagList;
	}

	@Override
	public String getValueFromTag(String str) {
		if(str == null || str.length() <= 2) return "(Error Invalid code: " + str + ")";
		switch(str) {
			case "%KDA.kill%":{
				return this.getKill() + "";
			}
			case "%KDA.death%":{
				return this.getDeath() + "";
			}
			//...
			
		}
		
		return "(Error Invalid code: " + str + ")";
	}

	

}
