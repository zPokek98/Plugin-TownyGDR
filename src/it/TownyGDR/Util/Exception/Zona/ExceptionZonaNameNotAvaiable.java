/**
 * 
 */
package it.TownyGDR.Util.Exception.Zona;

/*********************************************************************
 * @author: Elsalamander
 * @data: 18 mar 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Eccezzione Nome Zona non utilizzabile!
 * 
 *********************************************************************/
public class ExceptionZonaNameNotAvaiable extends Exception{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Costruttore con messaggio personalizzato
	 * @param message
	 */
	public ExceptionZonaNameNotAvaiable(String message) {
        super(message);
    }
	
	/**
	 * Costruttore con messaggio standard
	 * @param message
	 */
	public ExceptionZonaNameNotAvaiable() {
        super("Nome Zona non utilizzabile!!!");
    }

}
