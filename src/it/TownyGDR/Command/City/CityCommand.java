/**
 * 
 */
package it.TownyGDR.Command.City;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.City.Membri.Membro;
import it.TownyGDR.Towny.Zone.ElementoArea;
import it.TownyGDR.Towny.Zone.Zona;
import it.TownyGDR.Util.Exception.City.ExceptionCityImpossibleLoad;

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
 * /City -[id] lotto vendi/compra
 * 
 *********************************************************************/
/*
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
						//crea una citt� con il nome dato
						city = City.createCity(PlayerData.getPlayerData(p), args[1], Zona.getZonaByLocation(p.getLocation()));
						if(city != null) {
							p.sendMessage("Citt� creata!");
						}else {
							p.sendMessage("Citt� NON creata!");
						}
						return true;
					}
				
					case "-id":{
						try {
							city = City.getByID(Integer.parseInt(args[1]));
						}catch(NumberFormatException e){
							//valore passato al comando non vaido
							error(p);
						}catch(ExceptionCityImpossibleLoad e){
							//non carica la citt�
						}
					}break;
				}
				
				if(city == null) {
					p.sendMessage("Nessuta citt� trovata");
					return true;
				}
				
				
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
					
					case "lotto":{
						switch(args[3]) {
							case "vendi":{
								Chunk ck = p.getLocation().getChunk();
								ElementoArea ele = new ElementoArea(ck.getX(), ck.getZ());
								
								Membro mem = PlayerData.getPlayerData(p).getCity().getMembroByUUID(p.getUniqueId());
								city.getArea().createLottoVendita(ele, 0, mem);
								
								p.sendMessage("Creato il lotto");
							}break;
							
							case "compra":{
								Chunk ck = p.getLocation().getChunk();
								ElementoArea ele = new ElementoArea(ck.getX(), ck.getZ());
								
								Membro mem = PlayerData.getPlayerData(p).getCity().getMembroByUUID(p.getUniqueId());
								mem.getLotto().compraLotto(city.getArea().LottoVenditagetByElementoArea(ele), mem);
								p.sendMessage("Lotto comprato");
							}
							
							
						}
						
						
						
					}break;
				}
				
			}else{
				//console
				
			}	
			return true;
		}catch(ArrayIndexOutOfBoundsException e) {
			//e.printStackTrace();
			error(sender);
			return true;
		}		

	}
*/
	/**
	 * @param sender
	 */
/*
	private void error(CommandSender sender) {
		sender.sendMessage("Sintassi non valida per eseguire il commando!");
	}
}
*/