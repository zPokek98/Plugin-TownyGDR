package it.TownyGDR;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import it.Library;
import it.MySQL.MySQL;
import it.TownyGDR.Command.City.CityCommand;
import it.TownyGDR.Command.Zona.ZonaCommand;
import it.TownyGDR.Event.EventPlayerManager;
import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Towny.Luogo;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.Zone.Zona;
import it.TownyGDR.Util.Exception.City.ExceptionCityImpossibleLoad;
import it.TownyGDR.Util.Exception.Zona.ExceptionZonaImpossibleToLoad;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

/*********************************************************************
 * @author: Elsalamander
 * @data: 27 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Plugin per il Towny.
 * La modalità in questione comprende molti oggetti per realizzare
 * le città/nazioni e le loro iterazioni.
 * 
 * In questa modalità sono presenti le città dei luoghi che vengono claimati
 * dai Sindaci delle città per costruire la propria citta con i sui cittadini
 * le città a sua volta sono luoghi nella quale ci sono certe regole di
 * building e iterazioni con altri player e NPC.
 * 
 * Più città compongono una nazione e quindi possibilità di creare gruppi
 * ancora più grandi di player/città, ci sono iterazioni fra città e nazione
 * come fra nazione e nazione.
 * 
 * L'obbiettivo del plugin è realizzare queste ultime, ovvero le città/nazioni
 * e i suoi componenti e ovviamente le loro iterazioni.
 * 
 * #Altre info------------------------------------------------------
 * La economia è supportata dalla vault, ma bisogna creare l'eonomia nel suo complesso
 * Come i permessi e la Chat.
 * 
 * 
 * #Gestione del Lavoro-----------------------------------------------
 * Scelta obbiettivi, se volete fare uno dei seguenti obbiettivi, scrivere il proprio nome affianco alla classe scelta
 * se manca e ritenete opportuno aggiungerne una aggiungetela con criterio! e mettete il vostro nome,
 * se invece riechiedete che uno vi da una mano a fare una classe scrivete il vostro nome e affianco "Aiuto" la persona
 * che lo aiuta deve scrivere il proprio nome a destra di "Aiuto" se ci sono più persone che lo aiutano mettete una virgola
 * di separazione.
 * 
 * Scaletta Obbiettivi: (Leggenda: "[ ]": non toccato | "[-]":Iniziato | "[x]":Finito | "[+]": Da controllare)
 * - [x]: Taggable
 * - [x]: Util
 * - [-]: PlayerData			("Elsalamander")
 * 		- [x]: Statistiche		("Elsalamander")
 * 			- [x]: KDA			("Elsalamander")
 * 
 * - [ ]: Casata
 * - [ ]: Eventi
 * - [-]: Towny
 * 		- [-]: City
 * 			- [x]: Area
 * 				- [x]: Area
 * 				- [x]: Lotto
 * 			- [x]: Membro							("Elsalamander" Da finire)
 * 				- [x]: MembroType "Enumerazione"	("Elsalamander")
 * 			- [ ]: Edifici
 * 				- [ ]: Municipio
 * 			- [x]: Impostazioni
 * 				- [x]: PvpOnCity
 * 			- [ ]: Regole
 * 
 * 		- [-]: Nazione
 * 			- [-]: Politici
 * 				- [ ]: Sovrano
 * 				- [ ]: Diplomatico
 * 			- [ ]: Diplomazia
 * 				- [x]: Relazioni
 * 			- [ ]: Votazione
 * 				- [ ]: Proposta
 * 					- [ ]: DichiarazioneGuerra
 * 		- [x]: Zone
 * 			- [x]: Zona
 * 			- [x]: ElementoArea
 * 			- [x]: Settore
 * 			- [x]: ZonaType
 * 			- [x]: Type
 * 				- [x]: ZonaCittadina
 * 			
 * 
 * Per evoluzioni e capire cosa si è fatto, solo per cose rilevnti o completamento
 * scriver cosa si è fatto e chi l'ha fatto in modo da capire chi e cosa ha fatto
 * 
 * - Elsalamander 27/02/2021: Fatto Scheletro Plugin
 * - Elsalamander 04/03/2021: Iniziato PlayerData, City, Membri.
 * 
 ********************************************************************/

public class TownyGDR extends JavaPlugin{
	
	/**********************************
	 * Variabili globali alla libreria
	 **********************************/
	protected MySQL mySQL;
	protected boolean debug;
	public static Economy econ = null;
    public static Permission perms = null;
    public static Chat chat = null;
	
	
	/**********************************
	 * Variabili statiche/private
	 **********************************/
	private static TownyGDR instance;
	private static World TownyWorld;
	private ConsoleCommandSender send;
	private FileConfiguration config;
	private Library library;
	private MySQL database;
	
	//Funzione LOAD - ENABLE - DISABLE **********************************************************************
	public void onLoad() {
		instance = this; //inizializzo l'istanza
	}
	
	//Enable
	public void onEnable() {
		send = Bukkit.getConsoleSender(); //Inizializza consoleSender per mandare messagi nella console
		
		PluginDescriptionFile desc = this.getDescription();
		send.sendMessage(ChatColor.GRAY + String.format("[%s]------- Avvio Plugin: " + desc.getName() + " V:" + desc.getVersion() + "-------", getDescription().getName()));
		
		//prndi il file di config
		//controlla se esiste il file
		//if(!(new File(this.getDataFolder().toString() + File.separatorChar + "config.yml")).exists()) {
		//	this.getConfig().options().copyDefaults(true);
		//	this.saveConfig();
		//}
		//this.config = this.getConfig();
		
		//Cattura il mondo
		TownyWorld = Bukkit.getWorld("Towny");
		
		//Carica la libreria
		try {
			this.library = this.getLibraryInstance();
		}catch(RuntimeException e) {
			send.sendMessage(ChatColor.GRAY + String.format("[%s] - Disabilitazione perchè non è stata trovata la dipenza Library!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
		}
		
		send.sendMessage(ChatColor.GRAY + String.format("[%s] Load Vault...", getDescription().getName()));
		if(!setupEconomy()){
			//send.sendMessage(ChatColor.GRAY + String.format("[%s] - Disabilitazione perchè non è stata trovata la dipenza Vault!", getDescription().getName()));
			//getServer().getPluginManager().disablePlugin(this);
            //return;
        }
		
        if(!setupPermissions()) /*getServer().getPluginManager().disablePlugin(this)*/;
        if(!setupChat()) /*getServer().getPluginManager().disablePlugin(this)*/;
		
		//prendi l'oggetto mySQL
        send.sendMessage(ChatColor.GRAY + String.format("[%s]Load MySQL...", getDescription().getName()));
		this.database = this.library.getMySQL();
		
		//registra gli eventi
		send.sendMessage(ChatColor.GRAY + String.format("[%s]Load Eventi...", getDescription().getName()));
		this.registerEvent();
		
		//registra comandi
		send.sendMessage(ChatColor.GRAY + String.format("[%s]Load Command...", getDescription().getName()));
		this.registerCommand();
		
		//Carica le zone
		send.sendMessage(ChatColor.GRAY + String.format("[%s]Load Zone...", getDescription().getName()));
		try{
			Zona.initZona();
		}catch (IOException e){
			e.printStackTrace();
		} catch (ExceptionZonaImpossibleToLoad e) {
			//Impossibile caricare la zona
			send.sendMessage(ChatColor.RED + String.format("[%s]Impossibile caricare la zona!!!", getDescription().getName()));
			send.sendMessage(ChatColor.RED + String.format("[%s]Chiusura server...", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
		}
		
		//Carica le città anche se probabilmente sono già state caricate tremite le zone
		send.sendMessage(ChatColor.GRAY +  String.format("[%s]Load City...", getDescription().getName()));
		try{
			City.initCity();
		}catch (ExceptionCityImpossibleLoad e){
			//Impossibile caricare una città...
			getServer().getPluginManager().disablePlugin(this);
		}
		
	}

	//Disable
	public void onDisable() {
		send.sendMessage(ChatColor.GRAY + "------- Disabilitazione -------");
		
		//Salva tutti i dati
		//Ma prima kicka tutti i player
		//e salva i loro dati
		for (Player target : Bukkit.getServer().getOnlinePlayers()) {
			PlayerData pd = PlayerData.getPlayerData(target);
			try{
				pd.save();
			}catch(IOException e){
				send.sendMessage(String.format("[%s] Impossibile salvare i dati di: ", target.getName()));
			}  //Salva i dati del player
		    target.kickPlayer("Sei stato/a kickato perchè il server si sta chiudendo!");
		}
		
		//Salva il file di config
		//this.saveConfig();
		
		//Salva le zone
		Zona.saveAll();
		
		//Salva tutti i luoghi
		Luogo.saveAll();
		
	}
		
	
	//Funzioni di Supporto ************************************************************************************
	/**
	 * Ritorna listanza del plugin
	 * @return
	 */
	public static TownyGDR getInstance() {
		return instance;
	}
	
	/**
	 * Ritorna listanza del MySQL
	 * @return
	 */
	public MySQL getMySQL() {
		return this.database;
	}
	
	/**
	 * Ritorna il file di config principale
	 * @return
	 */
	public FileConfiguration getConfig() {
		return this.config;
	}
	
	/**
	 * Prendi listanza del plugin
	 * @return
	 */
	private Library getLibraryInstance() {
		 Plugin plugin = getServer().getPluginManager().getPlugin("Library");
		 if(plugin instanceof Library) {
			 return (Library) plugin;
		 }else{
			 throw new RuntimeException("Libreria non trovata!!");
		 }
	}
	
	/**
	 * Registra gli eventi del plugin
	 */
	private void registerEvent() {
		PluginManager pl = this.getServer().getPluginManager();
		pl.registerEvents(new EventPlayerManager(), this);
		
	}
	
	/**
	 * Registra i commandi
	 */
	private void registerCommand() {
		this.getCommand("Zona").setExecutor(new ZonaCommand());
		this.getCommand("City").setExecutor(new CityCommand());
	}

	/**
	 * Carica la vault e prni la classe Economy
	 * @return
	 */
	private boolean setupEconomy() {
		/*
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        send.sendMessage(ChatColor.GRAY + "Load Economy class...");
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
        */
		//Plugin vault = getServer().getPluginManager().getPlugin("Vault");
        //if (vault == null) {
        //  return false;
        //}
        //myEco = new MyEconomy();
        //getServer().getServicesManager().register(Economy.class, myEco, vault, ServicePriority.Highest);
		
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            econ = economyProvider.getProvider();
        }

        return (econ != null);
    }
 
	/**
	 * Carica la classe Chat della vault
	 * @return
	 */
    private boolean setupChat() {
    	RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }
        return (chat != null);
    }
 
    /**
     * Carica la classe Permission della vault
     * @return
     */
    private boolean setupPermissions() {
    	RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            perms = permissionProvider.getProvider();
        }
        return (perms != null);
    }

	/**
	 * @return
	 */
	public static World getTownyWorld() {
		return TownyWorld;
	}
}
