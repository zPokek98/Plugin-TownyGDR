/**
 * 
 */
package it.TownyGDR.Command.PlayerData;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import it.TownyGDR.PlayerData.PlayerData;
import net.md_5.bungee.api.ChatColor;

/*********************************************************************
 * @author: Elsalamander
 * @data: 6 apr 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * 
 *********************************************************************/
public class PlayerDataFirstJoin implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String arg, String[] args) {
		//Chi ha lanciato il comando?
		if(sender instanceof Player) {
			Player p = (Player) sender;
			//Controlla che questo player è nella lista dei primi join
			ArrayList<PlayerData> list = PlayerData.getListFirstJoin();
			
			PlayerData pd = PlayerData.getPlayerData(p);
			
			for(PlayerData pd_tmp : list) {
				if(pd_tmp.getUUID().equals(pd.getUUID())) {
					//if(args[1].equalsIgnoreCase(pd.getUUID().toString())) {
						pd.openSelectionFirstJoin();
						return true;
					//}
				}
			}
			
		}else{
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Questo commando è solo per i player!");
		}
		return true;
	}

}
