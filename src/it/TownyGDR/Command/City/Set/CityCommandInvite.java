/**
 * 
 */
package it.TownyGDR.Command.City.Set;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import it.TownyGDR.Command.CommandManager;
import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.City.Membri.Invite;
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
 * Gli inviti sono gestiti dalla classe Invite
 * 
 *********************************************************************/
public class CityCommandInvite extends CommandManager {

	private static ArrayList<String> perm = new ArrayList<String>();
	
	
	static{
		perm.add("City.help");
	}
	
	/**
	 * @param perm
	 */
	public CityCommandInvite() {
		super(perm);
	}

	@Override
	/**
	 * City invite <nomePlayer>
	 */
	public boolean onCommand(CommandSender sender, Command command, String arg, String[] args) throws CommandSyntaxError, CommandPermissionError, CommandSenderError {
		//Controlla che è un player
		if(sender instanceof Player) {
			Player p = (Player) sender;
			
			//Controlla i permessi
			if(this.checkPermission(p)) {

				//get playerData
				PlayerData pd = PlayerData.getPlayerData(p);
				
				//è dentro una città?
				if(pd.getCity() != null) {
					//è un sindaco?
					City city = pd.getCity();
					if(city.hasSindaco(pd.getUUID())) {
						//prendi il giocatore invitato
						Player pi = Bukkit.getPlayer(args[2]);
						PlayerData pdi = PlayerData.getPlayerData(pi);
						if(pi != null) {
							Invite.addInvite(pd, pdi, city);
							
						}else{
							p.sendMessage("Il player inserito non esiste o non è online");
						}
					}
				}else{
					p.sendMessage("Non sei dentro una citta'/Sindaco");
				}
				return true;
			}else{
				throw new CommandPermissionError();
			}
			
		}else{
			throw new CommandSenderError();
		}
	}
	


}
