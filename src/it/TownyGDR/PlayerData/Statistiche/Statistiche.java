/**
 * 
 */
package it.TownyGDR.PlayerData.Statistiche;

import org.bukkit.configuration.ConfigurationSection;

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
 * Classe astratta che generalizza le statistiche.
 * Le statistiche per lo più avranno un Tag.
 * Esse saranno salvate in un file specificato dal ConfigurationSection.
 * 
 *********************************************************************/
public abstract class Statistiche implements Salva<ConfigurationSection>, Taggable{

	/**
	 * Imposta tutti i valori della statistica a ZERO o
	 * ad un valore definito DEFAULT
	 */
	public abstract void reset();
	
	/**
	 * Riporta in una Stringa i valori della statistica
	 */
	public abstract String toString();
	
	/**
	 * Ritorna il nome della Statistica
	 * @return
	 */
	public abstract String getName();
	
}
