/**
 * 
 */
package it.TownyGDR.Towny;

import it.TownyGDR.Towny.City.City;

/*********************************************************************
 * @author: Elsalamander
 * @data: 12 mar 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Tutte le Aree come città/spawn/miniere/foreste/Parchi?/Altro
 * sono aree ben definite che sono generalizzate da questa classe
 * 
 *********************************************************************/
public abstract class Luogo {

	/**
	 * Ritorna che tipo di luogo è
	 * @return
	 */
	public abstract LuoghiType getType();

	/**
	 * Ogni luogo ha un id, che è gestito nella classe specifica del luogo.
	 * @return
	 */
	public abstract int getId();
	
	/**
	 * Ritorna tremite type e id il luogo specifico
	 * @param type
	 * @param id
	 * @return
	 */
	public static Luogo getById(LuoghiType type, int id) {
		switch(type){
			case City:{
				return City.getByID(id);
			}
			
			default:
				break;
			
		}
		
		return null;
	}
	
}
