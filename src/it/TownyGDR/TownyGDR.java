package it.TownyGDR;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import it.Library;
import it.MySQL.MySQL;
import it.TownyGDR.Command.CommandManager;
import it.TownyGDR.Event.EventPlayerManager;

/*********************************************************************
 * @author: Elsalamander
 * @data: 27 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Plugin per il Towny.
 * La modalit� in questione comprende moolti oggetti per realizzare
 * le citt�/nazioni e le loro iterazioni.
 * 
 * In questa modalit� sono presenti le citt� dei luoghi che vengono claimati
 * dai Sindaci delle citt� per costruire la propria citta con i sui cittadini
 * le citt� a sua volta sono luoghi nella quale ci sono certe regole di
 * building e iterazioni con altri player e NPC.
 * 
 * Pi� citt� compongono una nazione e quindi possibilit� di creare gruppi
 * ancora pi� grandi di player/citt�, ci sono iterazioni fra citt� e nazione
 * come fra nazione e nazione.
 * 
 * L'obbiettivo del plugin � realizzare queste ultime, ovvero le citt�/nazioni
 * e i suoi componenti e ovviamente le loro iterazioni.
 * 
 * #Altre info------------------------------------------------------
 * La economia � supportata dalla vault
 * Come i permessi e la Chat.
 * 
 * E' presente il DataBase, il salvataggio dei dati � regolato come
 * scritto nella classe MySQL e MySQLConnection
 * 
 * #Gestione del Lavoro-----------------------------------------------
 * Scelta obbiettivi, se volete fare uno dei seguenti obbiettivi, scrivere il proprio nome affianco alla classe scelta
 * se manca e ritenete opportuno aggiungerne una aggiungetela con criterio! e mettete il vostro nome,
 * se invece riechiedete che uno vi da una mano a fare una classe scrivete il vostro nome e affianco "Aiuto" la persona
 * che lo aiuta deve scrivere il proprio nome a destra di "Aiuto" se ci sono pi� persone che lo aiutano mettete una virgola
 * di separazione.
 * 
 * Scaletta Obbiettivi: (Leggenda: "[ ]": non toccato | "[-]":Iniziato | "[x]":Finito | "[+]": Da controllare)
 * - [x]: Taggable
 * - [-]: Util
 * - [ ]: PlayerData
 * 		- [-]: Statistiche
 * 			- [ ]: KDA
 * 
 * - [ ]: Casata
 * - [ ]: Eventi
 * - [-]: Towny
 * 		- [-]: City
 * 			- [ ]: Area
 * 				- [ ]: Lotto
 * 			- [-]: Membro
 * 				- [x]: MembroType "Enumerazione"
 * 				- [ ]: Sindaco
 * 				- [ ]: Cittadino
 * 			- [ ]: Edifici
 * 				- [ ]: Municipio
 * 			- [ ]: Impostazioni
 * 				- [ ]: PvpOnCity
 * 			- [ ]: Regole
 * 		- [-]: Nazione
 * 			- [-]: Politici
 * 				- [ ]: Sovrano
 * 				- [ ]: Diplomatico
 * 			- [ ]: Diplomazia
 * 				- [x]: Relazioni
 * 			- [ ]: Votazione
 * 				- [ ]: Proposta
 * 					- [ ]: DichiarazioneGuerra
 * 			
 * 
 * Per evoluzioni e capire cosa si � fatto, solo per cose rilevnti o completamento
 * scriver cosa si � fatto e chi l'ha fatto in modo da capire chi e cosa ha fatto
 * 
 * - Elsalamander 27/2/2020: Fatto Scheletro Plugin
 * 
 ********************************************************************/

public class TownyGDR extends JavaPlugin{
	
	/**********************************
	 * Variabili globali alla libreria
	 **********************************/
	protected MySQL mySQL;
	protected boolean debug;
	
	
	/**********************************
	 * Variabili statiche/private
	 **********************************/
	private static TownyGDR instance;
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
		send.sendMessage(ChatColor.GRAY + "------- Avvio Plugin: " + desc.getName() + " V:" + desc.getVersion() + "-------");
		
		//prndi il file di config
		this.config = this.getConfig();
		
		//Carica la libreria
		try {
			this.library = this.getLibraryInstance();
		}catch(RuntimeException e) {
			getServer().getPluginManager().disablePlugin(this);
		}
		
		//prendi l'oggetto mySQL
		this.database = this.library.getMySQL();
		
		//registra gli eventi
		this.registerEvent();
		
		//registra comandi
		this.registerCommand();
		
		//...
		
	}

	//Disable
	public void onDisable() {
		send.sendMessage(ChatColor.GRAY + "------- Disabilitazione -------");
		//Salva tutti i dati
		//...
		
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
		this.getCommand("Towny").setExecutor(new CommandManager());
	}

}