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
public class CommandSyntaxError extends Exception {
	
	private static final long serialVersionUID = 1L;

	public CommandSyntaxError() {
		this("Errore nella sitassi del comando!");
	}
	
	public CommandSyntaxError(String mes) {
		super(mes);
	}
	
}
