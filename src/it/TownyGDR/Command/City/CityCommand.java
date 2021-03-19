/**
 * 
 */
package it.TownyGDR.Command.City;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.Zone.Zona;

/*********************************************************************
 * @author: Elsalamander
 * @data: 18 mar 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * 
 * /City -create Nome
 * /City -[id] getinfo
 * 
 * /City -[id] claim
 * 
 * 
 *********************************************************************/
public class CityCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String strCommand, String[] args) {
		//Controllo errorri di indici
		try{
			//Natura sender
			if(sender instanceof Player) {
				Player p = (Player) sender;
				
				//Controlla se ha il permesso per eseguire il commando
				if(!p.hasPermission("TownyGDR.City.Command")) {
					p.sendMessage(ChatColor.RED + "Non hai i permessi per usare questo commando!");
					return true;
				}
				
				City city = null;
				
				switch(args[0]) {
					case "-create":{
						//crea una città con il nome dato
						city = City.createCity(PlayerData.getPlayerData(p), args[1], Zona.getZonaByLocation(p.getLocation()));
						if(city != null) {
							p.sendMessage("Città creata!");
						}else {
							p.sendMessage("Città NON creata!");
						}
					}break;
				
					case "-id":{
						city = City.getByID(Integer.parseInt(args[1]));
					}break;
				}
				
				if(city == null) return true;
				
				
				switch(args[2]) {
					case "getinfo":{
						p.sendMessage("bbbbbbb");
					}break;
					
					case "save":{
						try {
							city.save(null);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}break;
					
					case "claim":{
						if(city.getArea().claim(p.getLocation(), city, PlayerData.getPlayerData(p))) {
							p.sendMessage("Area claimata");
						}else {
							p.sendMessage("Area non claimata");
						}
					}
				}
				
			}else{
				//console
				
			}	
			
		}catch(ArrayIndexOutOfBoundsException e) {
			//e.printStackTrace();
			error(sender);
			return true;
		}		
		return false;
	}

	/**
	 * @param sender
	 */
	private void error(CommandSender sender) {
		sender.sendMessage("Sintassi non valida per eseguire il commando!");
	}
}
