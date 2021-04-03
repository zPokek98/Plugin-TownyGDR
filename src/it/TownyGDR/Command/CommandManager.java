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
 * 
 *********************************************************************/
public abstract class CommandManager {
	
	protected ArrayList<String> permission;
	
	public CommandManager(ArrayList<String> perm) {
		this.permission = perm;
	}

	public abstract boolean onCommand(CommandSender sender, Command command, String arg, String[] args) throws CommandSyntaxError, CommandPermissionError, CommandSenderError;
	
	public ArrayList<String> getPermission(){
		return this.permission;
	}
	
	public boolean checkPermission(Player p) {
		for(String str : this.permission) {
			if(!p.hasPermission(str)) {
				return false;
			}
		}
		return true;
	}
	
}
