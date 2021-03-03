/**
 * 
 */
package it.TownyGDR.Towny.City.Membri.Ruoli;

import java.util.UUID;

import it.TownyGDR.Towny.City.Membri.Membro;
import it.TownyGDR.Towny.City.Membri.MembroType;

/*********************************************************************
 * @author: Elsalamander
 * @data: 27 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Classe che descrive le funzioni per il sindaco
 * 
 *********************************************************************/
public class Sindaco extends Membro{

	/**
	 * @param uuid
	 * @param ruolo
	 */
	public Sindaco(UUID uuid) {
		super(uuid, MembroType.Sindaco);
	}

}
