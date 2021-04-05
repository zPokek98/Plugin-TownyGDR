/**
 * 
 */
package it.TownyGDR.Towny.City.Membri;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import it.TownyGDR.TownyGDR;
import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Towny.City.City;

/*********************************************************************
 * @author: Elsalamander
 * @data: 3 apr 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Classe cupporto per l'invito di player nella città
 * Devo sapere chi invita chi.
 * E devo vere un processo che fa scadere gli inviti.
 * (Si poteva tenere a memoria l'orario dell'invito, ma se non viene 
 *  accettato o che so altro, non verrà mai rimosso, ecco perchè di
 *  un semplice Task)
 *  
 * Chiave: Plyer Invitato
 * Argomento: Tutte le informazioni dell'invito
 * 
 ********************************************************************/
public class Invite {

	protected static ConcurrentHashMap<PlayerData, Data> map = new ConcurrentHashMap<PlayerData, Data>();
	
	public static void addInvite(PlayerData sender, PlayerData invitato, City onCity) {
		//Controlla che questo player non è già stato invitato nella stessa città
		Data data = map.get(invitato);
		if(data != null) {
			//è stato già invitato, ma dalla stessa città?
			if(data.city.equals(onCity)) {
				//si dalla stessa città
				sender.getPlayer().sendMessage("Il player e' stato già invitato!");
			}else{
				//non è la stessa città, invitalo
				map.put(invitato, new Data(sender, invitato, onCity));
				
				//Manda i messaggi
				sender.getPlayer().sendMessage("Il player e' stato invitato!");
				Player send = invitato.getPlayer();
				send.sendMessage("Sei stato invitato nella citta': " + onCity.getName());
				Bukkit.getScheduler().runTaskLater(TownyGDR.getInstance(), new TaskInvito(invitato), 1200);
			}
		}else{
			//non era mai stato invitato negli ultimi minuti
			data = new Data(sender, invitato, onCity);
			map.put(invitato, data);
			
			//Manda i messaggi
			sender.getPlayer().sendMessage("Il player e' stato invitato!");
			invitato.getPlayer().sendMessage("Sei stato invitato nella citta': " + onCity.getName());
			Bukkit.getScheduler().runTaskLater(TownyGDR.getInstance(), new TaskInvito(invitato), 1200);
			
		}
	}
	
	
	public static void accept(PlayerData invitato) {
		//controlla che esiste il suo invito.
		Data data = map.get(invitato);
		if(data != null) {
			//se c'è l'invito deve essere valido.
			
			//controlla se fa parte di una città o no
			if(invitato.getCity() != null) {
				//fa già parte di una città!
				Player send = invitato.getPlayer();
				send.sendMessage("Se vuoi accettare prima e' meglio che esci dalla citta' attuale!");
				send.sendMessage("Esci dalla città con /city leave");
				send.sendMessage("Poi ripeti il comando /city inviteaccept, per accettare l'invito!");
				
			}else{
				//non fa parte di una città
				data.city.addMembro(invitato);
				
				Player send = invitato.getPlayer();
				send.sendMessage("Ora fai parte della citta': " + data.city.getName());
				
				map.remove(invitato);
			}
		}else{
			Player send = invitato.getPlayer();
			send.sendMessage("Non hai inviti al momento!");
		}
	}
	
	public static void deny(PlayerData invitato) {
		//controlla che esiste il suo invito.
		Data data = map.get(invitato);
		if(data != null) {
			//se c'è l'invito deve essere valido.
			
			//rimuovi l'invito
			map.remove(invitato);
			
			Player send = invitato.getPlayer();
			send.sendMessage("Hai rifiutato l'invito!");
			
		}else{
			Player send = invitato.getPlayer();
			send.sendMessage("Non hai inviti al momento!");
		}
	}
	
}

class Data{
	public Data(PlayerData sender, PlayerData invitato, City onCity) {
		this.sender = sender;
		this.invitato = invitato;
		this.city = onCity;
		this.time = LocalDateTime.now();
	}
	protected PlayerData sender;
	protected PlayerData invitato;
	protected City city;
	protected LocalDateTime time;
}

class TaskInvito implements Runnable{
	
	private PlayerData rimuovi;
	
	public TaskInvito(PlayerData rimuovi) {
		this.rimuovi = rimuovi;
	}

	@Override
	public void run(){
		Invite.map.remove(rimuovi);
	}
	
}
