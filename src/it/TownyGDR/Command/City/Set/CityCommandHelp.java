/**
 * 
 */
package it.TownyGDR.Command.City.Set;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.ChatColor;
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
	private static String helpHeader = ChatColor.GOLD + "---------------------------------------";
	private static String helpTail = ChatColor.GOLD + "---------------------------------------";
	static {
		perm.add("City.help");
		
		ArrayList<String> tmp = new ArrayList<String>();
		
		tmp.add(ChatColor.AQUA + "/City create <nomeCitta> " + ChatColor.YELLOW + ": Crea la città");
		tmp.add(ChatColor.AQUA + "/City invite <nomePlayer>" + ChatColor.YELLOW + ": Invita il player nella città");
		tmp.add(ChatColor.AQUA + "/City inviteAccept" + ChatColor.YELLOW + ": Accetta l'invito alla città");
		tmp.add(ChatColor.AQUA + "/City invite <nomePlayer>" + ChatColor.YELLOW + ": Rifiuta l'invito alla città");
		tmp.add(ChatColor.AQUA + "/City kick <nomePlayer>" + ChatColor.YELLOW + ": Kicka il player dalla città");
		tmp.add(ChatColor.AQUA + "/City set description <descrizione>");
		tmp.add(ChatColor.AQUA + "/City claim " + ChatColor.YELLOW + ": Claimare la posizione attuale");
		tmp.add(ChatColor.AQUA + "/City help " + ChatColor.YELLOW + ": Per la lista dei comandi");
		helpMap.put(0, tmp);
		
		tmp = new ArrayList<String>();
		tmp.add(ChatColor.AQUA + "/City lotto vendi <prezzo> " + ChatColor.YELLOW + ": Per vendere un lotto");
		tmp.add(ChatColor.AQUA + "/City lotto compra " + ChatColor.YELLOW + ": Per comprare il lotto in cui si è");
		tmp.add(ChatColor.AQUA + "/City lotto getinfo " + ChatColor.YELLOW + ": Informazioni sul lotto in cui si è");
		tmp.add(ChatColor.AQUA + "/City getinfo " + ChatColor.YELLOW + ": Informazioni sulla città in cui si è");
		
		helpMap.put(1, tmp);
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
					page = Integer.parseInt(args[1]);
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
