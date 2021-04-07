/**
 * 
 */
package it.TownyGDR.Towny.Task;

import org.bukkit.Location;

import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Towny.Luogo;
import it.TownyGDR.Towny.Zone.ElementoArea;
import it.TownyGDR.Towny.Zone.Sector;
import it.TownyGDR.Towny.Zone.Zona;


/*********************************************************************
 * @author: Elsalamander
 * @data: 1 apr 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Classe supporto al TaskLocation
 * 
 *********************************************************************/
public class Posiction {
	
	//Variabili oggetto
	private Sector sec;
	private ElementoArea ele;
	private Luogo luogo;
	private Location location;
	
	public Posiction(Sector sec, ElementoArea ele, Luogo luogo, Location location) {
		this.sec = sec;
		this.ele = ele;
		this.luogo = luogo;
		this.location = location;
	}
	
	/**
	 * Ritorna la posizione del player
	 * @param pd
	 * @return
	 */
	public static Posiction getPosPlayerData(PlayerData pd) {
		Posiction pos = TaskLocation.map.get(pd);
		
		if(pos == null) {
			Location loc = pd.getPlayer().getLocation();
			//può essere null se il player non è dentro una zona di una fazione
			Zona zon = Zona.getZonaByLocation(loc);
			Sector sec = Sector.getSectorByLocation(loc);
			ElementoArea ele = new ElementoArea(loc.getChunk());
			
			return new Posiction(sec, ele, zon == null ? null : zon.getLuogo(), loc);
		}else{
			return pos;
		}
	}
	
	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @return the sec
	 */
	public Sector getSec() {
		return sec;
	}
	
	/**
	 * @param sec the sec to set
	 */
	public void setSec(Sector sec) {
		this.sec = sec;
	}
	
	/**
	 * @return the ele
	 */
	public ElementoArea getEle() {
		return ele;
	}
	
	/**
	 * @param ele the ele to set
	 */
	public void setEle(ElementoArea ele) {
		this.ele = ele;
	}
	
	/**
	 * @return the faz
	 */
	public Luogo getLuogo() {
		return this.luogo;
	}
	
	/**
	 * @param faz the faz to set
	 */
	public void setLuogo(Luogo luogo) {
		this.luogo = luogo;
	}
}
