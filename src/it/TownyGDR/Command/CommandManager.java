/**
 * 
 */
package it.TownyGDR.Command;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import it.TownyGDR.Util.Exception.Command.CommandPermissionError;
import it.TownyGDR.Util.Exception.Command.CommandSenderError;
import it.TownyGDR.Util.Exception.Command.CommandSyntaxError;


/*********************************************************************
 * @author: Elsalamander
 * @data: 3 apr 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Generalizzazione comandi
 * 
 *********************************************************************/
public abstract class CommandManager {
	
	//lista permessi per il comando
	protected ArrayList<String> permission;
	
	/**
	 * Costruttore comando, per indicare i sui permessi
	 * @param perm
	 */
	public CommandManager(ArrayList<String> perm) {
		this.permission = perm;
	}

	/**
	 * Esecuzione comando!
	 * @param sender
	 * @param command
	 * @param arg
	 * @param args
	 * @return
	 * @throws CommandSyntaxError
	 * @throws CommandPermissionError
	 * @throws CommandSenderError
	 */
	public abstract boolean onCommand(CommandSender sender, Command command, String arg, String[] args) throws CommandSyntaxError, CommandPermissionError, CommandSenderError;
	
	/**
	 * Ritorno permessi
	 * @return
	 */
	public ArrayList<String> getPermission(){
		return this.permission;
	}
	
	/**
	 * Controlla se ha i permessi per eseguire questo comando
	 * @param p
	 * @return
	 */
	public boolean checkPermission(Player p) {
		return true;
		/*
		for(String str : this.permission) {
			if(!p.hasPermission(str)) {
				return false;
			}
		}
		return true;
		*/
	}
	
}
