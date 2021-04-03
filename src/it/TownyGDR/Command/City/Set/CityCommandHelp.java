/**
 * 
 */
package it.TownyGDR.Command.City.Set;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import it.TownyGDR.Command.CommandManager;
import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.Zone.Zona;
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
public class CityCommandHelp extends CommandManager{

	private static ArrayList<String> perm = new ArrayList<String>();
	private static Map<Integer, ArrayList<String>> helpMap = new TreeMap<Integer, ArrayList<String>>();
	private static String helpHeader = "---------------------------------------";
	private static String helpTail = "---------------------------------------";
	static {
		perm.add("City.help");
		
		ArrayList<String> tmp = new ArrayList<String>();
		
		tmp.add("/City create <nomeCitta>: Crea la città");
		tmp.add("/City invite <nomePlayer>: Invita il player nella città");
		tmp.add("/City kick <nomePlayer>: Kicka il player dalla città");
		tmp.add("/City set description <descrizione>");
		tmp.add("/City claim: Claimare la posizione attuale");
		tmp.add("/City help: Per la lista dei comandi");
		helpMap.put(0, tmp);
	}
	
	/**
	 * @param perm
	 */
	public CityCommandHelp() {
		super(perm);
	}

	@Override
	/**
	 * /city help 1
	 */
	public boolean onCommand(CommandSender sender, Command command, String arg, String[] args) throws CommandSyntaxError, CommandPermissionError, CommandSenderError {
		//Controlla che è un player
		if(sender instanceof Player) {
			Player p = (Player) sender;
			
			//Controlla i permessi
			if(this.checkPermission(p)) {
				
				//pagina help
				int page = 0;
				//secondo argomento
				if(args.length >= 3) {
					page = Integer.parseInt(args[2]);
				}
				
				//Lista
				p.sendMessage(helpHeader);
				ArrayList<String> list = helpMap.get(page);
				for(String str : list) {
					p.sendMessage(str);
				}
				p.sendMessage(helpTail);
				
				return true;
			}else{
				throw new CommandPermissionError();
			}
			
		}else{
			throw new CommandSenderError();
		}
	}

}
