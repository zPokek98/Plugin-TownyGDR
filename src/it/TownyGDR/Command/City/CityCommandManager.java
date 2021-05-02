/**
 * 
 */
package it.TownyGDR.Command.City;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mojang.datafixers.util.Pair;

import it.TownyGDR.Command.CommandManager;
import it.TownyGDR.Command.City.Set.CityCommandClaim;
import it.TownyGDR.Command.City.Set.CityCommandCreateCity;
import it.TownyGDR.Command.City.Set.CityCommandGetInfo;
import it.TownyGDR.Command.City.Set.CityCommandHelp;
import it.TownyGDR.Command.City.Set.CityCommandKick;
import it.TownyGDR.Command.City.Set.CityCommandSave;
import it.TownyGDR.Command.City.Set.CityCommandSetDescription;
import it.TownyGDR.Command.City.Set.Invite.CityCommandInvite;
import it.TownyGDR.Command.City.Set.Invite.CityCommandInviteAccept;
import it.TownyGDR.Command.City.Set.Invite.CityCommandInviteDeny;
import it.TownyGDR.Command.City.Set.Lotto.CityCommandLottoCompra;
import it.TownyGDR.Command.City.Set.Lotto.CityCommandLottoGetInfo;
import it.TownyGDR.Command.City.Set.Lotto.CityCommandLottoVendi;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.City.Area.Area;
import it.TownyGDR.Util.Exception.City.ExceptionCityImpossibleLoad;
import it.TownyGDR.Util.Exception.Command.CommandPermissionError;
import it.TownyGDR.Util.Exception.Command.CommandSenderError;
import it.TownyGDR.Util.Exception.Command.CommandSyntaxError;
import net.md_5.bungee.api.ChatColor;


/*********************************************************************
 * @author: Elsalamander
 * @data: 3 apr 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Classe per la gestione comandi per tutti i comandi che iniziano con
 * "/city ..."
 * 
 * Qua ci sarà solo la suddivisione dei comandi, il comando è demandato
 * a una classe specifica nel packege "Set"
 * 
 * Un comando può avere 2 tipi di inizio:
 * - uno per specificare una città tramite:	-id   (id città)
 * 											-name (nome città)
 * 											-qua  (posizione del CommandSender)
 * 
 * - uno che sotto intende la città a cui fa parte.
 * 
 ************************************************************************************/
public class CityCommandManager implements CommandExecutor{
	
	private static CommandManager create 			= new CityCommandCreateCity();
	private static CommandManager invite 			= new CityCommandInvite();
	private static CommandManager inviteAccept 		= new CityCommandInviteAccept();
	private static CommandManager inviteDeny 		= new CityCommandInviteDeny();
	private static CommandManager kick 				= new CityCommandKick();
	private static CommandManager claim 			= new CityCommandClaim();
	private static CommandManager help 				= new CityCommandHelp();
	private static CommandManager description  		= new CityCommandSetDescription();
	private static CommandManager lottoVendi 		= new CityCommandLottoVendi();
	private static CommandManager lottoCompra 		= new CityCommandLottoCompra();
	private static CommandManager lottoGetInfo 		= new CityCommandLottoGetInfo();
	private static CommandManager save				= new CityCommandSave();
	private static CommandManager getInfo 			= new CityCommandGetInfo();
	

	@Override
	public boolean onCommand(CommandSender sender, Command command, String arg, String[] args) {
		
		try{
			City city = null;
			int offset = 0;
			//che tipo di comando è?
			if(args[0].equalsIgnoreCase("-id") || args[0].equalsIgnoreCase("-nome") || args[0].equalsIgnoreCase("-qua")) {
				//prendi la città
				Pair<City, Integer> pair = getCity(args, sender);
				city = pair.getFirst();
				offset = pair.getSecond().intValue();
				
				//Chiama la funzione in base a quello che serve
				switch(args[offset]) {
					case "":{
						
					}break;
				}
			}else{
				//Comando per i player
				switch(args[offset]) {
					case "create":{
						return create.onCommand(sender, command, arg, args);
					}
					
					case "invite":{
						return invite.onCommand(sender, command, arg, args);
					}
					
					case "inviteaccept":{
						return inviteAccept.onCommand(sender, command, arg, args);
					}
					
					case "invitedeny":{
						return inviteDeny.onCommand(sender, command, arg, args);
					}
					
					case "kick":{
						return kick.onCommand(sender, command, arg, args);
					}
					
					case "claim":{
						return claim.onCommand(sender, command, arg, args);
					}
					
					case "help":{
						return help.onCommand(sender, command, arg, args);
					}
					
					//in realtà questo è solo per gli admin :)
					case "save":{
						return save.onCommand(sender, command, arg, args);
					}
					
					case "getInfo":{
						return getInfo.onCommand(sender, command, arg, args);
					}
					
					case "lotto":{
						switch(args[offset + 1]) {
							case "vendi":{
								return lottoVendi.onCommand(sender, command, arg, args);
							}
							
							case "compra":{
								return lottoCompra.onCommand(sender, command, arg, args);
							}
							
							case "info":{
								return lottoGetInfo.onCommand(sender, command, arg, args);
							}
							
							default:{
								throw new CommandSyntaxError();
							}
						}
					}
					
					//Switch case per il set:
					case "set":{
						switch(args[offset + 1]) {
							case "description":{
								return description.onCommand(sender, command, arg, args);
							}
						}
						
					}
					
					default:{
						throw new CommandSyntaxError();
					}
				}
				
			}
			
			
		}catch(CommandSyntaxError | ArrayIndexOutOfBoundsException e){
			//Errore nell sitassi del comando!
			//...
			
		}catch(CommandSenderError e){
			//Erroe è un comando solo per i player! Non per la console!
			//...
			
		}catch(CommandPermissionError e){
			//Non ha i permessi per eseguire questo comando!
			//...
			
		}
		return false;
	}

	/**
	 * Ritorna la città e l'offeset del comando
	 * Sintassi:
	 * - args[0] : deve contenere il comando (-id, -name, -qua)
	 * - args[1] : le eventuali informaazioni!
	 * @param args 
	 * @param sender 
	 * @return
	 * @throws CommandSyntaxError 
	 * @throws CommandSenderError 
	 */
	private Pair<City, Integer> getCity(String[] args, CommandSender sender) throws CommandSyntaxError, CommandSenderError {
		City city = null;
		
		switch(args[0]) {
		
			case "-id":{
				try {
					city = City.getByID(Integer.parseInt(args[1]));
				}catch(NumberFormatException e){
					//valore passato al comando non vaido
					throw new CommandSyntaxError("valore passato al comando non vaido");
				}catch(ExceptionCityImpossibleLoad e){
					//non carica la città
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Impossibile caricare la città di id: " + args[1]);
				}
				return new Pair<City, Integer>(city, 2);
			}
			
			case "-name":{
				try{
					city = City.getByName(args[1]);
				}catch(NumberFormatException e){
					//valore passato al comando non vaido
					throw new CommandSyntaxError("valore passato al comando non vaido");
				}
				return new Pair<City, Integer>(city, 2);
			}
			
			case "-qua":{
				try{
					if(sender instanceof Player) {
						Player p = (Player) sender;
						city = Area.getCityFromArea(p.getLocation().getChunk());
					}else{
						//solo per i player questo commando!
						throw new CommandSenderError();
					}
					
				}catch(NumberFormatException e){
					//valore passato al comando non vaido
					throw new CommandSyntaxError("valore passato al comando non vaido");
				}
				return new Pair<City, Integer>(city, 1);
			}
			
		}
		
		return null;
	}

	
	
	
	
	
	
	
	
	
	
}
