/**
 * 
 */
package it.TownyGDR.Util.Exception.Zona;

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
public class ExceptionSectorInvalidStringLoad extends ExceptionLoad{

	private static final long serialVersionUID = 1L;
	
	public ExceptionSectorInvalidStringLoad(String message) {
		super(message);
	}

	public ExceptionSectorInvalidStringLoad() {
		super("Stringa data per ottenere il settore non valida!");
	}
}
