/**
 * 
 */
package it.TownyGDR.Util.Exception.City;

import it.TownyGDR.Util.Exception.ExceptionLoad;

/*********************************************************************
 * @author: Elsalamander
 * @data: 21 mar 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * 
 *********************************************************************/
public class ExceptionCityImpossibleLoad extends ExceptionLoad{

	public ExceptionCityImpossibleLoad(String mes) {
		super(mes);
	}
	
	public ExceptionCityImpossibleLoad() {
		super("Impossibile caricare la città");
	}

}
