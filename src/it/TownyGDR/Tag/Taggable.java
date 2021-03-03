package it.TownyGDR.Tag;

import java.util.ArrayList;

/*********************************************************************
 * @author: Elsalamander
 * @data: 26 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Tutte gli oggetti che hanno un tag di riferimento per il get di una
 * vriabile devono avere queste funzioni per far in modo da avere
 * una comoda trasformazione da %tag% a variabile gestita sulla classe
 * stessa dell'oggetto.
 * 
 *********************************************************************/
public interface Taggable {

	/**
	 * Ritorna se ha questo tag
	 * @param tag
	 * @return
	 */
	public boolean hasTag(String tag);
	
	/**
	 * Ritorna tutti i tag dell'oggetto
	 * @return
	 */
	public ArrayList<String> getTagList();
	
	/**
	 * Ritorna il tag abbinata all'oggetto "ogg" se il tag non
	 * esiste ritorna null
	 * @param str
	 * @return
	 */
	public String getValueFromTag(String str);
	
	/**
	 * Ritorna il tag dell'oggetto taggable
	 * @param obj
	 * @param tag
	 * @return
	 */
	public static String getValueFromTagAndObj(Taggable obj, String tag) {
		return obj.getValueFromTag(tag);
	}
	
}