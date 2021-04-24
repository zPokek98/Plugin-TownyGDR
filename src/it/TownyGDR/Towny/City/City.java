/**
 * 
 */
package it.TownyGDR.Towny.City;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
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
import it.TownyGDR.Util.Exception.ExceptionLoad;
import it.TownyGDR.Util.Exception.City.ExceptionCityImpossibleLoad;
import it.TownyGDR.Util.Exception.City.ExceptionCityImpossibleLoadArea;
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
 * Le città vengono vengono salvate in una array per cache, per la ricerca
 * si utilizza un opportuno id che identifica unicamente una città, l'id
 * è restituito da una funzione che prende il più grande presente fra tutte le 
 * città caricate e non presenti nel server,(ho scelto che le città devono avere
 * anche un nome univoco, quindi una possibile ricerca è data dal nome della città).
 * 
 * 
 * La città risiede su una zona e la sua area è vincolata da essa
 * la gestione dell'area della città e demandata alla classe Area.
 * 
 * I membri della città sono descritti singolarmente dalla classe Membro
 * usando come identificato UUID del player, il ruolo del player è dentro
 * l'oggetto membro.
 * Tutti i membri sono contenuti in un ArrayList.
 * 
 * Gli edifici della città, devono avere tutti l'interfaccia pubblica data
 * dalla classe "Edifici" il suo comportamento sarà demandato a una classe
 * appropriata per l'edificio, al momento ci stanno come edifici:
 * - un cazzo
 * 
 * La città può avere delle impostazioni?
 * Del tipo regolamentare le interazioni fra i membri della città o altro
 * Tutte le impostazioni sono dentro l'oggetto Impostazioni(contiente il set
 * creato)
 * 
 * La città ha anche una lista di "Regole" non gestite da codice in quanto saranno
 * regole imposte del sindaco/i in quanto comportarsi nella città.
 * 
 * Manca da fare:
 * 
 * 
 * 
 *********************************************************************/
public class City extends Luogo implements Salva<CustomConfig>, Taggable{
	
	//Variabile di cache per le città per ottimizzare tempi e memoria
	public static ArrayList<City> ListCity = new ArrayList<City>();
	
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
		this.id 		 = City.getMaxID() + 1;;
		this.name 		 = nome;
		this.descrizione = "";
		
		this.area 		 = new Area(zon);
		this.membri 	 = new ArrayList<Membro>();
		pd.setCity(this);
		this.membri.add(new Membro(pd.getUUID(), MembroType.Sindaco));
		
		zon.setLuogo(this);
		
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
		this.id 		 = City.getMaxID() + 1;;
		this.name 		 = null;
		this.descrizione = "";
		this.membri 	 = new ArrayList<Membro>();
		this.area 		 = new Area(null);
		this.municipio	 = new Municipio();
		this.impostazioni= new Impostazioni();
		this.regole		 = new Regole();
		
		ListCity.add(this);
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/**
	 * Creare una città da zero
	 * Tutte le impostazioni della città saranno quelle di default
	 * Il fondatore è il primo sindaco, è l'unico membro iniziale
	 * @param pd
	 * @return
	 */
	public static City createCity(PlayerData pd, String nomeCitta, Zona zon) {
		if(zon == null){
			return null;
		}
		
		//Controlla che la zona è libera
		if(zon.getLuogo() == null){
			//libera
		}else{
			//non è libera
			return null;
		}
		
		//modifichiamo il nome(la prima maiuscola e il resto miniscolo)
		nomeCitta = nomeCitta.toLowerCase();
		String primo = Character.toString(nomeCitta.charAt(0)).toUpperCase();
		nomeCitta = nomeCitta.substring(1);
		nomeCitta = primo + nomeCitta;
		
		//Controlla se nessun'altra città ha questo nome
		if(!checkIsFreeName(nomeCitta)) {
			return null;
		}
		
		//Alloca la città
		City city = new City(nomeCitta, pd, zon);
		
		//Prendi l'id massimo fra le città
		//city.id = City.getMaxID() + 1;
		
		//Il fondatore è automaticamente il sindaco
		//city.membri.add(new Membro(pd.getUUID(), MembroType.Sindaco));
		//city.name = nomeCitta;
		
		//Area inizliale? Al momento non c'è claim ??????????????
		//...
		
		try{
			city.save(null); //può anche essere null l'argomento
		}catch(IOException e){
			//Non riesce a salvare la città...
			Bukkit.getConsoleSender().sendMessage("Impossibile salvare la città di nome e id: " + city.name + city.id);
		}
		return city;
	}
	
	/**
	 * @param nomeCitta
	 * @return
	 */
	private static boolean checkIsFreeName(String nomeCitta) {
		//controlla se il nome contiene degli spazi indesiderati
		if(nomeCitta.contains(" ")) {
			return false;
		}
		
		//Dato che le città posso anche non essere cariche nella RAM cerco in tutti i file di salvataggio
		String[] files = Util.getListNameFile("City");
		for(String str : files) {
			//Per ogni "file", in realtà nome del file.
			str = str.substring(0,str.length() - 4); //rimuobi il ".yml" alla fine
			CustomConfig customConfig = new CustomConfig("City/" + str, TownyGDR.getInstance());
			FileConfiguration config = customConfig.getConfig();
			//confronta i nomi
			if(config.getString("Nome").equalsIgnoreCase(str)) {
				return false;
			}
		}
		return true;
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
			//Per ogni "file", in realtà nome del file.
			str = str.substring(0,str.length() - 4); //rimuobi il ".yml" alla fine
			CustomConfig customConfig = new CustomConfig("City/" + str, TownyGDR.getInstance());
			FileConfiguration config = customConfig.getConfig();
			//confronta gli id
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
		try{
			Membro.save(config, this.membri);
		}catch (IOException e){
			//e.printStackTrace();
			//non ci entrerà mai
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
	public void load(CustomConfig database) throws IOException, ExceptionLoad {
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
	 * @throws ExceptionCityImpossibleLoad 
	 * @throws ExceptionCityImpossibleLoadArea 
	 */
	public static City loadCityByCustomConfig(CustomConfig config) throws ExceptionCityImpossibleLoad {
		City city = new City();
		try{
			try{
				city.load(config);
			}catch(ExceptionLoad e){
				//Impossibile caricare la città
				String mes = "Impossibile cariare la città di nome e id: " + config.getFile().getName();
				throw new ExceptionCityImpossibleLoad(mes);
			}
		}catch(IOException e){
			//non ci entrerà mai
		}
		if(!ListCity.contains(city)) {
			ListCity.add(city);
		}
		return city;
	}
	
	/**
	 * Carica la città sapendo solo il suo id.
	 * Da evitare poichè ha complessità elevata
	 * @param id
	 * @return
	 * @throws ExceptionCityImpossibleLoad 
	 */
	public static City loadCityByID(long id) throws ExceptionCityImpossibleLoad {
		String[] files = Util.getListNameFile("City");
		for(String str : files) {
			//per ogni "file"
			str = str.substring(0,str.length() - 4); //elimina ".yml" alla fine
			
			CustomConfig customConfig = new CustomConfig("City" + File.separatorChar + str, TownyGDR.getInstance());
			FileConfiguration config = customConfig.getConfig();
			//trova il file che contiene come parametro su "ID" il valore dato alla funzione
			if(config.getInt("ID") == id) {
				//Trovata al città da caricare
				return City.loadCityByCustomConfig(customConfig);
			}
		}
		return null;
	}
	
	
	//************************************************************************************* Funzioni city
	/**
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param idCity
	 * @return
	 * @throws ExceptionCityImpossibleLoad 
	 */
	public static City getByID(long idCity) throws ExceptionCityImpossibleLoad {
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
	
	/**
	 * Ritorna se questo UUID è di un Sindaco della città
	 * @param uniqueId
	 * @return
	 */
	public boolean hasSindaco(UUID uniqueId) {
		for(Membro mem : this.membri) {
			if(mem.getUUID().equals(uniqueId)) {
				if(mem.getType().contains(MembroType.Sindaco)) {
					return true;
				}
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
	 * Aggiungi un membro alla città con il ruolo specificato
	 * @param pd
	 * @return
	 */
	public boolean addMembro(PlayerData pd, MembroType type) {
		//controlliamo che il pirla non è in un'altra città
		if(pd.getCity() == null) {
			//non è in un'altra città
			pd.setCity(this);
			return this.membri.add(new Membro(pd.getUUID(), type));
		}
		return false;
	}
	
	/**
	 * Aggiungi un nuovo membro con il ruolo di cittadino
	 * @param pd
	 * @return
	 */
	public boolean addMembro(PlayerData pd) {
		return this.addMembro(pd, MembroType.Cittadino);
	}
	
	/**
	 * Rimuovi dalla città il membro dato il Membro.
	 * @param mem
	 * @return
	 */
	public boolean removeMembro(Membro mem) {
		if(this.membri.contains(mem)) {
			PlayerData.getFromUUID(mem.getUUID()).setCity(null);
			return this.membri.remove(mem);
		}
		return false;
	}
	
	/**
	 * Rimuovi il membro dato dal player passato se presente
	 * @param pd
	 * @return
	 */
	public boolean removeMembro(PlayerData pd) {
		for(Membro mem : this.membri) {
			if(mem.getUUID().equals(pd.getUUID())) {
				//trovato il membro del player
				pd.setCity(null);
				return this.membri.remove(mem);
			}
		}
		return false;
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
	 * @throws ExceptionCityImpossibleLoad 
	 */
	public static void initCity() throws ExceptionCityImpossibleLoad {
		String[] files = Util.getListNameFile("City");
		for(String str : files) {
			str = str.substring(0,str.length() - 4);
			try{
				City.loadCityByCustomConfig(new CustomConfig("City" + File.separatorChar + str, TownyGDR.getInstance()));
			}catch(ExceptionCityImpossibleLoad e){
				Bukkit.getConsoleSender().sendMessage(e.getMessage());
				throw new ExceptionCityImpossibleLoad(e.getMessage());
			}
		}
	}

	/**
	 * Ritorna il membro della città se esiste tramite un UUID
	 * @param uuid
	 * @return
	 */
	public Membro getMembroByUUID(UUID uuid) {
		for(Membro mem : this.membri) {
			if(mem.getUUID().equals(uuid)) {
				return mem;
			}
		}
		return null;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param descrizione the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param string
	 * @return
	 */
	public static City getByName(String name) {
		for(City city : City.ListCity) {
			if(city.getName().equalsIgnoreCase(name)) {
				return city;
			}
		}
		return null;
	}
	
	
	
	
	
}
