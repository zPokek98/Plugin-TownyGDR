/**
 * 
 */
package it.TownyGDR.Towny.City;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import it.CustomConfig.CustomConfig;
import it.TownyGDR.TownyGDR;
import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Tag.Taggable;
import it.TownyGDR.Towny.LuoghiType;
import it.TownyGDR.Towny.Luogo;
import it.TownyGDR.Towny.City.Area.Area;
import it.TownyGDR.Towny.City.Edifici.Municipio.Municipio;
import it.TownyGDR.Towny.City.Impostazioni.Impostazioni;
import it.TownyGDR.Towny.City.Membri.Membro;
import it.TownyGDR.Towny.City.Membri.MembroType;
import it.TownyGDR.Towny.City.Regole.Regole;
import it.TownyGDR.Towny.Zone.Zona;
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
 * Classe che descrivi la città nel suo insieme.
 * La città avrà bisogno di parecchie classi di supporto tra cui:
 * - Membri (Sindaco,cittadino,...)
 * - Area
 * - Regole
 * - Impostazioni
 * - Edifici
 * - Nazione
 * 
 *********************************************************************/
public class City extends Luogo implements Salva<CustomConfig>, Taggable{
	
	//Variabile di cache per le città per ottimizzare tempi e memoria
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
	
	//Membri città
	private ArrayList<Membro> membri;
	
	//Area della città
	private Area area;
	
	//Edifici
	private Municipio municipio;
	
	//Impostazioni
	private Impostazioni impostazioni;
	
	//Regole
	private Regole regole;
	
	/**
	 * Costruttore PRIVATO per il caricamento della città,
	 * la costruzione della città e demandato a una funzione
	 * "createCity"
	 */
	private City(String nome, PlayerData pd, Zona zon) {
		this.id 		 = -1;
		this.name 		 = nome;
		this.descrizione = "";
		this.membri 	 = new ArrayList<Membro>();
		this.membri.add(new Membro(pd.getUUID(), MembroType.Sindaco));
		
		this.area 		 = new Area(zon);
		this.municipio	 = new Municipio();
		this.impostazioni= new Impostazioni();
		this.regole		 = new Regole();
		
		ListCity.add(this);
	}
	
	/**
	 * Costruttore PRIVATO per il caricamento della città,
	 * la costruzione della città e demandato a una funzione
	 * "createCity"
	 */
	private City() {
		this.id 		 = -1;
		this.name 		 = null;
		this.descrizione = "";
		this.membri 	 = new ArrayList<Membro>();
		this.area 		 = new Area(null);
		this.municipio	 = new Municipio();
		this.impostazioni= new Impostazioni();
		this.regole		 = new Regole();
		
		ListCity.add(this);
	}
	
	/**
	 * Creare una città da zero
	 * Tutte le impostazioni della città saranno quelle di default
	 * Il fondatore è il primo sindaco, è l'unico membro iniziale
	 * @param pd
	 * @return
	 */
	public static City createCity(PlayerData pd, String nomeCitta, Zona zon) {
		//Controlla che la zona è libera
		if(zon.getLuogo() == null) {
			//libera
		}else {
			//non è libera
			return null;
		}
		
		
		City city = new City(nomeCitta, pd, zon);
		//Prendi l'id massimo fra le città
		city.id = City.getMaxID() + 1;
		
		//Il fondatore è automaticamente il sindaco
		//city.membri.add(new Membro(pd.getUUID(), MembroType.Sindaco));
		//city.name = nomeCitta;
		
		//Area inizliale? Al momento non c'è claim ??????????????
		//...
		
		try{
			city.save(null); //può anche essere null l'argomento
		}catch(IOException e){
			e.printStackTrace();
		}
		return city;
	}
	
	/**
	 * Ritorna il massimo id raggiunto.
	 * Nel caso per fondare una nuova città bisogna sommare +1 dopo.
	 * @return
	 */
	private static int getMaxID() {
		//Dato che le città posso anche non essere cariche nella RAM cerco in tutti i file di salvataggio
		int tmp = 0;
		String[] files = Util.getListNameFile("City");
		for(String str : files) {
			str = str.substring(0,str.length() - 4);
			CustomConfig customConfig = new CustomConfig("City/" + str, TownyGDR.getInstance());
			FileConfiguration config = customConfig.getConfig();
			if(config.getInt("ID") > tmp) {
				tmp = config.getInt("ID");
			}
		}
		return tmp;
	}

	/**
	 * Ritorna se 2 città sono uguali in base all'id
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
			customConfig = new CustomConfig("City" + File.separatorChar + this.getName() + "(" + this.id + ")" , true, TownyGDR.getInstance());
			config = customConfig.getConfig();
		}else{
			customConfig = database;
			config = customConfig.getConfig();
		}
		
		config.set("ID", this.id);
		config.set("Nome", this.getName());
		config.set("Descrizione", this.descrizione);
		
		if(!config.contains("Area")) config.set("Area.tmp", "tmp");
		this.saveArea(config.getConfigurationSection("Area"));
		if(config.contains("Area.tmp")) config.set("Area.tmp", null);
		
		if(!config.contains("Membri")) config.set("Membri.tmp", "tmp");
		this.saveMembri(config.getConfigurationSection("Membri"));
		if(config.contains("Membri.tmp")) config.set("Membri.tmp", null);
		
		if(!config.contains("Edifici")) config.set("Edifici.tmp", "tmp");
		this.saveEdifici(config.getConfigurationSection("Edifici"));
		if(config.contains("Edifici.tmp")) config.set("Edifici.tmp", null);
		
		if(!config.contains("Impostazioni")) config.set("Impostazioni.tmp", "tmp");
		this.impostazioni.save(config.getConfigurationSection("Impostazioni"));
		if(config.contains("Impostazioni.tmp")) config.set("Impostazioni.tmp", null);
		
		if(!config.contains("Regole")) config.set("Regole.tmp", "tmp");
		this.regole.save(config.getConfigurationSection("Regole"));
		if(config.contains("Regole.tmp")) config.set("Regole.tmp", null);
		
		//Salva i dati nel file.
		if(!customConfig.save()) {
			throw new IOException(); //Errore nel salvataggio!
		}	
	}

	/**
	 * Salva solo i vari edifici della città
	 * @param configurationSection
	 */
	private void saveEdifici(ConfigurationSection config) {
		this.municipio.save(config);
	}

	/**
	 * Salva solo i membri della città
	 * Ci sono più ruoli, l'idea è quella i salvare raggruppando i ruoli e mettere ad esso una lista
	 * degli uuid che hanno quel ruolo, dato che si posso avere più ruoli questi si ripetono
	 * su più campi eventualmente.
	 * @param configurationSection
	 */
	private void saveMembri(ConfigurationSection config) {
		try {
			Membro.save(config, this.membri);
		} catch (IOException e) {
			//e.printStackTrace();
			//Errore
		}
	}

	/**
	 * Salva solo l'area della città
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
			customConfig = new CustomConfig("City/" + this.getName() + "(" + this.id + ")" , true, TownyGDR.getInstance());
			config = customConfig.getConfig();
		}else{
			customConfig = database;
			config = customConfig.getConfig();
		}
		
		this.id   			= config.getInt("ID", -1);
		if(this.id == -1) throw new IOException(); //Gestione errore.
		this.name 			= config.getString("Nome", "ErrorGetName");
		this.descrizione	= config.getString("Descrizione");
		
		//prima i membri e poi l'area
		this.membri = Membro.loadMembri(config.getConfigurationSection("Membri"), this);
		
		this.area.load(config.getConfigurationSection("Area"));
		this.municipio.load(config.getConfigurationSection("Municipio"));
		this.impostazioni.load(config.getConfigurationSection("Impostazioni"));
		this.regole.load(config.getConfigurationSection("Regole"));
	}
	
	/**
	 * Carica la citta sapendo il suo customConfig, se non esiste il file
	 * ritorna null, se è avvenuto un errore di lettura lanca una eccezione, e ritorna null.
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
	 * Carica la città sapendo solo il suo id.
	 * Da evitare poichè ha complessità elevata
	 * @param id
	 * @return
	 */
	public static City loadCityByID(long id) {
		String[] files = Util.getListNameFile("City");
		for(String str : files) {
			str = str.substring(0,str.length() - 4);
			CustomConfig customConfig = new CustomConfig("City/" + str, TownyGDR.getInstance());
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
		//Non ho trovato una città con quel id nella cache cerca ora fra tutti i file di salvataggio
		return City.loadCityByID(idCity);
	}

	/**
	 * @return
	 */
	@Override
	public int getId() {
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

	//************************************************************************************* Funzioni città
	/**
	 * Ritorna le impostazioni della città
	 * @return
	 */
	public Impostazioni getImpostazioni() {
		return this.impostazioni;
	}

	/**
	 * Ritorna se questo UUID è di un player che appartiene alla città
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

	@Override
	public LuoghiType getType() {
		return LuoghiType.City;
	}

	/**
	 * Ritorna la lista dei sindaci
	 */
	public ArrayList<Membro> getSindaco() {
		ArrayList<Membro> tmp = new ArrayList<Membro>();
		for(Membro mem : this.membri) {
			if(mem.getType().contains(MembroType.Sindaco)) {
				tmp.add(mem);
			}
		}
		return tmp;
	}

	/**
	 * Ritorna i membri della città
	 * @return
	 */
	public ArrayList<Membro> getMembri() {
		return this.membri;
	}

	/**
	 * @return
	 */
	public Area getArea() {
		return this.area;
	}

	/**
	 * @return
	 */
	public CustomConfig getConfig() {
		return new CustomConfig("City/" + this.getName() + "(" + this.id + ")" , true, TownyGDR.getInstance());
	}


	/**
	 * Carica tutte le città
	 */
	public static void initCity() {
		String[] files = Util.getListNameFile("City");
		for(String str : files) {
			str = str.substring(0,str.length() - 4);
			City.loadCityByCustomConfig(new CustomConfig("City" + File.separatorChar + str, TownyGDR.getInstance()));
		}
	}
}
