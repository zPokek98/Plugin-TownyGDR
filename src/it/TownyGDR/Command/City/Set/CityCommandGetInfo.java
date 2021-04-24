/**
 * 
 */
package it.TownyGDR.Command.City.Set;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import it.TownyGDR.Command.CommandManager;
import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.City.Membri.Membro;
import it.TownyGDR.Towny.Zone.ElementoArea;
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
public class CityCommandGetInfo  extends CommandManager {

	private static ArrayList<String> perm = new ArrayList<String>();
	
	
	static{
		perm.add("City.save");
	}
	
	/**
	 * @param perm
	 */
	public CityCommandGetInfo() {
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
						
						p.sendMessage(ChatColor.GOLD + "------ Info Citta' ------");
						p.sendMessage(ChatColor.GOLD + "ID: " + city.getId());
						p.sendMessage(ChatColor.GOLD + "Nome: " + city.getName());
						p.sendMessage(ChatColor.GOLD + "Descrizione: " + city.getDescrizione());
						p.sendMessage(ChatColor.GOLD + "Zona ID: " + city.getArea().getZona().getID());
						p.sendMessage(ChatColor.GOLD + "Area Size: " + city.getArea().getSize());
						
						//lista aree
						String area = "";
						for(ElementoArea ele : city.getArea().getClaimedArea()) {
							area +=  ele.getX() + " : " + ele.getZ() + ";";
						}
						area = area.length() > 0 ? area.substring(0, area.length() - 1) : area;
						p.sendMessage(ChatColor.GOLD + "Area: " + area);
						
						//lista sindaci
						String sind = "";
						for(Membro m : city.getSindaco()) {
							sind += PlayerData.getFromUUID(m.getUUID()).getPlayer().getName() + ";";
						}
						sind = sind.length() > 0 ? sind.substring(0, sind.length() - 1) : sind;
						p.sendMessage(ChatColor.GOLD + "Membri: " + sind);
						
						//lista membri
						String membri = "";
						for(Membro m : city.getMembri()) {
							PlayerData pdtmp = PlayerData.getFromUUID(m.getUUID());
							String name = null;
							if(pdtmp == null) {
								name = pdtmp.getPlayer().getName();
							}else{
								name = Bukkit.getOfflinePlayer(m.getUUID()).getName();
							}
							membri += name + ";";
						}
						membri = membri.length() > 0 ? membri.substring(0, membri.length() - 1) : membri;
						p.sendMessage(ChatColor.GOLD + "Membri: " + membri);
						
						p.sendMessage(ChatColor.GOLD + "------ Info Citta' End ------");
					}
				}else{
					p.sendMessage(ChatColor.DARK_RED + "Non sei dentro una citta'/Sindaco");
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
