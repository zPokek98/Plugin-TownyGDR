/**
 * 
 */
package it.TownyGDR.Command.City.Set.Lotto;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import it.TownyGDR.Command.CommandManager;
import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.City.Area.LottoVendita;
import it.TownyGDR.Towny.City.Membri.Membro;
import it.TownyGDR.Towny.Zone.ElementoArea;
import it.TownyGDR.Util.Exception.Command.CommandPermissionError;
import it.TownyGDR.Util.Exception.Command.CommandSenderError;
import it.TownyGDR.Util.Exception.Command.CommandSyntaxError;
import net.md_5.bungee.api.ChatColor;

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
public class CityCommandLottoVendi extends CommandManager {

	private static ArrayList<String> perm = new ArrayList<String>();
	static{
		perm.add("City.lotto.vendi");
	}
	
	/**
	 * @param perm
	 */
	public CityCommandLottoVendi() {
		super(perm);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String arg, String[] args) throws CommandSyntaxError, CommandPermissionError, CommandSenderError {
		//Controlla che � un player
		if(sender instanceof Player) {
			Player p = (Player) sender;
			
			//Controlla i permessi
			if(this.checkPermission(p)) {

				//get playerData
				PlayerData pd = PlayerData.getPlayerData(p);
				
				//� dentro una citt�?
				if(pd.getCity() != null) {
					//� un sindaco?
					City city = pd.getCity();
					if(city.hasSindaco(pd.getUUID())) {
						//ottieni il chunk in cui si trova il player
						Chunk ck = p.getLocation().getChunk();
						//get elemento area
						ElementoArea ele = new ElementoArea(ck.getX(), ck.getZ());
						
						//ottieni il membro
						Membro mem = city.getMembroByUUID(p.getUniqueId());
						
						//crea il lotto da vendere
						Double prezzo = 0.0;
						try{
							prezzo = Double.parseDouble(args[2]);
						}catch(NumberFormatException e) {
							throw new CommandSyntaxError();
						}
						
						LottoVendita lotto = city.getArea().createLottoVendita(ele, prezzo, mem);
						
						//feedBack
						if(lotto != null) {
							p.sendMessage("Creato il lotto");
						}else{
							p.sendMessage(ChatColor.RED + "Lotto NON in vendita!");
						}
						
						
					}else{
						p.sendMessage("Non sei il Sindaco della citta'");
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

