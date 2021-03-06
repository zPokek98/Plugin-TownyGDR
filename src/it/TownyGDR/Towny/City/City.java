/**
 * 
 */
package it.TownyGDR.Towny.City;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import it.CustomConfig.CustomConfig;
import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Tag.Taggable;
import it.TownyGDR.Towny.City.Area.Area;
import it.TownyGDR.Towny.City.Edifici.Municipio.Municipio;
import it.TownyGDR.Towny.City.Impostazioni.Impostazioni;
import it.TownyGDR.Towny.City.Membri.Membro;
import it.TownyGDR.Towny.City.Membri.Ruoli.Sindaco;
import it.TownyGDR.Towny.City.Regole.Regole;
import it.TownyGDR.Util.Util;
import it.TownyGDR.Util.Save.Salva;

/*********************************************************************
 * @author: Elsalamander
 * @data: 27 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Classe che descrivi la citt� nel suo insieme.
 * La citt� avr� bisogno di parecchie classi di supporto tra cui:
 * - Membri (Sindaco,cittadino,...)
 * - Area
 * - Regole
 * - Impostazioni
 * - Edifici
 * - Nazione
 * 
 *********************************************************************/
public class City implements Salva<CustomConfig>, Taggable{
	
	//Variabile di cache per le citt� per ottimizzare tempi e memoria
	private static ArrayList<City> ListCity = new ArrayList<City>();
	
	//Contenitore dei Tag per la city
	private static ArrayList<String> TagList = new ArrayList<String>();
	static{		//Immetti i tag nel contenitore dei Tag
		TagList.add("%City.name%");
		TagList.add("%City.id%");
		TagList.add("%...%");
	}
	
	//Variabili oggetto
	private int id;
	private String name;
	private String descrizione;
	
	//Membri citt�
	private ArrayList<Membro> membri;
	
	//Area della citt�
	private Area area;
	
	//Edifici
	private Municipio municipio;
	
	//Impostazioni
	private Impostazioni impostazioni;
	
	//Regole
	private Regole regole;
	
	/**
	 * Costruttore PRIVATO per il caricamento della citt�,
	 * la costruzione della citt� e demandato a una funzione
	 * "createCity"
	 */
	private City() {
		this.id 		 = -1;
		this.name 		 = null;
		this.descrizione = "";
		this.membri 	 = new ArrayList<Membro>();
		this.area 		 = new Area();
		this.municipio	 = new Municipio();
		this.impostazioni= new Impostazioni();
		this.regole		 = new Regole();
	}
	
	/**
	 * Creare una citt� da zero
	 * Tutte le impostazioni della citt� saranno quelle di default
	 * Il fondatore � il primo sindaco, � l'unico membro iniziale
	 * @param pd
	 * @return
	 */
	public City createCity(PlayerData pd, String nomeCitt�) {
		City city = new City();
		//Prendi l'id massimo fra le citt�
		city.id = City.getMaxID() + 1;
		
		//Il fondatore � automaticamente il sindaco
		city.membri.add(new Sindaco(pd.getUUID()));
		city.name = nomeCitt�;
		
		//Area inizliale? Al momento non c'� claim ??????????????
		//...
		
		try{
			city.save(null); //pu� anche essere null l'argomento
		}catch(IOException e){
			e.printStackTrace();
		}
		return city;
	}
	
	/**
	 * Ritorna il massimo id raggiunto.
	 * Nel caso per fondare una nuova citt� bisogna sommare +1 dopo.
	 * @return
	 */
	private static int getMaxID() {
		//Dato che le citt� posso anche non essere cariche nella RAM cerco in tutti i file di salvataggio
		int tmp = 0;
		String[] files = Util.getListNameFile("City");
		for(String str : files) {
			CustomConfig customConfig = new CustomConfig("City/" + str);
			FileConfiguration config = customConfig.getConfig();
			if(config.getInt("ID") > tmp) {
				tmp = config.getInt("ID");
			}
		}
		return tmp;
	}

	/**
	 * Ritorna se 2 citt� sono uguali in base all'id
	 * @param city
	 * @return
	 */
	public boolean equals(City city) {
		return this.id == city.getId();
	}

	@Override
	public void save(CustomConfig database) throws IOException {
		//prendi
		CustomConfig customConfig;
		FileConfiguration config;
		if(database == null) {
			customConfig = new CustomConfig("City/" + this.getName() + "(" + this.id + ")" , true);
			config = customConfig.getConfig();
		}else{
			customConfig = database;
			config = customConfig.getConfig();
		}
		
		config.set("ID", this.id);
		config.set("Nome", this.getName());
		config.set("Descrizione", this.descrizione);
		
		this.saveArea(config.getConfigurationSection("Area"));
		this.saveMembri(config.getConfigurationSection("Membri"));
		this.saveEdifici(config.getConfigurationSection("Edifici"));
		
		this.impostazioni.save(config.getConfigurationSection("Impostazioni"));
		this.regole.save(config.getConfigurationSection("Regole"));
		
		//Salva i dati nel file.
		if(!customConfig.save()) {
			throw new IOException(); //Errore nel salvataggio!
		}	
	}

	/**
	 * Salva solo i vari edifici della citt�
	 * @param configurationSection
	 */
	private void saveEdifici(ConfigurationSection config) {
		this.municipio.save(config);
	}

	/**
	 * Salva solo i membri della citt�
	 * Ci sono pi� ruoli, l'idea � quella i salvare raggruppando i ruoli e mettere ad esso una lista
	 * degli uuid che hanno quel ruolo, dato che si posso avere pi� ruoli questi si ripetono
	 * su pi� campi eventualmente.
	 * @param configurationSection
	 */
	private void saveMembri(ConfigurationSection config) {
		for(Membro mem : this.membri) {
			mem.save(config);
		}
	}

	/**
	 * Salva solo l'area della citt�
	 * @param configurationSection
	 */
	private void saveArea(ConfigurationSection config) {
		this.area.save(config);
	}

	@Override
	public void load(CustomConfig database) throws IOException {
		//prendi
		CustomConfig customConfig;
		FileConfiguration config;
		if(database == null) {
			customConfig = new CustomConfig("City/" + this.getName() + "(" + this.id + ")" , true);
			config = customConfig.getConfig();
		}else{
			customConfig = database;
			config = customConfig.getConfig();
		}
		
		this.id   			= config.getInt("ID", -1);
		if(this.id == -1) throw new IOException(); //Gestione errore.
		this.name 			= config.getString("Nome", "ErrorGetName");
		this.descrizione	= config.getString("Descrizione");
		
		this.membri = Membro.loadMembri(config.getConfigurationSection("Membri"));
		
		this.area.load(config.getConfigurationSection("Area"));
		this.municipio.load(config.getConfigurationSection("Municipio"));
		this.impostazioni.load(config.getConfigurationSection("Impostazioni"));
		this.regole.load(config.getConfigurationSection("Regole"));
	}
	
	/**
	 * Carica la citta sapendo il suo customConfig, se non esiste il file
	 * ritorna null, se � avvenuto un errore di lettura lanca una eccezione, e ritorna null.
	 * @param config
	 * @return
	 */
	public static City loadCityByCustomConfig(CustomConfig config) {
		City city = new City();
		try{
			city.load(config);
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
		if(!ListCity.contains(city)) {
			ListCity.add(city);
		}
		return null;
	}
	
	/**
	 * Carica la citt� sapendo solo il suo id.
	 * Da evitare poich� ha complessit� elevata
	 * @param id
	 * @return
	 */
	public static City loadCityByID(long id) {
		String[] files = Util.getListNameFile("City");
		for(String str : files) {
			CustomConfig customConfig = new CustomConfig("City/" + str);
			FileConfiguration config = customConfig.getConfig();
			if(config.getInt("ID") == id) {
				return City.loadCityByCustomConfig(customConfig);
			}
		}
		return null;
	}
	
	
	//************************************************************************************* Funzioni city
	/**
	 * @return
	 */
	private String getName() {
		return this.name;
	}

	/**
	 * @param idCity
	 * @return
	 */
	public static City getByID(long idCity) {
		for(City tmp : ListCity) {
			if(tmp.getId() == idCity) {
				return tmp;
			}
		}
		//Non ho trovato una citt� con quel id nella cache cerca ora fra tutti i file di salvataggio
		return City.loadCityByID(idCity);
	}

	/**
	 * @return
	 */
	public long getId() {
		return this.id;
	}

	//******************************************************************************************* TAG
	@Override
	public boolean hasTag(String tag) {
		return City.TagList.contains(tag);
	}

	@Override
	public ArrayList<String> getTagList() {
		return City.TagList;
	}

	@Override
	public String getValueFromTag(String str) {
		if(str == null || str.length() <= 2) return "(Error Invalid code: " + str + ")";
		switch(str) {
			case "%City.name%":{
				return this.getName();
			}
			case "%City.uuid%":{
				return this.getId() + "";
			}
			//...
			
		}
		
		return "(Error Invalid code: " + str + ")";
	}

	//************************************************************************************* Funzioni citt�
	/**
	 * Ritorna le impostazioni della citt�
	 * @return
	 */
	public Impostazioni getImpostazioni() {
		return this.impostazioni;
	}

	/**
	 * Ritorna se questo UUID � di un player che appartiene alla citt�
	 * @param uniqueId
	 * @return
	 */
	public boolean hasMembro(UUID uniqueId) {
		for(Membro mem : this.membri) {
			if(mem.getUUID().equals(uniqueId)) {
				return true;
			}
		}
		return false;
	}


}
