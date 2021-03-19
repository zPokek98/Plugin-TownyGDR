/**
 * 
 */
package it.TownyGDR.Command.Zona;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import it.CustomConfig.CustomConfig;
import it.TownyGDR.TownyGDR;
import it.TownyGDR.Towny.Zone.ElementoArea;
import it.TownyGDR.Towny.Zone.Sector;
import it.TownyGDR.Towny.Zone.Zona;
import it.TownyGDR.Towny.Zone.ZonaType;
import it.TownyGDR.Util.Exception.Zona.ExceptionZonaNameNotAvaiable;

/*********************************************************************
 * @author: Elsalamander
 * @data: 18 mar 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Comandi per le Zone
 * /Zona -[id/nome/loc] args... (save)
 * 
 * /Zona -[id/nome] <arg> [add/remove/check] -qua
 * /Zona -[id/nome] <arg> [add/remove/check] -pos [x,z]
 * 
 * /Zona -[loc] -qua [add/remove/check] -qua
 * /Zona -[loc] -qua [add/remove/check] -pos [x,z]
 * 
 * /Zona -[loc] -pos [x,z] [add/remove/check] -qua
 * /Zona -[loc] -pos [x,z] [add/remove/check] -pos [x,z]
 * 
 * /Zona -[id/nome] <arg> getinfo
 * /Zona -[loc] -qua getinfo
 * /Zona -[loc] -pos [x,z] getinfo
 * 
 *********************************************************************/
public class ZonaCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String strCommand, String[] args) {
		
		//Controllo possibili errori
		try{
		
			//Natura sender
			if(sender instanceof Player) {
				//Player
				Player p = (Player)sender;
				
				//Controlla se ha il permesso per eseguire il commando
				if(!p.hasPermission("TownyGDR.Zona.Command")) {
					p.sendMessage(ChatColor.RED + "Non hai i permessi per usare questo commando!");
					return true;
				}
				
				
				//Il comando contiene "Zona"
				//Mi serve sapere cosa c'� dopo
				if(args.length < 1) {
					error(p);
				}
				
				//Ho 3 scelte tra cui: [id, nome, loc]
				//Prendi la Zona interessata.
				Zona zon = null;
				int offset = 0; //Per capire la posizione del comando add o altri
				
				switch(args[0]) {
					case "-id":{
						//prendi la zona secondo l'id
						int id = 0;
						try {
							id = Integer.parseInt(args[1]);
							zon = Zona.getByID(id);
							offset = 2;
						}catch(NumberFormatException e) {
							error(p);
							return true;
						}
					}break;
					
					case "-nome":{
						//prendi la zona secondo il nome
						zon = Zona.getZona(args[1]);
						offset = 2;
					}break;
					
					case "-loc":{
						//prendi la zona secondo la posizone del player
						//ho due possibili opzioni [-qua , -pos [x,z]
						if(args[2].equalsIgnoreCase("-qua")) {
							zon = Zona.getZonaByLocation(p.getLocation());
							offset = 2;
						}else if(args[2].equalsIgnoreCase("-pos")) {
							try {
								int x = Integer.parseInt(args[3]);
								int z = Integer.parseInt(args[4]);
								zon = Zona.getZonaByLocation(new Location(p.getLocation().getWorld(), x, 0, z));
								offset = 4;
							}catch(NumberFormatException e) {
								error(p);
								return true;
							}
						}
					}break;
					
					case "-create":{
						//crea una zona
						String nome = args[1];
						ZonaType type = ZonaType.valueOf(args[2]);
						try {
							zon = new Zona(nome, type);
						} catch (ExceptionZonaNameNotAvaiable e) {
							p.sendMessage("Nome non valido!");
						}
						return true;
					}
				}
				
				//Controlla se ho preso una zona
				if(zon == null) {
					p.sendMessage("Nessuna zona trovata!");
					return true;
				}
				
				//Dopo aver ottenuto la zona ho vari comandi in cascata... [add/remove/check/getinfo]
				switch(args[offset]) {
				
					case "save":{
						try {
							zon.save(new CustomConfig("Zone" + File.separatorChar + zon.getName() + "(" + zon.getID() + ")", TownyGDR.getInstance()));
							return true;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					case "add":{
						//Aggiungi alla zona trovata l'elemento d'area indicato se possibile
						//l'elemento d'area � indicato con [-qua , -pos x,z]
						//ho due possibili opzioni [-qua , -pos [x,z]
						if(args[offset + 1].equalsIgnoreCase("-qua")) {
							//aggiungi
							if(zon.addElementoArea(new ElementoArea(p.getLocation().getChunk()))) {
								p.sendMessage("Questa posizione � stata aggiunta alla zona");
							}else{
								p.sendMessage("Questa posizione � gi� presente nella zona, non � stata ulteriormente aggiunta!");
							}
						}else if(args[offset + 1].equalsIgnoreCase("-pos")) {
							try {
								int x = Integer.parseInt(args[offset + 2]);
								int z = Integer.parseInt(args[offset + 3]);
								if(zon.addElementoArea(new ElementoArea((new Location(p.getWorld(), x, 0, z)).getChunk()))) {
									p.sendMessage("Questa posizione � stata aggiunta alla zona");
								}else{
									p.sendMessage("Questa posizione � gi� presente nella zona, non � stata ulteriormente aggiunta!");
								}
							}catch(NumberFormatException e) {
								error(p);
								return true;
							}
						}
					}break;
					
					case "remove":{
						//Aggiungi alla zona trovata l'elemento d'area indicato se possibile
						//l'elemento d'area � indicato con [-qua , -pos x,z]
						//ho due possibili opzioni [-qua , -pos [x,z]
						if(args[offset + 1].equalsIgnoreCase("-qua")) {
							//aggiungi
							if(zon.removeElementoArea(new ElementoArea(p.getLocation().getChunk()))) {
								p.sendMessage("Questa posizione � stata rimossa  dalla zona");
							}else{
								p.sendMessage("Questa posizione NON c'� nella zona, non � stata rimossa!");
							}
						}else if(args[offset + 1].equalsIgnoreCase("-pos")) {
							try {
								int x = Integer.parseInt(args[offset + 2]);
								int z = Integer.parseInt(args[offset + 3]);
								if(zon.removeElementoArea(new ElementoArea((new Location(p.getWorld(), x, 0, z)).getChunk()))){
									p.sendMessage("Questa posizione � stata rimossa  dalla zona");
								}else{
									p.sendMessage("Questa posizione NON c'� nella zona, non � stata rimossa!");
								}
							}catch(NumberFormatException e) {
								error(p);
								return true;
							}
						}
					}break;
					
					case "check":{
						//Aggiungi alla zona trovata l'elemento d'area indicato se possibile
						//l'elemento d'area � indicato con [-qua , -pos x,z]
						//ho due possibili opzioni [-qua , -pos [x,z]
						if(args[offset + 1].equalsIgnoreCase("-qua")) {
							//aggiungi
							if(zon.contain(new ElementoArea(p.getLocation().getChunk()))) {
								p.sendMessage("Questa posizione � dentro la zona");
							}else{
								p.sendMessage("Questa posizione NON � dentro la zona");
							}
						}else if(args[offset + 1].equalsIgnoreCase("-pos")) {
							try {
								int x = Integer.parseInt(args[offset + 2]);
								int z = Integer.parseInt(args[offset + 3]);
								if(zon.contain(new ElementoArea((new Location(p.getWorld(), x, 0, z)).getChunk()))) {
									p.sendMessage("Questa posizione � dentro la zona");
								}else{
									p.sendMessage("Questa posizione NON � dentro la zona");
								}
							}catch(NumberFormatException e) {
								error(p);
								return true;
							}
						}
					}break;
					
					case "getinfo":{
						p.sendMessage("Info della zona: " + zon.getName());
						p.sendMessage("Type: " + zon.getType().toString());
						p.sendMessage("Type Luogo(-1 se non c'�)" + (zon.getLuogo() != null ? zon.getLuogo().getId() : -1));
						p.sendMessage("Id Luogo(-1 se non c'�)" + (zon.getLuogo() != null ? zon.getLuogo().getId() : -1));
						p.sendMessage("Id Zona: " + zon.getID());
						for(Sector sec : zon.getArea().keySet()) {
							for(ElementoArea ele : zon.getArea().get(sec)) {
								p.sendMessage("Chunk: " + ele.getX() + " : " + ele.getZ());
							}
						}
						
					}break;
				}
				
				
			}else{
				//Console
				
			}
		
		
		}catch(ArrayIndexOutOfBoundsException e) {
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
