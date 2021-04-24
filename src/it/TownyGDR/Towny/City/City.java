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
 * Classe che descrivi la citt� nel suo insieme.
 * La citt� avr� bisogno di parecchie classi di supporto tra cui:
 * - Membri (Sindaco,cittadino,...)
 * - Area
 * - Regole
 * - Impostazioni
 * - Edifici
 * - Nazione
 * 
 * Le citt� vengono vengono salvate in una array per cache, per la ricerca
 * si utilizza un opportuno id che identifica unicamente una citt�, l'id
 * � restituito da una funzione che prende il pi� grande presente fra tutte le 
 * citt� caricate e non presenti nel server,(ho scelto che le citt� devono avere
 * anche un nome univoco, quindi una possibile ricerca � data dal nome della citt�).
 * 
 * 
 * La citt� risiede su una zona e la sua area � vincolata da essa
 * la gestione dell'area della citt� e demandata alla classe Area.
 * 
 * I membri della citt� sono descritti singolarmente dalla classe Membro
 * usando come identificato UUID del player, il ruolo del player � dentro
 * l'oggetto membro.
 * Tutti i membri sono contenuti in un ArrayList.
 * 
 * Gli edifici della citt�, devono avere tutti l'interfaccia pubblica data
 * dalla classe "Edifici" il suo comportamento sar� demandato a una classe
 * appropriata per l'edificio, al momento ci stanno come edifici:
 * - un cazzo
 * 
 * La citt� pu� avere delle impostazioni?
 * Del tipo regolamentare le interazioni fra i membri della citt� o altro
 * Tutte le impostazioni sono dentro l'oggetto Impostazioni(contiente il set
 * creato)
 * 
 * La citt� ha anche una lista di "Regole" non gestite da codice in quanto saranno
 * regole imposte del sindaco/i in quanto comportarsi nella citt�.
 * 
 * Manca da fare:
 * 
 * 
 * 
 *********************************************************************/
public class City extends Luogo implements Salva<CustomConfig>, Taggable{
	
	//Variabile di cache per le citt� per ottimizzare tempi e memoria
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
	 * Costruttore PRIVATO per il caricamento della citt�,
	 * la costruzione della citt� e demandato a una funzione
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
	 * Creare una citt� da zero
	 * Tutte le impostazioni della citt� saranno quelle di default
	 * Il fondatore � il primo sindaco, � l'unico membro iniziale
	 * @param pd
	 * @return
	 */
	public static City createCity(PlayerData pd, String nomeCitta, Zona zon) {
		if(zon == null){
			return null;
		}
		
		//Controlla che la zona � libera
		if(zon.getLuogo() == null){
			//libera
		}else{
			//non � libera
			return null;
		}
		
		//modifichiamo il nome(la prima maiuscola e il resto miniscolo)
		nomeCitta = nomeCitta.toLowerCase();
		String primo = Character.toString(nomeCitta.charAt(0)).toUpperCase();
		nomeCitta = nomeCitta.substring(1);
		nomeCitta = primo + nomeCitta;
		
		//Controlla se nessun'altra citt� ha questo nome
		if(!checkIsFreeName(nomeCitta)) {
			return null;
		}
		
		//Alloca la citt�
		City city = new City(nomeCitta, pd, zon);
		
		//Prendi l'id massimo fra le citt�
		//city.id = City.getMaxID() + 1;
		
		//Il fondatore � automaticamente il sindaco
		//city.membri.add(new Membro(pd.getUUID(), MembroType.Sindaco));
		//city.name = nomeCitta;
		
		//Area inizliale? Al momento non c'� claim ??????????????
		//...
		
		try{
			city.save(null); //pu� anche essere null l'argomento
		}catch(IOException e){
			//Non riesce a salvare la citt�...
			Bukkit.getConsoleSender().sendMessage("Impossibile salvare la citt� di nome e id: " + city.name + city.id);
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
		
		//Dato che le citt� posso anche non essere cariche nella RAM cerco in tutti i file di salvataggio
		String[] files = Util.getListNameFile("City");
		for(String str : files) {
			//Per ogni "file", in realt� nome del file.
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
	 * Nel caso per fondare una nuova citt� bisogna sommare +1 dopo.
	 * @return
	 */
	private static int getMaxID() {
		//Dato che le citt� posso anche non essere cariche nella RAM cerco in tutti i file di salvataggio
		int tmp = 0;
		String[] files = Util.getListNameFile("City");
		for(String str : files) {
			//Per ogni "file", in realt� nome del file.
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
		try{
			Membro.save(config, this.membri);
		}catch (IOException e){
			//e.printStackTrace();
			//non ci entrer� mai
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
	 * ritorna null, se � avvenuto un errore di lettura lanca una eccezione, e ritorna null.
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
				//Impossibile caricare la citt�
				String mes = "Impossibile cariare la citt� di nome e id: " + config.getFile().getName();
				throw new ExceptionCityImpossibleLoad(mes);
			}
		}catch(IOException e){
			//non ci entrer� mai
		}
		if(!ListCity.contains(city)) {
			ListCity.add(city);
		}
		return city;
	}
	
	/**
	 * Carica la citt� sapendo solo il suo id.
	 * Da evitare poich� ha complessit� elevata
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
				//Trovata al citt� da caricare
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
		//Non ho trovato una citt� con quel id nella cache cerca ora fra tutti i file di salvataggio
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
	
	/**
	 * Ritorna se questo UUID � di un Sindaco della citt�
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
	 * Ritorna i membri della citt�
	 * @return
	 */
	public ArrayList<Membro> getMembri() {
		return this.membri;
	}
	
	/**
	 * Aggiungi un membro alla citt� con il ruolo specificato
	 * @param pd
	 * @return
	 */
	public boolean addMembro(PlayerData pd, MembroType type) {
		//controlliamo che il pirla non � in un'altra citt�
		if(pd.getCity() == null) {
			//non � in un'altra citt�
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
	 * Rimuovi dalla citt� il membro dato il Membro.
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
	 * Carica tutte le citt�
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
	 * Ritorna il membro della citt� se esiste tramite un UUID
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
