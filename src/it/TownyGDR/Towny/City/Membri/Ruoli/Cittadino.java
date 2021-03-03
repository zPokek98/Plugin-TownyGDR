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
 * Classe che descrive il cittadino e le sue funzioni nella citta
 * 
 *********************************************************************/
public class Cittadino extends Membro{

	/**
	 * @param uuid
	 * @param ruolo
	 */
	public Cittadino(UUID uuid) {
		super(uuid, MembroType.Cittadino);
	}

}
