/**
 * 
 */
package it.TownyGDR.PlayerData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import it.CustomConfig.CustomConfig;
import it.TownyGDR.TownyGDR;
import it.TownyGDR.PlayerData.Statistiche.Stats.KDA;
import it.TownyGDR.Tag.Taggable;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Util.Util;
import it.TownyGDR.Util.Save.Salva;
import net.milkbowl.vault.economy.EconomyResponse;

/*********************************************************************
 * @author: Elsalamander
 * @data: 27 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Classe che "Estende" la classe player anche se non esplicitamente
 * Verrano racchiuse tutte le informazioni riguardanti il player
 * e le sue statistiche di rilevanza e altri dati.
 * 
 * 
 *********************************************************************/
@SuppressWarnings("unused")
public class PlayerData implements Salva<CustomConfig>, Taggable{
	
	//Variabile di cache per il playerData per eventuali get, e ottimizzazione tempi
	private static ArrayList<PlayerData> ListPlayerData = new ArrayList<PlayerData>(); //mi obbliga a creare la
																					   //funzione "equals"
	//Contenitore dei Tag per il player
	private static ArrayList<String> TagList = new ArrayList<String>();
	static{		//Immetti i tag nel contenitore dei Tag
		TagList.add("%Player.name%");
		TagList.add("%Player.uuid%");
		TagList.add("%...%");
	}
	
	//Variabili oggetto giocatore**********
	private Player player;	//Puntatore cache all'oggetto player per otterene il player secondo il server
	private UUID   uuid;    //UUID del giocatore senza ricorrere a funzioni strane, usato poi per il 
							//salvataggio e localizzazione del suo file e righa nel database.
	private long   idCity;	//Id della città se ne fa parte, caso contrario -1.
	private City   city;	//Puntatore oggetto città se è in una, caso contrario null.
	//Statistiche
	private KDA    kda;		//Kill/Death/"Assist?" del player
	
	
	/**
	 * Costruttore per il PlayerData, senza informazioni apparte il suo UUID
	 * tutto il resto ha valore di default.
	 * @param player
	 */
	public PlayerData(Player player){
		if(player != null) {
			this.player = player;
			this.uuid   = player.getUniqueId();
		}else{
			this.player = null;
			this.uuid   = null;
		}
		this.idCity = -1;
		this.city   = null;
		this.kda    = new KDA();
		
		//Aggiungilo alla cache.
		ListPlayerData.add(this);
	}
	
	/**
	 * Ritorna l'oggetto player per il server.
	 * @return
	 */
	public Player getPlayer() {
		return this.player;
	}
	
	/**
	 * Ritorna l'uuid del playerData.
	 * @return
	 */
	public UUID getUUID() {
		return this.uuid;
	}
	
	/**
	 * Ritorna se è lo stesso player con il confronto degli uuid
	 * non serve loggetto player.
	 * @param pd
	 * @return
	 */
	public boolean equals(PlayerData pd) {
		return this.uuid.equals(pd.getUUID());
	}

	@Override
	/**
	 * Salva i dati del giocatore del file/database specificato
	 * Se è null, vengono salvati i dati su file, nel modo 
	 * standard.
	 */
	public void save(CustomConfig database) throws IOException{
		//prendi
		CustomConfig customConfig;
		FileConfiguration config;
		if(database == null) {
			customConfig = new CustomConfig("PlayerData/" + this.uuid , true, TownyGDR.getInstance());
			config = customConfig.getConfig();
		}else{
			customConfig = database;
			config = customConfig.getConfig();
		}
		//salva i dati player
		config.set("UUID", this.uuid.toString());
		config.set("IdCity", this.idCity);
		
		//Salva id città
		config.set("City", this.idCity);
		
		//Salva le statistiche
		this.kda.save(config.getConfigurationSection("Statistiche.KDA"));
		//...
		
		//Salva i dati nel file.
		if(!customConfig.save()) {
			throw new IOException(); //Errore nel salvataggio!
		}
	}

	@Override
	public void load(CustomConfig database) {
		//prendi
		CustomConfig customConfig;
		FileConfiguration config;
		if(database == null) {
			customConfig = new CustomConfig("PlayerData/" + this.uuid , true, TownyGDR.getInstance());
			config = customConfig.getConfig();
		}else{
			customConfig = database;
			config = customConfig.getConfig();
		}
		
		//Leggi l'id della città, prendi il suo puntatore, e verifica se esiste in caso contrario metti
		//id città a -1
		this.idCity = config.getInt("IdCity", -1);
		if(this.idCity != -1) {
			this.city = City.getByID(idCity);
			if(this.city == null){
				this.idCity = -1;
			}
		}
		
		//Carica le statistiche
		this.kda.load(config.getConfigurationSection("Statistiche.KDA"));
		//...
		
	}

	/**
	 * Carica il player tramite un uuid
	 * @param uuid
	 * @return
	 */
	public static PlayerData loadPlayerData(UUID uuid) {
		PlayerData pd = new PlayerData(null);
		pd.uuid = uuid;
		pd.load(new CustomConfig("PlayerData/" + pd.uuid , true, TownyGDR.getInstance()));
		return pd;
	}
	
	/**
	 * Ritorna il playerData con questo uuid se esiste
	 * @param playerUuid
	 * @return
	 */
	public static PlayerData getFromUUID(UUID playerUuid) {
		for(PlayerData pd : ListPlayerData) {
			if(pd.getUUID().equals(playerUuid)) {
				return pd;
			}
		}
		if(PlayerData.checkExistThisPlayer(playerUuid)) {
			return PlayerData.loadPlayerData(playerUuid);
		}
		return null;
	}
	
	/**
	 * Controlla se questo player con l'uuid esiste un suo salvataggio
	 * @param uuid
	 * @return
	 */
	public static boolean checkExistThisPlayer(UUID uuid) {
		String[] paths = Util.getListNameFile("PlayerData");
		for(String str : paths) {
			str = str.substring(0,str.length() - 4);
			if(str.equals(uuid.toString())) {
				return true;
			}
		}
		return false;
	}
	
	//***************************************************************************** Economy player
	/** 
	 * Ritorna i soldi del player.
	 */
	public double getBalance() {
		return 1000;
		//return TownyGDR.myEco.getBalance(player);
	}
	
	/**
	 * Aggiungi i soldi al player
	 * @param val
	 * @return
	 */
	public boolean addMoney(double val) {
		if(val > 0) {
			/*EconomyResponse result = */TownyGDR.myEco.depositPlayer(player, val);
			return true;
		}
		return false;
	}
	
	/**
	 * Togli soldi al player
	 * @param val
	 * @return
	 */
	public boolean withdrawMoney(double val) {
		if(val > 0) {
			TownyGDR.myEco.withdrawPlayer(player, val);
			return true;
		}
		return false;
	}
	
	
	//...operare con la vault
	
	//************************************************************************************* City interaction
	/**
	 * Imposta il valore della citta al player
	 * @param i
	 */
	public void setCity(City city) {
		if(city != null) {
			this.city = city;
			this.idCity = this.city.getId();
		}else {
			this.idCity = -1;
			this.city = null;
		}
	}

	/**
	 * Ritorna la città a cui appartiene, null se non appartiene a nessuna città.
	 * @return
	 */
	public City getCity() {
		if(this.city == null) {
			if(this.idCity != -1) {
				return City.getByID(idCity);
			}
		}else{
			return this.city;
		}
		return null;
	}
	
	
	//******************************************************************************************* Statistiche
	/**
	 * Ritorna la statistica KDA
	 * @return the kda
	 */
	public KDA getKda() {
		return kda;
	}
	
	//******************************************************************************************* TAG
	@Override
	public boolean hasTag(String tag) {
		return PlayerData.TagList.contains(tag);
	}

	@Override
	public ArrayList<String> getTagList() {
		return PlayerData.TagList;
	}

	@Override
	public String getValueFromTag(String str) {
		if(str == null || str.length() <= 2) return "(Error Invalid code: " + str + ")";
		
		//Si riferisce al KDA?
		if(str.contains("KDA")) {
			//mantieni solo la parte dal KDA in poi
			return this.kda.getValueFromTag(str.replaceFirst("Player.", ""));
		}
		//...
		
		
		//non ha che fare con le statistiche, cerca il valore...
		switch(str) {
			case "%Player.name%":{
				return this.player.getName();
			}
			case "%Player.uuid%":{
				return this.uuid.toString();
			}
			//...
			
		}
		
		return "(Error Invalid code: " + str + ")";
	}

	/**
	 * @param p
	 * @return
	 */
	public static PlayerData getFromPlayer(Player p) {
		PlayerData tmp = PlayerData.getFromUUID(p.getUniqueId());
		return tmp == null ? new PlayerData(p) : tmp;
	}

}
