/**
 * 
 */
package it.TownyGDR.Util.Exception.Command;

/*********************************************************************
 * @author: Elsalamander
 * @data: 3 apr 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * 
 *********************************************************************/
public class CommandPermissionError  extends Exception {
	
	private static final long serialVersionUID = 1L;

	public CommandPermissionError() {
		this("Errore nella sitassi del comando!");
	}
	
	public CommandPermissionError(String mes) {
		super(mes);
	}

}
