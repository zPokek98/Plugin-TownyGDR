/**
 * 
 */
package it.TownyGDR.Util.Exception.City;

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
public class ExceptionCityImpossibleLoadArea extends Exception{

	private static final long serialVersionUID = 1L;
	
	
	public ExceptionCityImpossibleLoadArea(String message) {
		super(message);
	}
	
	
	public ExceptionCityImpossibleLoadArea() {
		super("Impossibile caricare l'area della città");
	}

}
