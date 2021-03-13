/**
 * 
 */
package it.TownyGDR.Towny.Zone.Type;

import it.TownyGDR.Towny.Zone.Zona;
import it.TownyGDR.Towny.Zone.ZonaType;

/*********************************************************************
 * @author: Elsalamander
 * @data: 9 mar 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Zona destinata alla città fondata da un player.
 * Questa descrive unicamente il fatto che è una zona destinata per la
 * città non comprende anche l'area della città, ma solo quella destinata!
 * 
 *********************************************************************/
public class ZonaCittadina extends Zona {

	/**
	 * @param nome
	 * @param type
	 */
	public ZonaCittadina(String nome) {
		super(nome, ZonaType.Cittadina);
	}

}
