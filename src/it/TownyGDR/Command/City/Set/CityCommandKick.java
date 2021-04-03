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
public class CityCommandKick extends CommandManager {

	private static ArrayList<String> perm = new ArrayList<String>();
	static {
		perm.add("City.help");
	}
	
	/**
	 * @param perm
	 */
	public CityCommandKick() {
		super(perm);
		// TODO Auto-generated constructor stub
	}

	@Override
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
						//prendi il giocatore da kickare
						Player pi = Bukkit.getPlayer(args[2]);
						if(pi != null) {
							PlayerData pdi = PlayerData.getPlayerData(pi);
							
							//Controlla che è dentro la città
							if(city.hasMembro(pdi.getUUID())) {
								city.removeMembro(city.getMembroByUUID(pdi.getUUID()));
							}else{
								p.sendMessage("Il player inserito non fa parte della citta'");
							}
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
