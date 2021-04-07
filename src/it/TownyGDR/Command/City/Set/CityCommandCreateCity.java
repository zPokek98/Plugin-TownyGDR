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
public class CityCommandCreateCity extends CommandManager{

	private static ArrayList<String> perm = new ArrayList<String>();
	static {
		perm.add("City.createCity");
	}
	
	/**
	 * @param perm
	 */
	public CityCommandCreateCity() {
		super(perm);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String arg, String[] args) throws CommandSyntaxError, CommandPermissionError, CommandSenderError {
		//Controlla che è un player
		if(sender instanceof Player) {
			Player p = (Player) sender;
			
			//Controlla i permessi
			if(this.checkPermission(p)) {
				//ottieni il playerData sel sender
				PlayerData pd = PlayerData.getPlayerData(p);
				
				//crea una città con il nome dato
				Zona zon = Zona.getZonaByLocation(p.getLocation());
				
				City city = City.createCity(pd, args[1], zon);
				
				//feedback
				if(city != null) {
					p.sendMessage("Città creata!");
				}else{
					p.sendMessage("Città NON creata!");
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
