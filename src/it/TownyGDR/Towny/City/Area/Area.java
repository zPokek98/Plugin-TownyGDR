/**
 * 
 */
package it.TownyGDR.Towny.City.Area;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import it.TownyGDR.Util.Save.Salva;

/*********************************************************************
 * @author: Elsalamander
 * @data: 27 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Classe che descrive l'area della città.
 * L'area è la zona claimata dalla città dal sindaco e un insieme di
 * lotti che sono le aree elementari della città.
 * Ogni volta che il sindaco vuole claimare la zona da claimare deve essere
 * adiacente all'area gia presente se c'è.
 * 
 *********************************************************************/
public class Area implements Salva<ConfigurationSection>{
	
	//Variabili oggetto
	private ArrayList<Lotto> lotti; //Array di Lotto per comodità(comporta di creare la funzione "equals" in Lotto
	

	//*********************************************************************** Costruttori
	/**
	 * Costruttore Area Vuota
	 */
	public Area() {
		
	}
	
	//*********************************************************************** Funzioni Oggetto
	/**
	 * Ritorna la dimensione dell'area in numero di Chunk
	 * @return
	 */
	public int getSize() {
		int size = 0;
		for(Lotto lot : lotti) {
			size += lot.getSize();
		}
		return size;
	}
	
	
	/**
	 * @param lotto
	 * @return
	 */
	public static boolean checkIsFree(Lotto lotto) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Controlla se una parte del lotto è contenuto nella città, se anche un suo pezzo
	 * è all'interno ritorna FALSE.
	 * @param lot
	 * @return
	 */
	public boolean containLotto(Lotto lot) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Controlla se la posizione "loc" è all'interno della città.
	 * 
	 * Funzione molto usata in futuro per controlla se un player è
	 * all'interno di una città o no!
	 * @param loc
	 * @return
	 */
	public boolean containLocation(Location loc) {
		//TODO
		return false;
	}

	/**
	 * Controlla se il Lotto "lot" è adiacente all'area, se no ritorna false
	 * @param lot
	 * @return 
	 */
	public boolean checkAdiacenza(Lotto lot) {
		//TODO
		return false;
	}

	@Override
	public void save(ConfigurationSection database) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void load(ConfigurationSection database) {
		// TODO Auto-generated method stub
		
	}

}
