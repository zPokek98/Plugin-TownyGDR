/**
 * 
 */
package it.TownyGDR.Command.City.Set;

import java.io.IOException;
import java.util.ArrayList;

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
 * @data: 5 apr 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * 
 *********************************************************************/
public class CityCommandSave extends CommandManager {

	private static ArrayList<String> perm = new ArrayList<String>();
	
	
	static{
		perm.add("City.save");
	}
	
	/**
	 * @param perm
	 */
	public CityCommandSave() {
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
						
						try{
							city.save(null);
							p.sendMessage("La città è stata salvata!");
						}catch (IOException e){
							p.sendMessage("La città NON è stata salvata, ERROR");
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
