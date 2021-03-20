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
				//Mi serve sapere cosa c'è dopo
				if(args.length < 1) {
					error(p);
				}
				
				//Ho 3 scelte tra cui: [id, nome, loc]
				//Prendi la Zona interessata.
				Zona zon = null;
				int offset = 0; //Per capire la posizione del comando add o altri
				
				switch(args[0]) {
					case "-id":{
						zon = this.getZonaByID(args[1]);
						offset = 2;
					}break;
					
					case "-nome":{
						//prendi la zona secondo il nome
						zon = Zona.getZona(args[1]);
						offset = 2;
					}break;
					
					case "-loc":{
						this.getZonaByLocation(args, p);
						if(args[2].equalsIgnoreCase("-qua")) {
							offset = 2;
						}else if(args[2].equalsIgnoreCase("-pos")) {
							offset = 4;
						}else{
							//Sintassi non corretta
							error(p);
							return true;
						}
					}break;
					
					case "-create":{
						//crea una zona
						String nome = args[1];
						ZonaType type = ZonaType.valueOf(args[2]);
						try {
							zon = new Zona(nome, type);
							p.sendMessage("Zona creata con successo, ID zona creata: " + zon.getID());
						} catch (ExceptionZonaNameNotAvaiable e) {
							p.sendMessage("Nome non valido!");
						}
						return true;
					}
					
					case "-help":{
						this.help(p);
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
				
					//Salva la Zona
					case "save":{
						try {
							zon.save(new CustomConfig("Zone" + File.separatorChar + zon.getName() + "(" + zon.getID() + ")", TownyGDR.getInstance()));
							return true;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					//Aggiungi alla zona una posizione
					case "add":{
						//Aggiungi alla zona trovata l'elemento d'area indicato se possibile
						//l'elemento d'area è indicato con [-qua , -pos x,z]
						//ho due possibili opzioni [-qua , -pos [x,z]
						if(args[offset + 1].equalsIgnoreCase("-qua")) {
							//aggiungi
							if(zon.addElementoArea(new ElementoArea(p.getLocation().getChunk()))) {
								p.sendMessage("Questa posizione è stata aggiunta alla zona");
							}else{
								p.sendMessage("Questa posizione è già presente nella zona, non è stata ulteriormente aggiunta!");
							}
						}else if(args[offset + 1].equalsIgnoreCase("-pos")) {
							try {
								int x = Integer.parseInt(args[offset + 2]);
								int z = Integer.parseInt(args[offset + 3]);
								if(zon.addElementoArea(new ElementoArea((new Location(p.getWorld(), x, 0, z)).getChunk()))) {
									p.sendMessage("Questa posizione è stata aggiunta alla zona");
								}else{
									p.sendMessage("Questa posizione è già presente nella zona, non è stata ulteriormente aggiunta!");
								}
							}catch(NumberFormatException e) {
								error(p);
								return true;
							}
						}
						return true;
					}//break;
					
					//Rimmuovi la posizione data dalla zona
					case "remove":{
						//Aggiungi alla zona trovata l'elemento d'area indicato se possibile
						//l'elemento d'area è indicato con [-qua , -pos x,z]
						//ho due possibili opzioni [-qua , -pos [x,z]
						if(args[offset + 1].equalsIgnoreCase("-qua")) {
							//aggiungi
							if(zon.removeElementoArea(new ElementoArea(p.getLocation().getChunk()))) {
								p.sendMessage("Questa posizione è stata rimossa  dalla zona");
							}else{
								p.sendMessage("Questa posizione NON c'è nella zona, non è stata rimossa!");
							}
						}else if(args[offset + 1].equalsIgnoreCase("-pos")) {
							try {
								int x = Integer.parseInt(args[offset + 2]);
								int z = Integer.parseInt(args[offset + 3]);
								if(zon.removeElementoArea(new ElementoArea((new Location(p.getWorld(), x, 0, z)).getChunk()))){
									p.sendMessage("Questa posizione è stata rimossa  dalla zona");
								}else{
									p.sendMessage("Questa posizione NON c'è nella zona, non è stata rimossa!");
								}
							}catch(NumberFormatException e) {
								error(p);
								return true;
							}
						}
						return true;
					}//break;
					
					//Controlla se la posizione data è dentro la zona
					case "check":{
						//Aggiungi alla zona trovata l'elemento d'area indicato se possibile
						//l'elemento d'area è indicato con [-qua , -pos x,z]
						//ho due possibili opzioni [-qua , -pos [x,z]
						if(args[offset + 1].equalsIgnoreCase("-qua")) {
							//aggiungi
							if(zon.contain(new ElementoArea(p.getLocation().getChunk()))) {
								p.sendMessage("Questa posizione è dentro la zona");
							}else{
								p.sendMessage("Questa posizione NON è dentro la zona");
							}
						}else if(args[offset + 1].equalsIgnoreCase("-pos")) {
							try {
								int x = Integer.parseInt(args[offset + 2]);
								int z = Integer.parseInt(args[offset + 3]);
								if(zon.contain(new ElementoArea((new Location(p.getWorld(), x, 0, z)).getChunk()))) {
									p.sendMessage("Questa posizione è dentro la zona");
								}else{
									p.sendMessage("Questa posizione NON è dentro la zona");
								}
							}catch(NumberFormatException e) {
								error(p);
								return true;
							}
						}
						return true;
					}//break;
					
					//Informazioni della zona trovata
					case "getinfo":{
						p.sendMessage("Info della zona: " + zon.getName());
						p.sendMessage("Type: " + zon.getType().toString());
						p.sendMessage("Type Luogo(-1 se non c'è)" + (zon.getLuogo() != null ? zon.getLuogo().getId() : -1));
						p.sendMessage("Id Luogo(-1 se non c'è)" + (zon.getLuogo() != null ? zon.getLuogo().getId() : -1));
						p.sendMessage("Id Zona: " + zon.getID());
						for(Sector sec : zon.getArea().keySet()) {
							p.sendMessage("Settore: " + sec.toString());
							for(ElementoArea ele : zon.getArea().get(sec)) {
								p.sendMessage("Chunk: " + ele.getX() + " : " + ele.getZ());
							}
						}
						return true;
					}//break;
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
	 * Comando help per la zona
	 * @param p
	 */
	private void help(Player p) {
		p.sendMessage(ChatColor.GREEN + "----- Zona Help ------");
		p.sendMessage(ChatColor.GREEN + "/zona -[id/nome] <arg> [add/remove/check] -qua");
		p.sendMessage(ChatColor.GREEN + "/zona -[id/nome] <arg> [add/remove/check] -pos <args>");
		p.sendMessage(ChatColor.GREEN + "/zona -loc -qua [add/remove/check] -qua");
		p.sendMessage(ChatColor.GREEN + "/zona -loc -qua [add/remove/check] -pos <args>");
		p.sendMessage(ChatColor.GREEN + "/zona -loc -pos <args> [add/remove/check] -qua");
		p.sendMessage(ChatColor.GREEN + "/zona -loc -pos <args> [add/remove/check] -pos <args>");
		p.sendMessage(ChatColor.GREEN + "/zona -create <nome> <type>");
		p.sendMessage(ChatColor.GREEN + "/zona -[id/nome] <arg> save");
		p.sendMessage(ChatColor.GREEN + "/zona -loc -qua save");
		p.sendMessage(ChatColor.GREEN + "/zona -loc -pos <args> save");
		p.sendMessage(ChatColor.GREEN + "/zona -[id/nome] <arg> getinfo");
		p.sendMessage(ChatColor.GREEN + "/zona -loc -qua getinfo");
		p.sendMessage(ChatColor.GREEN + "/zona -loc -pos <args> getinfo");
		
	}

	/**
	 * @param sender
	 */
	private void error(CommandSender sender) {
		sender.sendMessage("Sintassi non valida per eseguire il commando!");
	}
	
	//Prendi la zona in base all'id dato
	/**
	 * Ritorna la zona in base alle info date
	 * @param id
	 * @return
	 */
	private Zona getZonaByID(String id) {
		try {
			return Zona.getByID(Integer.parseInt(id));
		}catch(NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * Ritorna la zona in base alle info date
	 * @param args
	 * @param p
	 * @return
	 */
	private Zona getZonaByLocation(String args[], Player p) {
		//prendi la zona secondo la posizone del player
		//ho due possibili opzioni [-qua , -pos [x,z]
		if(args[1].equalsIgnoreCase("-qua")) {
			return Zona.getZonaByLocation(p.getLocation());
		}else if(args[1].equalsIgnoreCase("-pos")) {
			try {
				int x = Integer.parseInt(args[2]);
				int z = Integer.parseInt(args[3]);
				return Zona.getZonaByLocation(new Location(p.getLocation().getWorld(), x, 0, z));
				
			}catch(NumberFormatException e) {
				return null;
			}
		}
		return null;
	}

}
