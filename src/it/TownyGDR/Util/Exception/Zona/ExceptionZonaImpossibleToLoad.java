/**
 * 
 */
package it.TownyGDR.Util.Exception.Zona;

import it.TownyGDR.Util.Exception.ExceptionLoad;

/*********************************************************************
 * @author: Elsalamander
 * @data: 18 mar 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * 
 *********************************************************************/
public class ExceptionZonaImpossibleToLoad extends ExceptionLoad{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Costruttore con messaggio personalizzato
	 * @param message
	 */
	public ExceptionZonaImpossibleToLoad(String message) {
		super(message);
	}
	
	/**
	 * Costruttore con messaggio standard
	 * @param message
	 */
	public ExceptionZonaImpossibleToLoad() {
		super("Impossibile Caricare la Zona!!!");
	}

}
