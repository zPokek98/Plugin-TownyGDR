package it.TownyGDR.Util.Save;


/*********************************************************************
 * @author: Elsalamander
 * @data: 27 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Interfaccia per indicare che una classe ha la funzione di save e load
 * necessaria per moltissimo oggetti poichè i dati delle nazioni/città/player
 * devono essere salvati da qualche parte!
 * 
 * Attenzione dato che i dati posso essere salvati su un database o su
 * file bisogna specificare dove salvare con il tamplate quando si
 * implementa la classe!
 * 
 ********************************************************************/
public interface Salva<T> {
	
	/**
	 * Salva i dati.
	 */
	public void save(T database);
	
	/**
	 * Carica i dati
	 */
	public void load(T database);
	

}
